package com.starocean.fileCopy.db.sqlserver.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AttachmentFileDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2801021791766065679L;

	/**
	 * 对应 SQL select 出来的结果集（非表实体，属于 DTO/VO 更合适）
	 */

	
	private Long fid;

	/** a.FFILEID as file_id */
	private String fileId;

	/** a.FBILLTYPE as bill_type */
	private String billType;

	/** a.FATTACHMENTNAME as file_ori_name */
	private String fileOriName;

	/** a.FCREATETIME as create_time */
	private LocalDateTime createTime;

	/** a.FMODIFYTIME as modify_time */
	private LocalDateTime modifyTime;

	/** b.FNUMBER as file_number */
	private String fileNumber;

	/** c.FLOCATIONPATH as location_path */
	private String locationPath;

	/** c.FRELATIVEPATH as relative_path */
	private String relativePath;

	/** c.FFILESIZE as file_size */
	private Long fileSize;
	
	private LocalDateTime effectiveTime;
	
	/**
	 * 本次job，查询到的新的一条时间，create_time/modify_time
	 */
	private LocalDateTime batchMaxEffectiveTime;
	

	public Long getFid() {
		return fid;
	}

	public void setFid(Long fid) {
		this.fid = fid;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getFileOriName() {
		return fileOriName;
	}

	public void setFileOriName(String fileOriName) {
		this.fileOriName = fileOriName;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(LocalDateTime modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getFileNumber() {
		return fileNumber;
	}

	public void setFileNumber(String fileNumber) {
		this.fileNumber = fileNumber;
	}

	public String getLocationPath() {
		return locationPath;
	}

	public void setLocationPath(String locationPath) {
		this.locationPath = locationPath;
	}

	public String getRelativePath() {
		return relativePath;
	}

	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	
	

	public LocalDateTime getEffectiveTime() {
		return effectiveTime;
	}

	public void setEffectiveTime(LocalDateTime effectiveTime) {
		this.effectiveTime = effectiveTime;
	}

	public LocalDateTime getBatchMaxEffectiveTime() {
		return batchMaxEffectiveTime;
	}

	public void setBatchMaxEffectiveTime(LocalDateTime batchMaxEffectiveTime) {
		this.batchMaxEffectiveTime = batchMaxEffectiveTime;
	}

	@Override
	public String toString() {
		return "AttachmentFileDTO{" +
				"fid='" + fid + '\'' +
				"fileId='" + fileId + '\'' +
				", billType='" + billType + '\'' +
				", fileOriName='" + fileOriName + '\'' +
				", createTime=" + createTime +
				", modifyTime=" + modifyTime +
				", fileNumber='" + fileNumber + '\'' +
				", locationPath='" + locationPath + '\'' +
				", relativePath='" + relativePath + '\'' +
				", fileSize=" + fileSize +
				'}';
	}

}
