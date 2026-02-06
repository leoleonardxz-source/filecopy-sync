package com.starocean.fileCopy.remote;

import org.springframework.http.HttpMethod;

public interface RemoteApiDef {
    HttpMethod method();
    String path();
    String desc();
}
