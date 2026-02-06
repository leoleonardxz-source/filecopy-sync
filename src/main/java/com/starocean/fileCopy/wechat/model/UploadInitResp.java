package com.starocean.fileCopy.wechat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UploadInitResp extends WechatBaseResp {

    @JsonProperty("hit_exist")
    private boolean hitExist;

    @JsonProperty("upload_key")
    private String uploadKey;

    @JsonProperty("fileid")
    private String fileId;

    public boolean isHitExist() { return hitExist; }
    public void setHitExist(boolean hitExist) { this.hitExist = hitExist; }

    public String getUploadKey() { return uploadKey; }
    public void setUploadKey(String uploadKey) { this.uploadKey = uploadKey; }

    public String getFileId() { return fileId; }
    public void setFileId(String fileId) { this.fileId = fileId; }
}

