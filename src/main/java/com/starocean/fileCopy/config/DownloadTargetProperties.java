package com.starocean.fileCopy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "download.target.nginx")
public class DownloadTargetProperties {

    private Server server = new Server();

    private int downloadTimeoutSeconds = 120;
    private int largeThresholdMb = 50;
    private int chunkSizeMb = 5;
    private boolean resume = true;

    public static class Server {
        private String ip;
        private int port;
        private String username;
        private String password;

        // getters / setters
        public String getIp() { return ip; }
        public void setIp(String ip) { this.ip = ip; }

        public int getPort() { return port; }
        public void setPort(int port) { this.port = port; }

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // getters / setters
    public Server getServer() { return server; }
    public void setServer(Server server) { this.server = server; }

    public int getDownloadTimeoutSeconds() {
		return downloadTimeoutSeconds;
	}
	public void setDownloadTimeoutSeconds(int downloadTimeoutSeconds) {
		this.downloadTimeoutSeconds = downloadTimeoutSeconds;
	}
    public int getLargeThresholdMb() { return largeThresholdMb; }
    public void setLargeThresholdMb(int largeThresholdMb) { this.largeThresholdMb = largeThresholdMb; }

    public int getChunkSizeMb() { return chunkSizeMb; }
    public void setChunkSizeMb(int chunkSizeMb) { this.chunkSizeMb = chunkSizeMb; }

    public boolean isResume() { return resume; }
    public void setResume(boolean resume) { this.resume = resume; }
}
