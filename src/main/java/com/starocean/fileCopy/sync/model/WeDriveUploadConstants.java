package com.starocean.fileCopy.sync.model;

public final class WeDriveUploadConstants {
    private WeDriveUploadConstants() {}

    public static final long DIRECT_UPLOAD_THRESHOLD = 10L * 1024 * 1024; // 10MB
    public static final int PART_SIZE = 2 * 1024 * 1024; // 2MB
}
