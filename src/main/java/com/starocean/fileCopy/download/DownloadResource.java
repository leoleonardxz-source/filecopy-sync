package com.starocean.fileCopy.download;


public class DownloadResource {

    /**
     * nginx 下的相对路径
     * 如：442227/aaa.txt
     */
    private final String relativePath;

    /**
     * 本地保存的相对路径（或完整路径）
     */
    private final String localPath;

    public DownloadResource(String relativePath, String localPath) {
        this.relativePath = normalize(relativePath);
        this.localPath = localPath;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getLocalPath() {
        return localPath;
    }

    private String normalize(String path) {
        if (path == null) return "";
        String p = path.replace("\\", "/");
        while (p.startsWith("/")) {
            p = p.substring(1);
        }
        return p;
    }
}

