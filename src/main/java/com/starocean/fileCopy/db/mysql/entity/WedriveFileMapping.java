package com.starocean.fileCopy.db.mysql.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("wedrive_file_mapping")
public class WedriveFileMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String spaceId;

    private String sourceFileId;
    private String wedriveFileId;

    private String relativePath;
    private byte[] relativePathHash;

    private String folderFullPath;
    private byte[] folderFullPathHash;

    private String fileName;

    private LocalDateTime sourceEffectiveTime;
    private Long fileSize;

    private Integer status;
    private LocalDateTime lastSyncTime;
    private Integer retryCount;
    private String lastError;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ===== getters/setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSpaceId() { return spaceId; }
    public void setSpaceId(String spaceId) { this.spaceId = spaceId; }

    public String getSourceFileId() { return sourceFileId; }
    public void setSourceFileId(String sourceFileId) { this.sourceFileId = sourceFileId; }

    public String getWedriveFileId() { return wedriveFileId; }
    public void setWedriveFileId(String wedriveFileId) { this.wedriveFileId = wedriveFileId; }

    public String getRelativePath() { return relativePath; }
    public void setRelativePath(String relativePath) { this.relativePath = relativePath; }

    public byte[] getRelativePathHash() { return relativePathHash; }
    public void setRelativePathHash(byte[] relativePathHash) { this.relativePathHash = relativePathHash; }

    public String getFolderFullPath() { return folderFullPath; }
    public void setFolderFullPath(String folderFullPath) { this.folderFullPath = folderFullPath; }

    public byte[] getFolderFullPathHash() { return folderFullPathHash; }
    public void setFolderFullPathHash(byte[] folderFullPathHash) { this.folderFullPathHash = folderFullPathHash; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public LocalDateTime getSourceEffectiveTime() { return sourceEffectiveTime; }
    public void setSourceEffectiveTime(LocalDateTime sourceEffectiveTime) { this.sourceEffectiveTime = sourceEffectiveTime; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public LocalDateTime getLastSyncTime() { return lastSyncTime; }
    public void setLastSyncTime(LocalDateTime lastSyncTime) { this.lastSyncTime = lastSyncTime; }

    public Integer getRetryCount() { return retryCount; }
    public void setRetryCount(Integer retryCount) { this.retryCount = retryCount; }

    public String getLastError() { return lastError; }
    public void setLastError(String lastError) { this.lastError = lastError; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
