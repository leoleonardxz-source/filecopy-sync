package com.starocean.fileCopy.Util;

public class PathUtils {
    
	/**
     * 获取文件路径（如果包含文件名则去掉文件名）
     * @param path 文件路径
     * @return 去掉文件名后的路径，如果不包含文件名则返回原路径
     */
    public static String getFilePathWithoutFileName(String path) {
        if (path == null || path.trim().isEmpty()) {
            return "";
        }
        
        path = path.trim();
        
        // 处理常见的目录结尾
        if (path.endsWith("/") || path.endsWith("\\")) {
            return path;
        }
        
        // 分析路径结构
        int lastSeparatorIndex = Math.max(
            path.lastIndexOf('/'),
            path.lastIndexOf('\\')
        );
        
        if (lastSeparatorIndex == -1) {
            // 没有分隔符，可能是单个文件名或目录名
            // 检查是否是文件（有扩展名）
            int lastDotIndex = path.lastIndexOf('.');
            if (lastDotIndex > 0 && lastDotIndex < path.length() - 1) {
                // 有扩展名，可能是文件，返回空（当前目录）
                return "";
            } else {
                // 没有扩展名，可能是目录名，返回原路径
                return path;
            }
        }
        
        // 获取最后一个分隔符后的部分
        String lastPart = path.substring(lastSeparatorIndex + 1);
        
        // 如果最后一部分为空
        if (lastPart.isEmpty()) {
            return path;
        }
        
        // 判断最后一部分是否是文件名
        int lastDotIndex = lastPart.lastIndexOf('.');
        
        if (lastDotIndex > 0 && lastDotIndex < lastPart.length() - 1) {
            // 有扩展名，是文件
            return path.substring(0, lastSeparatorIndex);
        } else {
            // 没有扩展名或扩展名在开头，可能是目录
            return path;
        }
    }
    
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例
        String[] testPaths = {
            "Doc/2026/aaa.pdf",           // 文件路径
            "/data/tmp/docs/2025",        // 目录路径
            "/data/tmp/docs/2025/",       // 目录路径（带斜杠）
            "report.docx",                // 只有文件名
            "docs",                       // 只有目录名
            "docs/",                      // 目录名带斜杠
            "C:\\Users\\test\\file.txt",  // Windows文件路径
            "C:\\Users\\test",            // Windows目录路径
            "C:\\Users\\test\\",          // Windows目录路径带斜杠
            "",                           // 空字符串
            null,                         // null
            "../parent/file.txt",         // 相对路径文件
            "../parent",                  // 相对路径目录
            "./current/file.txt",         // 当前目录文件
            ".",                          // 当前目录
            "..",                         // 父目录
            "file.with.multiple.dots.txt", // 多点文件名
            "hidden.file",                // 隐藏文件
            "noextension",                // 无扩展名
            ".gitignore",                 // 以点开头的文件
            "path/to/.hidden/file.txt",   // 包含隐藏目录
            "path/to/dir.with.dots/doc.pdf" // 目录带点
        };
        
        System.out.println("测试 getParentPath 方法:");
        System.out.println("=".repeat(60));
        
        for (String path : testPaths) {
            String result = getFilePathWithoutFileName(path);
            System.out.printf("%-40s -> %s%n", 
                path == null ? "null" : path, 
                result);
        }
        
        System.out.println("\n特定用例:");
        System.out.println("relativePath = \"Doc/2026/aaa.pdf\"");
        System.out.println("结果: " + getFilePathWithoutFileName("Doc/2026/aaa.pdf"));
        System.out.println("\npath = \"/data/tmp/docs/2025\"");
        System.out.println("结果: " + getFilePathWithoutFileName("/data/tmp/docs/2025"));
    }
}
