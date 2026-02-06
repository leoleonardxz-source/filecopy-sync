package com.starocean.fileCopy.wechat.service;

import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.starocean.fileCopy.config.WechatCloudProperties;
import com.starocean.fileCopy.remote.RemoteApiClient;
import com.starocean.fileCopy.wechat.api.WechatBaseApi;
import com.starocean.fileCopy.wechat.model.WechatTokenResp;

@Service
public class WechatTokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatTokenService.class);
    private static final long REFRESH_BEFORE_EXPIRE_SECONDS = 300;

    private final RemoteApiClient remoteApiClient;
    private final WechatCloudProperties props;

    private volatile String accessToken;
    private volatile long expireAtEpochSecond;

    public WechatTokenService(RemoteApiClient remoteApiClient, WechatCloudProperties props) {
        this.remoteApiClient = remoteApiClient;
        this.props = props;
    }

    public String getAccessToken() {
        if (isTokenValid()) {
            return accessToken;
        }
        synchronized (this) {
            if (isTokenValid()) {
                return accessToken;
            }
            refreshToken();
            return accessToken;
        }
    }

    private boolean isTokenValid() {
        if (accessToken == null || accessToken.isBlank()) return false;
        long now = Instant.now().getEpochSecond();
        return now < (expireAtEpochSecond - REFRESH_BEFORE_EXPIRE_SECONDS);
    }

    private void refreshToken() {
        LOGGER.info("Refreshing WeChat access_token...");

        WechatTokenResp resp = remoteApiClient.call(
                WechatBaseApi.GET_TOKEN,
                Map.of(
                        "corpid", props.getCompany().getId(),
                        "corpsecret", props.getService().getSecret()
                ),
                null,
                null,
                WechatTokenResp.class
        ).block();

        if (resp == null || !resp.isSuccess()) {
            throw new IllegalStateException("Failed to refresh WeChat access_token, resp=" + resp);
        }

        this.accessToken = resp.getAccessToken();
        this.expireAtEpochSecond = Instant.now().getEpochSecond() + resp.getExpiresIn();

        LOGGER.info("WeChat access_token refreshed, expires_in={}s", resp.getExpiresIn());
    }
}