package com.starocean.fileCopy.db.mysql.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starocean.fileCopy.db.mysql.entity.FileSyncState;

@Mapper
public interface FileSyncStateMapper extends BaseMapper<FileSyncState> {

	@Select("SELECT * FROM file_sync_state WHERE source_file_id = #{sourceFileId} order by id desc LIMIT 1")
	FileSyncState selectBySourceFileId(@Param("sourceFileId") String sourceFileId);

	@Select("select id from file_sync_state order by id desc limit 1")
	Long selectMaxId();

	@Select("""
			SELECT id, source_file_id, relative_path, file_name, effective_time, source_file_size, download_time, sync_status, created_at, updated_at
			FROM file_sync_state
			order by id desc
			limit 1
			""")
	FileSyncState selectRecentRecord();

	@Select("""
			<script>
			SELECT *
			FROM file_sync_state
			WHERE source_file_id IN
			<foreach collection="ids" item="id" open="(" separator="," close=")">
			    #{id}
			</foreach>
			</script>
			""")
	List<FileSyncState> selectBySourceFileIds(@Param("ids") List<String> ids);

	/**
	 * upsert：根据 source_file_id 唯一键，插入或更新
	 * sync_status：成功写 0；失败你可以单独走 updateFail(...)
	 */
	@Insert("""
			INSERT INTO file_sync_state (
			    source_file_id,
			    relative_path,
			    file_name,
			    effective_time,
			    source_file_size,
			    download_time,
			    sync_status
			) VALUES (
			    #{s.sourceFileId},
			    #{s.relativePath},
			    #{s.fileName},
			    #{s.effectiveTime},
			    #{s.sourceFileSize},
			    #{s.downloadTime},
			    #{s.syncStatus}
			)
			ON DUPLICATE KEY UPDATE
			    relative_path    = VALUES(relative_path),
			    file_name        = VALUES(file_name),
			    effective_time   = VALUES(effective_time),
			    source_file_size = VALUES(source_file_size),
			    download_time    = VALUES(download_time),
			    sync_status      = VALUES(sync_status)
			""")
	int upsert(@Param("s") FileSyncState s);

	@Update("UPDATE file_sync_state SET sync_status = #{status}, updated_at = NOW() WHERE source_file_id = #{sourceFileId}")
	int updateStatusBySourceFileId(@Param("sourceFileId") String sourceFileId, @Param("status") int status);

}