package com.starocean.fileCopy.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.HttpClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;

@Configuration
public class WebClientConfig {

    @Bean
    @Qualifier("apiWebClient")
    WebClient apiWebClient(WechatCloudProperties props) {
        String baseUrl = "https://qyapi.weixin.qq.com";

        // JSON API 一般不需要太大内存，给个 2MB 足够
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(c -> c.defaultCodecs().maxInMemorySize(2 * 1024 * 1024))
                .build();

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(props.getService().getApiTimeoutSeconds()));

        WebClient.Builder builder = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(strategies);

//        if (StringUtils.hasText(props.getService().getApiTimeoutSeconds())) {
//            builder.defaultHeaders(h -> h.setBasicAuth(props.getServer().getUsername(), props.getServer().getPassword()));
//        }

        return builder.build();
    }

    @Bean
    @Qualifier("downloadWebClient")
    WebClient downloadWebClient(DownloadTargetProperties props) {
        String baseUrl = "http://" + props.getServer().getIp() + ":" + props.getServer().getPort();

        int chunkBytes = props.getChunkSizeMb() * 1024 * 1024;
        int maxInMemory = Math.max(chunkBytes, 2 * 1024 * 1024);

        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(c -> c.defaultCodecs().maxInMemorySize(maxInMemory))
                .build();

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(props.getDownloadTimeoutSeconds()));

        WebClient.Builder builder = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(strategies);

        if (StringUtils.hasText(props.getServer().getUsername())) {
            builder.defaultHeaders(h -> h.setBasicAuth(props.getServer().getUsername(), props.getServer().getPassword()));
        }

        return builder.build();
    }
}

