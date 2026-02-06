package com.starocean.fileCopy.wechat.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WechatTokenResp extends WechatBaseResp {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private int expiresIn; // seconds

    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }

    public int getExpiresIn() { return expiresIn; }
    public void setExpiresIn(int expiresIn) { this.expiresIn = expiresIn; }

    @Override
    public boolean isSuccess() {
        return super.isSuccess() && accessToken != null && !accessToken.isBlank();
    }
}

