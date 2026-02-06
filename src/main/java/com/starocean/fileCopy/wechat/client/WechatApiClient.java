package com.starocean.fileCopy.wechat.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.starocean.fileCopy.remote.RemoteApiClient;
import com.starocean.fileCopy.wechat.api.WechatApiDef;
import com.starocean.fileCopy.wechat.service.WechatTokenService;

import reactor.core.publisher.Mono;

@Component
public class WechatApiClient {

	private final RemoteApiClient remoteApiClient;
    private final WechatTokenService tokenService;

    public WechatApiClient(RemoteApiClient remoteApiClient, WechatTokenService tokenService) {
        this.remoteApiClient = remoteApiClient;
        this.tokenService = tokenService;
    }

    public <T> Mono<T> call(WechatApiDef api,
                            Map<String, ?> query,
                            Map<String, String> headers,
                            Object body,
                            Class<T> respType) {

        Map<String, Object> q = new HashMap<>();
        if (query != null) q.putAll(query);

        if (api.needAccessToken()) {
            q.put("access_token", tokenService.getAccessToken());
        }

        return remoteApiClient.call(api.method(), api.path(), q, headers, body, respType, null, null);
    }
}
