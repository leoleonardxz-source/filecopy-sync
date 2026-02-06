package com.starocean.fileCopy.sync.service;

import java.nio.file.Path;

import org.springframework.stereotype.Service;

import com.starocean.fileCopy.Util.DownloadUtil;


@Service
public class DownloadService {

    private final DownloadUtil downloadUtil;

    public DownloadService(DownloadUtil downloadUtil) {
        this.downloadUtil = downloadUtil;
    }

    public void download(String relativePath, Path localPath) throws Exception {
        downloadUtil.download(relativePath, localPath);
    }
}
