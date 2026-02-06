package com.starocean.fileCopy.db.mysql.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("wedrive_folder_mapping")
public class WedriveFolderMapping implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String spaceId;
    private String folderFileId;
    private String parentFileId;

    private String folderName;
    private String fullPath;
    private byte[] fullPathHash;

    private Integer depth;
    private Integer status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ===== getters/setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSpaceId() { return spaceId; }
    public void setSpaceId(String spaceId) { this.spaceId = spaceId; }

    public String getFolderFileId() { return folderFileId; }
    public void setFolderFileId(String folderFileId) { this.folderFileId = folderFileId; }

    public String getParentFileId() { return parentFileId; }
    public void setParentFileId(String parentFileId) { this.parentFileId = parentFileId; }

    public String getFolderName() { return folderName; }
    public void setFolderName(String folderName) { this.folderName = folderName; }

    public String getFullPath() { return fullPath; }
    public void setFullPath(String fullPath) { this.fullPath = fullPath; }

    public byte[] getFullPathHash() { return fullPathHash; }
    public void setFullPathHash(byte[] fullPathHash) { this.fullPathHash = fullPathHash; }

    public Integer getDepth() { return depth; }
    public void setDepth(Integer depth) { this.depth = depth; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}

