package com.starocean.fileCopy.wechat.api;

import org.springframework.http.HttpMethod;


/**
 * wechat 基础接口, 仅有一个获取 access_token 接口，
 * 别的上传部分的接口，见 {@link WechatWeDriveApi}
 */
public enum WechatBaseApi implements WechatApiDef {

	// 0) 获取token（不需要 access_token）
    GET_TOKEN(HttpMethod.GET, "/cgi-bin/gettoken", false, "获取 access_token");

    private final HttpMethod method;
    private final String path;
    private final boolean needAccessToken;
    private final String desc;

    WechatBaseApi(HttpMethod method, String path, boolean needAccessToken, String desc) {
        this.method = method;
        this.path = path;
        this.needAccessToken = needAccessToken;
        this.desc = desc;
    }

    @Override
    public HttpMethod method() {
        return method;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public boolean needAccessToken() {
        return needAccessToken;
    }

    @Override
    public String desc() {
        return desc;
    }
}
