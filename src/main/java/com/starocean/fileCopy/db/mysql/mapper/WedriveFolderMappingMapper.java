package com.starocean.fileCopy.db.mysql.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starocean.fileCopy.db.mysql.entity.WedriveFolderMapping;

/**
 * MyBatis mapper for wedrive_folder_mapping table.
 * Uses annotations (no XML).
 */
@Mapper
public interface WedriveFolderMappingMapper extends BaseMapper<WedriveFolderMapping> {

    @Select("SELECT * FROM wedrive_folder_mapping WHERE folder_file_id = #{folderFileId} LIMIT 1")
    WedriveFolderMapping selectByFolderFileId(@Param("folderFileId") String folderFileId);

    @Select("SELECT * FROM wedrive_folder_mapping WHERE space_id = #{spaceId} ORDER BY id")
    List<WedriveFolderMapping> selectBySpaceId(@Param("spaceId") String spaceId);

    @Select("SELECT id FROM wedrive_folder_mapping ORDER BY id DESC LIMIT 1")
    Long selectMaxId();

    @Insert("""
            INSERT INTO wedrive_folder_mapping (
                space_id, folder_file_id, parent_file_id, folder_name, full_path, full_path_hash, depth, status, created_at, updated_at
            ) VALUES (
                #{m.spaceId}, #{m.folderFileId}, #{m.parentFileId}, #{m.folderName}, #{m.fullPath}, #{m.fullPathHash}, #{m.depth}, #{m.status}, #{m.createdAt}, #{m.updatedAt}
            )
            """)
    int insertMapping(@Param("m") WedriveFolderMapping m);
}
