package com.starocean.fileCopy.wechat.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class FileRenameResp extends WechatBaseResp {

    @JsonProperty("file")
    private WeDriveFileItem file;

    public WeDriveFileItem getFile() { return file; }
    public void setFile(WeDriveFileItem file) { this.file = file; }
}

