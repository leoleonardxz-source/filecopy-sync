package com.starocean.fileCopy.sync.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.starocean.fileCopy.Util.WeDriveBlockShaUtil;
import com.starocean.fileCopy.db.mysql.entity.WedriveFileMapping;
import com.starocean.fileCopy.sync.model.UploadInitResp;
import com.starocean.fileCopy.sync.model.WeDriveUploadConstants;
import com.starocean.fileCopy.wechat.api.WechatWeDriveApi;
import com.starocean.fileCopy.wechat.client.WechatApiClient;
import com.starocean.fileCopy.wechat.model.FileIdResp;
import com.starocean.fileCopy.wechat.model.FileMoveResp;
import com.starocean.fileCopy.wechat.model.WechatBaseResp;

@Service
public class WeDriveUploadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(WeDriveUploadService.class);
	
    private final WechatApiClient wechatApiClient;
    

    public WeDriveUploadService(WechatApiClient wechatApiClient) {
        this.wechatApiClient = wechatApiClient;
    }

    public String uploadAndReturnFileId(String spaceId, String parentFolderId, Path localFile, String fileName) throws Exception {
    	String wechatCloudFileId = null;
        long size = Files.size(localFile);

        if (size <= WeDriveUploadConstants.DIRECT_UPLOAD_THRESHOLD) {
        	wechatCloudFileId = uploadDirect(spaceId, parentFolderId, localFile, fileName);
        } else {
        	// large file, use multipart upload
        	String uploadKey = uploadByParts(spaceId, parentFolderId, localFile, fileName, size);
        	//如果触发秒传，则uploadKey为null
        	if (StringUtils.hasText(uploadKey)) {
				//全部上传完成后，调用finish接口
        		wechatCloudFileId = uploadFinish(uploadKey);
			}
        }
		return wechatCloudFileId;
    }
    
    public FileMoveResp moveFile(String moveFolderId, WedriveFileMapping existingWeDriveFile) {
    	if (existingWeDriveFile != null && StringUtils.hasText(existingWeDriveFile.getWedriveFileId())) {
    		Map<String, Object> body = Map.of(
    				"fatherid", moveFolderId,
                    "replace", false,
                    "fileid", List.of(existingWeDriveFile.getWedriveFileId())
            );
            FileMoveResp block = wechatApiClient.call(WechatWeDriveApi.FILE_MOVE, null, null, body, FileMoveResp.class).block();
            LOGGER.debug("method [moveFile] , result is : {}",block.toString());
            return block;
    	}
		return null;
	}

    private String uploadFinish(String uploadKey) {
    	//https://qyapi.weixin.qq.com/cgi-bin/wedrive/file_upload_finish?access_token=ACCESS_TOKEN
    	if(StringUtils.hasText(uploadKey)) {
			Map<String, Object> body = Map.of(
					"upload_key", uploadKey
			);

			FileIdResp block = wechatApiClient.call(WechatWeDriveApi.WEDRIVE_FILE_UPLOAD_FINISH, null, null, body, FileIdResp.class).block();
			LOGGER.debug(block.toString());
			String fileId = block.getFileId();
			return fileId;
		}
		return null;
	}

	private String uploadDirect(String spaceId, String parentFolderId, Path file, String fileName) throws Exception {
        byte[] bytes = Files.readAllBytes(file);
        String base64 = Base64.getEncoder().encodeToString(bytes);

        Map<String, Object> body = Map.of(
                "spaceid", spaceId,
                "fatherid", parentFolderId,
                "file_name", fileName,
                "file_base64_content", base64
        );
        FileIdResp block = wechatApiClient.call(WechatWeDriveApi.FILE_UPLOAD, null, null, body, FileIdResp.class).block();
        LOGGER.debug(block.toString());
        String fileId = block.getFileId();
        return fileId;
    }

    private String uploadByParts(String spaceId, String parentFolderId, Path file, String fileName, long size) throws Exception {
    	String uploadKey = null;
    	//init part 需要对文件进行sha计算
    	List<String> blockSha = WeDriveBlockShaUtil.calcBlockSha(file);
        UploadInitResp init = uploadInit(spaceId, parentFolderId, fileName, size,blockSha);
        if (init.isHitRapidUpload()) {
            return uploadKey;
        } else {
			uploadKey = init.getUploadKey();
		}
        int partSize = WeDriveUploadConstants.PART_SIZE;

        try (InputStream in = Files.newInputStream(file)) {
            byte[] buf = new byte[partSize];
            int read;
            int partSeq = 1;

            while ((read = in.read(buf)) != -1) {
                byte[] chunk = (read == buf.length) ? buf : copyOf(buf, read);
                String base64 = Base64.getEncoder().encodeToString(chunk);

                uploadPart(spaceId, uploadKey, partSeq, base64);
                partSeq++;
            }
        }
        return uploadKey;
        // If doc requires commit/finalize, add here.
    }

    private UploadInitResp uploadInit(String spaceId, String parentFolderId, String fileName, long size,List<String> blockSha) {
        Map<String, Object> body = Map.of(
                "spaceid", spaceId,
                "fatherid", parentFolderId,
                "file_name", fileName,
                "size", size,
                "block_sha",blockSha
        );
        UploadInitResp block = wechatApiClient.call(
                WechatWeDriveApi.FILE_UPLOAD_INIT, null, null, body, UploadInitResp.class
        ).block();
        return block;
    }

    private void uploadPart(String spaceId, String uploadKey, int partSeq, String base64) {
        Map<String, Object> body = Map.of(
                "upload_key", uploadKey,
                "index", partSeq,
                "file_base64_content", base64
        );
        wechatApiClient.call(WechatWeDriveApi.FILE_UPLOAD_PART, null, null, body, WechatBaseResp.class).block();
    }

    private static byte[] copyOf(byte[] src, int len) {
        byte[] dst = new byte[len];
        System.arraycopy(src, 0, dst, 0, len);
        return dst;
    }

}
