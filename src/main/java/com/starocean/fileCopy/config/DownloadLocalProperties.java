package com.starocean.fileCopy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "download.local")
public class DownloadLocalProperties {

    /**
     * 本地临时下载目录
     * 示例：/tmp/fileCopy/downloads
     */
    private String tempDir;

    public String getTempDir() {
        return tempDir;
    }

    public void setTempDir(String tempDir) {
        this.tempDir = tempDir;
    }
}

