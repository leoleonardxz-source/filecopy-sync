package com.starocean.fileCopy.wechat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileCreateResp extends WechatBaseResp {

    @JsonProperty("fileid")
    private String fileId;

    @JsonProperty("url")
    private String url;

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
