package com.starocean.fileCopy.wechat.model;


import com.fasterxml.jackson.annotation.JsonProperty;

public class SpaceCreateResp extends WechatBaseResp {

    @JsonProperty("spaceid")
    private String spaceId;

    public String getSpaceId() { return spaceId; }
    public void setSpaceId(String spaceId) { this.spaceId = spaceId; }
}

