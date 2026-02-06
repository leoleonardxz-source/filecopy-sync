package com.starocean.fileCopy.sync.service;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starocean.fileCopy.Util.PathUtils;
import com.starocean.fileCopy.Util.WeDrivePathUtil;
import com.starocean.fileCopy.db.mysql.entity.WedriveFolderMapping;
import com.starocean.fileCopy.db.mysql.mapper.WedriveFolderMappingMapper;
import com.starocean.fileCopy.wechat.api.WechatWeDriveApi;
import com.starocean.fileCopy.wechat.client.WechatApiClient;
import com.starocean.fileCopy.wechat.model.FileCreateResp;

@Service
public class WeDriveFolderMappingService {

    private final WedriveFolderMappingMapper folderMapper;
    private final WechatApiClient wechatApiClient;
    private final Map<String, String> folderIdCache = new ConcurrentHashMap<>();

    public WeDriveFolderMappingService(WedriveFolderMappingMapper folderMapper,
                                       WechatApiClient wechatApiClient) {
        this.folderMapper = folderMapper;
        this.wechatApiClient = wechatApiClient;
    }

    /**
     * 确保目录链存在，返回最终目录的 folder_file_id。
     *
     * @param spaceId 微盘空间ID
     * @param rootFolderId 微盘根目录ID（如果你的“根”不是空，而是一个固定 root file_id，就传它）
     * @param dirs 目录列表（不含文件名），例如 ["625910","payment","2025","02"]
     */
    @Transactional // 仅保证本地库插入的原子性；微盘调用本身是远程，不在事务里回滚
    public String ensureFolders(String spaceId, String rootFolderId, List<String> dirs) {
    	//如果没有rootFolderId，就默认spaceId
    	rootFolderId = StringUtils.hasText(rootFolderId) ? rootFolderId : spaceId;
        if (dirs == null || dirs.isEmpty()) {
            // 没有子目录，直接返回 root
            return rootFolderId;
        }
        
        String parentFileId = rootFolderId; // 你可认为 rootFolderId 就是第一层的 parent
        StringBuilder fullPathBuilder = new StringBuilder();
        StringBuilder pathKey = new StringBuilder(spaceId).append(":");
        for (int i = 0; i < dirs.size(); i++) {
            String folderName = safeName(dirs.get(i));
            if (folderName.isBlank()) continue;
            if (fullPathBuilder.length() > 0) fullPathBuilder.append("/");
            fullPathBuilder.append(folderName);

            String fullPath = PathUtils.getFilePathWithoutFileName(fullPathBuilder.toString());
            byte[] pathHash = WeDrivePathUtil.sha256Path(spaceId, fullPath);
            pathKey.append("/").append(folderName);
            String cacheKey = pathKey.toString();
            //从内存中拿folder对应的file_id
            String cached = folderIdCache.get(cacheKey);
            //能拿到就说明之前创建过了，不用再调微盘的接口去创建了
            if (cached != null) {
            	parentFileId = cached;
                continue;
            } else {
            	// 如果内存中没查到，再去MySQL中查询（space + full_path_hash）
                WedriveFolderMapping existing = selectByPathHash(spaceId, pathHash);
                if (existing != null && existing.getStatus() != null && existing.getStatus() == 1) {
                    parentFileId = existing.getFolderFileId();
                    continue;
                }
            }
            // 2) 本地无 -> 调微盘创建目录
            String createdFolderId = createFolderRemote(spaceId, parentFileId, folderName);
            // 放入内存缓存
            folderIdCache.put(cacheKey, createdFolderId);

            // 3) 落库（并发下可能冲突：用唯一键保证幂等）
            WedriveFolderMapping row = new WedriveFolderMapping();
            row.setSpaceId(spaceId);
            row.setFolderFileId(createdFolderId);
            row.setParentFileId(parentFileId);
            row.setFolderName(folderName);
            row.setFullPath(fullPath);
            row.setFullPathHash(pathHash);
            row.setDepth(i + 1);
            row.setStatus(1);

            tryInsertOrRecover(spaceId, pathHash, row);

            // 4) 下一层 parent
            parentFileId = createdFolderId;
        }

        return parentFileId;
    }

    /**
     * 如果你已经有 full_path（例如从 relative_path 截取出来），也可以直接用这个版本。
     * @param fullPath "625910/payment/2025/02"（不含首尾/）
     */
    @Transactional
    public String ensureByFullPath(String spaceId, String rootFolderId, String fullPath) {
        String normalized = PathUtils.getFilePathWithoutFileName(fullPath);;
        if (normalized.isBlank()) return rootFolderId;

        List<String> dirs = List.of(normalized.split("/"));
        return ensureFolders(spaceId, rootFolderId, dirs);
    }

    // ====================== 内部方法 ======================

    private WedriveFolderMapping selectByPathHash(String spaceId, byte[] pathHash) {
        return folderMapper.selectOne(new LambdaQueryWrapper<WedriveFolderMapping>()
                .eq(WedriveFolderMapping::getSpaceId, spaceId)
                .eq(WedriveFolderMapping::getFullPathHash, pathHash)
                .last("limit 1"));
    }

    /**
     * 调用微盘创建目录，返回 folder_file_id（微盘的 file_id）
     *
     * ⚠️ 注意：微盘接口字段名可能与你实际文档不同，
     * 这里沿用你之前的示例：spaceid/parentid/file_name/file_type
     */
    private String createFolderRemote(String spaceId, String parentFileId, String folderName) {
        Map<String, Object> body = Map.of(
                "spaceid", spaceId,
                "fatherid", parentFileId,
                "file_name", folderName,
                "file_type", 1// 1 表示文件夹,目前只支持1
        );

        FileCreateResp resp = wechatApiClient.call(
                WechatWeDriveApi.FILE_CREATE,
                null,
                null,
                body,
                FileCreateResp.class
        ).block();

        if (resp == null) {
            throw new IllegalStateException("WeDrive FILE_CREATE resp is null");
        }

        // 常见返回字段：fileid / file_id（以你的接口实际为准）
        String fileId = resp.getFileId();

        if (fileId == null) {
            throw new IllegalStateException("WeDrive FILE_CREATE missing fileid, resp=" + resp);
        }
        return String.valueOf(fileId);
    }

    /**
     * 并发幂等处理策略：
     * - 正常 insert 成功：OK
     * - insert 失败（唯一键冲突）：
     *   说明别人已经写入了相同 path 的记录 -> 回查拿到真实 folder_file_id
     */
    private void tryInsertOrRecover(String spaceId, byte[] pathHash, WedriveFolderMapping row) {
        try {
            folderMapper.insert(row);
        } catch (Exception duplicateOrOther) {
            WedriveFolderMapping exists = selectByPathHash(spaceId, pathHash);
            if (exists != null) {
                return; // 并发情况下，别人已插入，直接用即可
            }
            // 如果回查也没有，那就不是唯一键冲突（或库异常），抛出原异常便于定位
            throw duplicateOrOther;
        }
    }

    private String safeName(String s) {
        if (s == null) return "";
        // 去掉两端空格，并避免出现类似 "a//b" 分隔问题
        String t = s.trim();
        // 微盘一般不允许 "/" 作为目录名的一部分
        t = t.replace("/", "_").replace("\\", "_");
        return t;
    }
}

