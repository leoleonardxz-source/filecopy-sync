package com.starocean.fileCopy.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.starocean.fileCopy.Util.DownloadUtil;
import com.starocean.fileCopy.config.WechatCloudProperties;
import com.starocean.fileCopy.db.mysql.mapper.FileSyncStateMapper;
import com.starocean.fileCopy.db.sqlserver.mapper.AttachmentFileMapper;
import com.starocean.fileCopy.service.AttachmentFileService;
import com.starocean.fileCopy.sync.service.DownloadService;
import com.starocean.fileCopy.sync.service.TempFileService;
import com.starocean.fileCopy.sync.service.WeDriveFileMappingService;
import com.starocean.fileCopy.sync.service.WeDriveFolderMappingService;
import com.starocean.fileCopy.sync.service.WeDriveUploadService;

@Service("attachmentFileServiceImpl")
public class AttachmentFileServiceImpl implements AttachmentFileService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentFileServiceImpl.class);

	private static final int BATCH_SIZE = 2000;
	private static final int LOOKBACK_DEFAULT_DAYS = 180;//假如是首次运行，回看180天的数据
	private static final int LOOKBACK_DEFAULT_HOURS = 24;//假如不是首次运行，为了减少出错，再额外回看24小时的数据
	private static final int HANDLE_PER_SIZE_DEFAULT = 200;// 每次批量查询本机mysql中文件同步的记录数量

	private final AttachmentFileMapper attachmentFileMapper;   // SQLServer
	private final FileSyncStateMapper fileSyncStateMapper;     // MySQL
	private final TempFileService tempFileService;				   // 临时文件服务（Spring Bean）
	private final DownloadService downloadService;
	private final WeDriveFolderMappingService folderMappingService;
	private final WeDriveFileMappingService fileMappingService;
	private final WeDriveUploadService uploadService;
	private final WechatCloudProperties cloudProps;

	public AttachmentFileServiceImpl(AttachmentFileMapper attachmentFileMapper,FileSyncStateMapper fileSyncStateMapper,
			DownloadUtil downloadUtil,TempFileService tempFileService,DownloadService downloadService,WeDriveFolderMappingService folderMappingService,WeDriveUploadService uploadService,
			WechatCloudProperties cloudProps,WeDriveFileMappingService fileMappingService) {
		this.attachmentFileMapper = attachmentFileMapper;
		this.fileSyncStateMapper = fileSyncStateMapper;
		//		this.downloadUtil = downloadUtil;
		this.tempFileService = tempFileService;
		this.downloadService = downloadService;
		this.folderMappingService = folderMappingService;
		this.uploadService = uploadService;
		this.cloudProps = cloudProps;
		this.fileMappingService = fileMappingService;
	}


	//	@Transactional(transactionManager = "mysqlTxManager",
	//	        rollbackFor = Exception.class)
	@Override
	public void syncOnce() {
		LOGGER.info("Starting syncOnce for AttachmentFileServiceImpl...");
		//TODO:do your business here
	}

}
