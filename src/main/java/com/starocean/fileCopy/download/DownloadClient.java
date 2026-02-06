package com.starocean.fileCopy.download;

import java.nio.file.Path;

import org.springframework.stereotype.Component;

import com.starocean.fileCopy.Util.DownloadUtil;

@Component
public class DownloadClient {
	
    private final DownloadUtil downloadUtil;

    public DownloadClient(DownloadUtil downloadUtil) {
        this.downloadUtil = downloadUtil;
    }

    public void download(DownloadResource resource) throws Exception {
        downloadUtil.download(resource.getRelativePath(), Path.of(resource.getLocalPath()));
    }
}
