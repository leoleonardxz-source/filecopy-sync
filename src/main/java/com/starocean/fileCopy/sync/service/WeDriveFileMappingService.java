package com.starocean.fileCopy.sync.service;


import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starocean.fileCopy.Util.PathUtils;
import com.starocean.fileCopy.Util.WeDrivePathUtil;
import com.starocean.fileCopy.db.mysql.entity.WedriveFileMapping;
import com.starocean.fileCopy.db.mysql.mapper.WedriveFileMappingMapper;
import com.starocean.fileCopy.db.sqlserver.dto.AttachmentFileDTO;

@Service
public class WeDriveFileMappingService {

	private final WedriveFileMappingMapper fileMapper;

	public WeDriveFileMappingService(WedriveFileMappingMapper fileMapper) {
		this.fileMapper = fileMapper;
	}

	/**
	 * 按 spaceId + sourceFileId 幂等保存（存在则更新，不存在则插入）
	 */
	public void upsertSuccess(String spaceId,
			AttachmentFileDTO row,
			String weDriveFileId,
			String folderFullPath,
			String fileName) {

		String relativePathContainFile = WeDrivePathUtil.normalizeRelativePathContainFile(row.getRelativePath());
		String onlyFolderNoFile = PathUtils.getFilePathWithoutFileName(folderFullPath);

		byte[] relHash = WeDrivePathUtil.sha256Path(spaceId, relativePathContainFile);
		byte[] folderHash = WeDrivePathUtil.sha256Path(spaceId, onlyFolderNoFile);

		WedriveFileMapping existing = fileMapper.selectOne(new LambdaQueryWrapper<WedriveFileMapping>()
				.eq(WedriveFileMapping::getSpaceId, spaceId)
				.eq(WedriveFileMapping::getSourceFileId, row.getFileId())
				.orderByDesc(WedriveFileMapping::getId)
				.last("limit 1"));

		if (existing == null) {
			WedriveFileMapping ins = new WedriveFileMapping();
			ins.setSpaceId(spaceId);
			ins.setSourceFileId(row.getFileId());
			ins.setWedriveFileId(weDriveFileId);
			ins.setRelativePath(relativePathContainFile);
			ins.setRelativePathHash(relHash);
			ins.setFolderFullPath(onlyFolderNoFile);
			ins.setFolderFullPathHash(folderHash);
			ins.setFileName(fileName);
			ins.setSourceEffectiveTime(row.getEffectiveTime());
			ins.setFileSize(row.getFileSize());
			ins.setStatus(1);
			ins.setRetryCount(0);
			ins.setLastError(null);
			ins.setLastSyncTime(LocalDateTime.now());
			fileMapper.insert(ins);
		} else {
			existing.setWedriveFileId(weDriveFileId);
			existing.setRelativePath(relativePathContainFile);
			existing.setRelativePathHash(relHash);
			existing.setFolderFullPath(onlyFolderNoFile);
			existing.setFolderFullPathHash(folderHash);
			existing.setFileName(fileName);
			existing.setSourceEffectiveTime(row.getEffectiveTime());
			existing.setFileSize(row.getFileSize());
			existing.setStatus(1);
			existing.setRetryCount(0);
			existing.setLastError(null);
			existing.setLastSyncTime(LocalDateTime.now());
			fileMapper.updateById(existing);
		}
	}

	public void markFailed(String spaceId, String sourceFileId, String errorMsg) {
		WedriveFileMapping existing = fileMapper.selectOne(new LambdaQueryWrapper<WedriveFileMapping>()
				.eq(WedriveFileMapping::getSpaceId, spaceId)
				.eq(WedriveFileMapping::getSourceFileId, sourceFileId)
				.orderByDesc(WedriveFileMapping::getId)
				.last("limit 1"));
		
		if (existing == null) return;
		
		existing.setStatus(2);
		existing.setRetryCount(existing.getRetryCount() == null ? 1 : existing.getRetryCount() + 1);
		existing.setLastError(errorMsg == null ? null : (errorMsg.length() > 1000 ? errorMsg.substring(0, 1000) : errorMsg));
		existing.setLastSyncTime(LocalDateTime.now());
		fileMapper.updateById(existing);
	}

	public WedriveFileMapping getWeDriveFileId(String spaceId, String fileId) {
		if (StringUtils.hasText(fileId) && StringUtils.hasText(spaceId)) {
			return fileMapper.selectOne(new LambdaQueryWrapper<WedriveFileMapping>()
					.eq(WedriveFileMapping::getSpaceId, spaceId)
					.eq(WedriveFileMapping::getSourceFileId, fileId)
					.orderByDesc(WedriveFileMapping::getId)
					.last("limit 1"));
		}
		return null;
	}
	
	public void updateByEntity(WedriveFileMapping existingWeDriveFile) {
		if (existingWeDriveFile != null && existingWeDriveFile.getId() != null) {
			WedriveFileMapping selectById = fileMapper.selectById(existingWeDriveFile.getId());
			if (selectById == null) {
				return;
			}
			fileMapper.updateById(existingWeDriveFile);
		}
	}

	public void updateByWeDriveFileIdAndSourceFileId(String wedriveFileId, String sourceFileId) {
		if (StringUtils.hasText(wedriveFileId) && StringUtils.hasText(sourceFileId)) {
			WedriveFileMapping existing = fileMapper.selectOne(new LambdaQueryWrapper<WedriveFileMapping>()
					.eq(WedriveFileMapping::getSourceFileId, sourceFileId)
					.eq(WedriveFileMapping::getWedriveFileId, wedriveFileId)
					.orderByDesc(WedriveFileMapping::getId)
					.last("limit 1"));
			if (existing != null) {
				fileMapper.updateById(existing);
			}
		}
	}

	
}
