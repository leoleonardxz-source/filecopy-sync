package com.starocean.fileCopy.Util;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

public final class WeDriveBlockShaUtil {

    private WeDriveBlockShaUtil() {}

    public static final int PART_SIZE = 2 * 1024 * 1024; // 2MB = 2097152

    /**
     * 按官方描述计算 block_sha（累积 SHA-1 的 hex 列表）
     *
     * @return 按分块顺序的 cumulative sha1 hex（每个 40 字符，小写）
     */
    public static List<String> calcBlockSha(Path file) throws Exception {
        List<String> blockSha = new ArrayList<>();

        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");

        try (InputStream in = Files.newInputStream(file)) {
            byte[] buf = new byte[PART_SIZE];
            int read;

            while ((read = in.read(buf)) != -1) {
                sha1.update(buf, 0, read);

                // 关键：拿到“当前 state”的累积 sha1
                // 用 clone 快照，再对快照 digest() 得到截至当前块的 sha1
                MessageDigest snapshot = (MessageDigest) sha1.clone();
                byte[] current = snapshot.digest();

                blockSha.add(bytesToHexLower(current));
            }
        }

        return blockSha;
    }

    private static String bytesToHexLower(byte[] bytes) {
        char[] hex = "0123456789abcdef".toCharArray();
        char[] out = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            out[i * 2] = hex[v >>> 4];
            out[i * 2 + 1] = hex[v & 0x0F];
        }
        return new String(out);
    }
}


