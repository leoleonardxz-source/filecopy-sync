package com.starocean.fileCopy.sync.service;


import org.springframework.stereotype.Component;

import com.starocean.fileCopy.db.mysql.entity.WedriveFileMapping;
import com.starocean.fileCopy.db.sqlserver.dto.AttachmentFileDTO;

@Component
public class NeedSyncDecider {

    /**
     * 规则（简化版）：
     * - 本地没有记录：需要同步
     * - 源 effective_time > 本地记录的 source_effective_time：需要同步
     */
    public boolean shouldSync(AttachmentFileDTO row, WedriveFileMapping existing) {
        if (existing == null) return true;
        if (existing.getSourceEffectiveTime() == null) return true;
        if (row.getEffectiveTime() == null) return true; // 保守：不确定就同步

        return row.getEffectiveTime().isAfter(existing.getSourceEffectiveTime());
    }
}

