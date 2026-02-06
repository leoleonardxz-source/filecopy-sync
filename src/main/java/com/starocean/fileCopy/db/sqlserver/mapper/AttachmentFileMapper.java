package com.starocean.fileCopy.db.sqlserver.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.starocean.fileCopy.db.sqlserver.dto.AttachmentFileDTO;

@Mapper
public interface AttachmentFileMapper {


	@Select("""
			WITH batch AS (
			    SELECT TOP (#{batchSize})
			        a.FID               AS fid,
			        a.FFILEID           AS file_id,
			        a.FBILLTYPE         AS bill_type,
			        a.FATTACHMENTNAME   AS file_ori_name,
			        a.FCREATETIME       AS create_time,
			        a.FMODIFYTIME       AS modify_time,
			        COALESCE(a.FMODIFYTIME, a.FCREATETIME) AS effective_time,
			        b.FNUMBER           AS file_number,
			        c.FLOCATIONPATH     AS location_path,
			        c.FRELATIVEPATH     AS relative_path,
			        c.FFILESIZE         AS file_size
			    FROM dbo.T_BAS_ATTACHMENT a
			    INNER JOIN dbo.T_BD_MATERIAL b ON a.FINTERID = b.FMATERIALID
			    INNER JOIN dbo.T_BAS_FILESERVERFILEINFO c ON a.FFILEID = c.FFILEID
			    WHERE a.FBILLTYPE = 'BD_MATERIAL'
			      AND a.FID <> 186588
			      AND a.FID > #{lastFid}
			      AND COALESCE(a.FMODIFYTIME, a.FCREATETIME)
			            >= DATEADD(
			                   HOUR,
			                   -24,
			                   DATEADD(HOUR, DATEDIFF(HOUR, 0, #{lastSyncTime}), 0)
			               )
			    ORDER BY a.FID ASC
			)
			SELECT
			    batch.*,
			    max_time.batch_max_effective_time
			FROM batch
			CROSS APPLY (
			    SELECT MAX(effective_time) AS batch_max_effective_time
			    FROM batch
			) max_time
			ORDER BY batch.fid ASC
			""")
	List<AttachmentFileDTO> selectIncrementalBatch(
			@Param("lastSyncTime") LocalDateTime lastSyncTime,
			@Param("lastFid") long lastFid,
			@Param("batchSize") int batchSize
			);

}

