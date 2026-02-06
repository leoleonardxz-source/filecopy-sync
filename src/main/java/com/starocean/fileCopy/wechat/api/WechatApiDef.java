package com.starocean.fileCopy.wechat.api;

import com.starocean.fileCopy.remote.RemoteApiDef;

public interface WechatApiDef extends RemoteApiDef {
    boolean needAccessToken();
}
