package com.starocean.fileCopy.db.mysql.entity;


import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("file_sync_state")
public class FileSyncState implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = -3125980503187287399L;

	@TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String sourceFileId;

    private String relativePath;

    private String fileName;

    private LocalDateTime effectiveTime;

    private Long sourceFileSize;

    private LocalDateTime downloadTime;

    /**
     * 0-成功 1-失败
     */
    private Integer syncStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // -------- getters / setters --------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSourceFileId() {
        return sourceFileId;
    }

    public void setSourceFileId(String sourceFileId) {
        this.sourceFileId = sourceFileId;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(LocalDateTime effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public Long getSourceFileSize() {
        return sourceFileSize;
    }

    public void setSourceFileSize(Long sourceFileSize) {
        this.sourceFileSize = sourceFileSize;
    }

    public LocalDateTime getDownloadTime() {
        return downloadTime;
    }

    public void setDownloadTime(LocalDateTime downloadTime) {
        this.downloadTime = downloadTime;
    }

    public Integer getSyncStatus() {
        return syncStatus;
    }

    public void setSyncStatus(Integer syncStatus) {
        this.syncStatus = syncStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "FileSyncState{" +
                "id=" + id +
                ", sourceFileId=" + sourceFileId +
                ", relativePath='" + relativePath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", effectiveTime=" + effectiveTime +
                ", sourceFileSize=" + sourceFileSize +
                ", downloadTime=" + downloadTime +
                ", syncStatus=" + syncStatus +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}

