package com.starocean.fileCopy.Util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class WeDrivePathUtil {

    private WeDrivePathUtil() {}

    /**
     * 不含文件名<br >
     * 标准化“目录 full_path”：
     * - \\ -> /
     * - 合并重复 /
     * - 去掉首尾 /
     * - 根目录返回 ""
     */
    public static String normalizeFullPathWithoutFile(String path) {
        if (path == null) return "";
        String s = path.trim().replace("\\", "/");
        s = s.replaceAll("/+", "/"); // 合并重复 /
        while (s.startsWith("/")) s = s.substring(1);
        while (s.endsWith("/")) s = s.substring(0, s.length() - 1);
        String[] parts = s.split("/");
        if (parts.length <= 1) {
        	//  /test.pdf
            return "";// 根目录
        } else {
        	// /Doc/2026/270745694d54406b9c7699c41afcf819.pdf
        	List<String> dirs = new ArrayList<>(Arrays.asList(parts).subList(0, parts.length - 1));
        	return String.join("/", dirs);
        }
    }
    
    /**
     * only get file name from path
     * @param path
     * @return
     */
    public static String getFileNameByPath(String path) {
        if (path == null) return "";
        String s = path.trim().replace("\\", "/");
        s = s.replaceAll("/+", "/"); // 合并重复 /
        while (s.startsWith("/")) s = s.substring(1);
        while (s.endsWith("/")) s = s.substring(0, s.length() - 1);
        String[] parts = s.split("/");
        return parts[parts.length - 1];
    }

    /**
     * 含文件名<br >
     * 标准化“相对路径 relative_path”
     * - \\ -> /
     * - 合并重复 /
     * - 去掉开头 /
     * - 不强制去掉结尾 /（一般文件路径不会以/结尾）
     */
    public static String normalizeRelativePathContainFile(String relativePath) {
        if (relativePath == null) return "";
        String s = relativePath.trim().replace("\\", "/");
        s = s.replaceAll("/+", "/");
        while (s.startsWith("/")) s = s.substring(1);
        return s;
    }

    /**
     * SHA-256(spaceId + ":" + normalizedPath) -> 32 bytes，写入 BINARY(32)
     */
    public static byte[] sha256Path(String spaceId, String normalizedPath) {
        try {
            String key = (spaceId == null ? "" : spaceId) + ":" + (normalizedPath == null ? "" : normalizedPath);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(key.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}

