package com.starocean.fileCopy.sync.service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Component;

import com.starocean.fileCopy.Util.WeDrivePathUtil;
import com.starocean.fileCopy.config.DownloadLocalProperties;

@Component
public class TempFileService {

    private final DownloadLocalProperties props;

    public TempFileService(DownloadLocalProperties props) {
        this.props = props;
    }

    public Path resolveTempPath(String relativePath) {
        String norm = WeDrivePathUtil.normalizeRelativePathContainFile(relativePath);
        Path baseDir = Paths.get(props.getTempDir()).toAbsolutePath().normalize();
        return baseDir.resolve(norm);
    }

    public void ensureBaseDir() throws IOException {
        Path baseDir = Paths.get(props.getTempDir()).toAbsolutePath().normalize();
        Files.createDirectories(baseDir);
    }

    public void deleteQuietly(Path p) {
        try {
            if (p != null) Files.deleteIfExists(p);
        } catch (Exception ignore) {}
    }
}

