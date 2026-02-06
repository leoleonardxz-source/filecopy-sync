package com.starocean.fileCopy.sync.model;

import java.util.Map;

public class UploadInitResp {

    private String uploadKey;
    private String fileId;
    private boolean hitRapidUpload;

    
    public String getUploadKey() { return uploadKey; }
	public boolean isHitRapidUpload() { return hitRapidUpload; }
    public String getFileId() {	return fileId;	}
    
	public static UploadInitResp from(Map<String, Object> resp) {
        UploadInitResp r = new UploadInitResp();
        if (resp == null) throw new RuntimeException("upload_init resp is null");

        Object uploadId = resp.get("upload_key");
        if (uploadId != null) r.uploadKey = String.valueOf(uploadId);
        Object fileId = resp.get("file_id");
        if (fileId != null) r.fileId = String.valueOf(fileId);

        // 按实际字段改：例如 "hit" / "exist" / "rapid_upload"
        Object hit = resp.get("hit_exist");
        r.hitRapidUpload = hit != null && ("1".equals(String.valueOf(hit)) || Boolean.TRUE.equals(hit));

        if (!r.hitRapidUpload && (r.uploadKey == null || r.uploadKey.isBlank())) {
            throw new RuntimeException("upload_init no upload_id, resp=" + resp);
        }
        return r;
    }
}
