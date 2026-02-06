package com.starocean.fileCopy.db.mysql.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starocean.fileCopy.db.mysql.entity.WedriveFileMapping;

/**
 * Mapper for wedrive_file_mapping table.
 * Uses MyBatis annotations (no XML). Extends MyBatis-Plus BaseMapper for basic CRUD.
 */
@Mapper
public interface WedriveFileMappingMapper extends BaseMapper<WedriveFileMapping> {

    @Select("SELECT * FROM wedrive_file_mapping WHERE source_file_id = #{sourceFileId} LIMIT 1")
    WedriveFileMapping selectBySourceFileId(@Param("sourceFileId") String sourceFileId);

    @Select("SELECT * FROM wedrive_file_mapping WHERE space_id = #{spaceId} ORDER BY id")
    List<WedriveFileMapping> selectBySpaceId(@Param("spaceId") String spaceId);

    @Select("SELECT id FROM wedrive_file_mapping ORDER BY id DESC LIMIT 1")
    Long selectMaxId();

    @Insert("""
            INSERT INTO wedrive_file_mapping (
                space_id, source_file_id, wedrive_file_id, relative_path, relative_path_hash, folder_full_path, folder_full_path_hash, file_name, source_effective_time, file_size, status, last_sync_time, retry_count, last_error, created_at, updated_at
            ) VALUES (
                #{m.spaceId}, #{m.sourceFileId}, #{m.wedriveFileId}, #{m.relativePath}, #{m.relativePathHash}, #{m.folderFullPath}, #{m.folderFullPathHash}, #{m.fileName}, #{m.sourceEffectiveTime}, #{m.fileSize}, #{m.status}, #{m.lastSyncTime}, #{m.retryCount}, #{m.lastError}, #{m.createdAt}, #{m.updatedAt}
            )
            """)
    int insertMapping(@Param("m") WedriveFileMapping m);
}
