package com.starocean.fileCopy.wechat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeDriveFileItem {

    @JsonProperty("fileid")
    private String fileId;

    @JsonProperty("file_name")
    private String fileName;

    @JsonProperty("spaceid")
    private String spaceId;

    @JsonProperty("fatherid")
    private String fatherId;

    @JsonProperty("file_size")
    private Long fileSize;

    @JsonProperty("ctime")
    private Long ctime;

    @JsonProperty("mtime")
    private Long mtime;

    @JsonProperty("file_type")
    private Integer fileType;

    @JsonProperty("file_status")
    private Integer fileStatus;

    @JsonProperty("sha")
    private String sha;

    @JsonProperty("md5")
    private String md5;

    @JsonProperty("url")
    private String url;

    // getters/setters
    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getSpaceId() { return spaceId; }
    public void setSpaceId(String spaceId) { this.spaceId = spaceId; }

    public String getFatherId() { return fatherId; }
    public void setFatherId(String fatherId) { this.fatherId = fatherId; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public Long getCtime() { return ctime; }
    public void setCtime(Long ctime) { this.ctime = ctime; }

    public Long getMtime() { return mtime; }
    public void setMtime(Long mtime) { this.mtime = mtime; }

    public Integer getFileType() { return fileType; }
    public void setFileType(Integer fileType) { this.fileType = fileType; }

    public Integer getFileStatus() { return fileStatus; }
    public void setFileStatus(Integer fileStatus) { this.fileStatus = fileStatus; }

    public String getSha() { return sha; }
    public void setSha(String sha) { this.sha = sha; }

    public String getMd5() { return md5; }
    public void setMd5(String md5) { this.md5 = md5; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}

