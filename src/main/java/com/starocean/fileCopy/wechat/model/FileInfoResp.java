package com.starocean.fileCopy.wechat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileInfoResp extends WechatBaseResp {

    @JsonProperty("file_info")
    private WeDriveFileItem fileInfo;

    public WeDriveFileItem getFileInfo() { return fileInfo; }
    public void setFileInfo(WeDriveFileItem fileInfo) { this.fileInfo = fileInfo; }
}

