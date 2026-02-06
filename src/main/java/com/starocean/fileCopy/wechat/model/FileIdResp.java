package com.starocean.fileCopy.wechat.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class FileIdResp extends WechatBaseResp {

    @JsonProperty("fileid")
    private String fileId;

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }
}

