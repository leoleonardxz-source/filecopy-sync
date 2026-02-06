package com.starocean.fileCopy.test;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;

import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import com.starocean.fileCopy.Util.DownloadUtil;

import reactor.netty.http.client.HttpClient;

/**
 * Simple demo to run DownloadUtil#download from a main method using a Spring context.
 * Usage:
 *   java -cp <classpath> com.starocean.fileCopy.test.DownloadDemo <remoteRelativePath> <localPath>
 * If args are omitted, defaults are used (adjust defaults below).
 */
public class DownloadDemo {

    public static void main(String[] args) throws IOException {

        // ======= 你自己的 nginx 下载地址 =======
        String ip = "127.0.0.1";
        int port = 8081;

        // ======= 如果 nginx 开了 basic auth，这里填账号密码 =======
        String username = "xxxxx";
        String password = "xxxxx";

        // ======= 要下载的相对路径 =======
        String relativePath = "erp.zip";

        // ======= 本地保存路径 =======
        Path localPath = Path.of("/temp/erp.zip");//相对路径

        String baseUrl = "http://" + ip + ":" + port;

        // 适当调大内存限制（你现在 DownloadUtil 是一次性 byte[]，文件大了会 OOM）
        ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(c -> c.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();

        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(120));

        WebClient.Builder builder = WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(strategies);

        if (username != null && !username.isBlank()) {
            builder.defaultHeaders(h -> h.setBasicAuth(username, password));
        }

        WebClient downloadWebClient = builder.build();

        // 直接 new DownloadUtil（构造器里只需要 WebClient）
        DownloadUtil downloadUtil = new DownloadUtil(downloadWebClient);

        System.out.println("Downloading: " + baseUrl + "/" + relativePath);
        downloadUtil.download(relativePath, localPath);
        System.out.println("Done. Saved at: " + localPath.toAbsolutePath());
    }
}
