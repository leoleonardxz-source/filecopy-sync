package com.starocean.fileCopy.remote;

import org.springframework.http.HttpMethod;

public enum RemoteApi implements RemoteApiDef {

    HEALTH(HttpMethod.GET, "/actuator/health", "健康检查");

    private final HttpMethod method;
    private final String path;
    private final String desc;

    RemoteApi(HttpMethod method, String path, String desc) {
        this.method = method;
        this.path = path;
        this.desc = desc;
    }

    @Override
    public HttpMethod method() { return method; }

    @Override
    public String path() { return path; }

    @Override
    public String desc() { return desc; }
}
