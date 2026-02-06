package com.starocean.fileCopy.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.starocean.fileCopy.exception.ExceptionCenter;
import com.starocean.fileCopy.service.AttachmentFileService;

@Component
public class WeDriveSyncJob{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WeDriveSyncJob.class);
	
    private final AttachmentFileService attachmentFileService;
    private final ExceptionCenter exceptionCenter;
	
	public WeDriveSyncJob(AttachmentFileService service,ExceptionCenter exceptionCenter) {
		this.attachmentFileService = service;
		this.exceptionCenter = exceptionCenter;
	}

	
//	@Scheduled(initialDelay = 15 * 1000, fixedDelay = 10 * 60 * 1000)
//    public void run() {
//        LOGGER.info("FileDownloadJob start");
//        attachmentFileService.runPagingOnce();
//        LOGGER.info("FileDownloadJob end");
//    }
	
	
	/**
	 * 服务启动后，过60秒首次运行。然后每隔1小时运行一次
	 */
	@Scheduled(initialDelay = 60 * 1000,fixedDelay = 1 * 60 * 60 * 1000)
    public void fileSyncJob() {
		LOGGER.info("--- fileSyncJob start...");
		/**
		 * 1. 在本地的mysql中查询同步的记录，找出最新的effective_time
		 * 2. 用该effective_time作为参数，去sqlserver上面查询文件
		 * 3. 查询到的数据，先判断数量多不多，不多的话，将file_id作为where条件，到mysql中查询是否有记录
		 * 4. 数量多的话，分片进行查询，将file_id作为key，放在map中
		 * 5. 进行循环，比如200条数据，每一条从map中尝试拿，拿到进行比较，是否需要下载
		 * 6. 需要下载的文件，下载到临时目录D:/temp_download
		 * 7. 根据文件的path，去mysql数据库查询各级目录，是否已经存在
		 * 8. 如果不存在，则调用创建文件夹的接口，接口返回的id，需要保存在mysql中，并且要记录下parent_id
		 * 9. 处理该文件，判断文件大小，是否需要分片上传，如果需要分片，则按照2M大小，分片上传
		 * 10. 将文件转成base64内容，或者分片的文件转成base64内容，调用上传接口去上传
		 * 11. 上传成功后，接口会返回对应的file_id，需要保存在mysql中,包括文件所在的目录，应该作为
		 * 12. 上传成功后，需要删除临时目录下的文件
		 * 13. 全部处理完成后，需要记录下本次同步的effective_time，作为下次同步的起点
		 * 
		 * 
		 * 
		 */
		try {
			attachmentFileService.syncOnce();
		} catch (Exception e) {
			LOGGER.error("--- [fileSyncJob]--- error:", e);
			exceptionCenter.handle("fileSyncJob", e, "WeDriveSyncJob.fileSyncJob");
			e.printStackTrace();
		}
		LOGGER.info("--- fileSyncJob end!");
    }

}
