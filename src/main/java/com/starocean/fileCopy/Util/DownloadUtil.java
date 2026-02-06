package com.starocean.fileCopy.Util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;

@Component
public class DownloadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DownloadUtil.class);

    private final WebClient downloadWebClient;

    public DownloadUtil(WebClient downloadWebClient) {
        this.downloadWebClient = downloadWebClient;
    }

    /**
     * 从 nginx 下载文件到本地
     *
     * @param relativePath nginx 下的相对路径，例如：442227/payment/2025/02/a.pdf
     * @param localPath    本地保存路径，例如：D:/temp_download/442227/payment/2025/02/a.pdf
     */
    public void download(String relativePath, Path localPath) {
        if (!StringUtils.hasText(relativePath)) {
            throw new IllegalArgumentException("relativePath is blank");
        }
        if (localPath == null) {
            throw new IllegalArgumentException("localPath is null");
        }

        try {
            // 1. 创建父目录
            Path parentDir = localPath.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            // 2. 发起下载请求（流式）
            Flux<DataBuffer> dataBufferFlux = downloadWebClient
                    .get()
                    .uri(uriBuilder -> uriBuilder.path("/" + relativePath).build())
                    .retrieve()
                    .bodyToFlux(DataBuffer.class);

            // 3. 写入本地文件（覆盖写）
            DataBufferUtils.write(
                    dataBufferFlux,
                    localPath,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            ).block();

            LOGGER.info("Download success: {} -> {}", relativePath, localPath.toAbsolutePath());

        } catch (Exception e) {
            LOGGER.error("Download failed: {} -> {}", relativePath, localPath, e);
            throw new RuntimeException("Download failed: " + relativePath, e);
        }
    }
}


