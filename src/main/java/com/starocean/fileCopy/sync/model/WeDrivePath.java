package com.starocean.fileCopy.sync.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WeDrivePath {
    private final List<String> dirs;
    private final String fileName;

    public WeDrivePath(List<String> dirs, String fileName) {
        this.dirs = dirs;
        this.fileName = fileName;
    }

    public List<String> getDirs() { return dirs; }
    public String getFileName() { return fileName; }

    public static WeDrivePath parseOnlyPathWithoutFile(String relativePath) {
        String p = relativePath == null ? "" : relativePath.replace("\\", "/");
        while (p.startsWith("/")) p = p.substring(1);

        String[] parts = p.split("/");
        if (parts.length <= 1) {
            return new WeDrivePath(List.of(), p);
        }
        List<String> dirs = new ArrayList<>(Arrays.asList(parts).subList(0, parts.length - 1));
        String name = parts[parts.length - 1];
        return new WeDrivePath(dirs, name);
    }
}
