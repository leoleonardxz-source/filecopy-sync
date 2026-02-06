package com.starocean.fileCopy.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.starocean.fileCopy.config.WechatCloudProperties;
import com.starocean.fileCopy.wechat.api.WechatWeDriveApi;
import com.starocean.fileCopy.wechat.client.WechatApiClient;
import com.starocean.fileCopy.wechat.model.WechatBaseResp;

@Component
public class ExceptionCenter {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCenter.class);
	private final WechatApiClient wechatApiClient;
	private final WechatCloudProperties cloudProps;
	
	public ExceptionCenter(WechatApiClient wechatApiClient,WechatCloudProperties cloudProps) {
		this.wechatApiClient = wechatApiClient;
		this.cloudProps = cloudProps;
	}

	public void handle(String source, Throwable t, String context) {
		
		/**
		 * {
			   "touser" : "LiuWenGuang",
			   "msgtype" : "text",
			   "agentid" : 1000013,
			   "text" : {
			       "content" : "你的快递已到，请携带工卡前往邮件中心领取。\n出发前可查看<a href=\"https://work.weixin.qq.com\">邮件中心视频实况</a>，聪明避开排队。"
			   }
			}
		 */
		//获取堆栈前30行
		String stackTop = stackTop(t, 30);
		//记录日志
		LOGGER.error("EXCEPTION [{}] time={} context={}\n{}",
				source, Instant.now(), context,stackTop );
		
		//发送微信消息
		StringBuilder msgBuilder = new StringBuilder();
		msgBuilder.append("fileCopy EXCEPTION\n");
		msgBuilder.append(stackTop);
		Map<String, Object> body = Map.of(
                "touser", cloudProps.getCompany().getSoftwareUserId(),
                "msgtype", "text",
                "agentid", cloudProps.getService().getAgentId(),
                "text", Map.of("content", msgBuilder.toString())
        );
		
		WechatBaseResp block = wechatApiClient.call(
                WechatWeDriveApi.MESSAGE_SEND,
                null,
                null,
                body,
                WechatBaseResp.class
        ).block();
		LOGGER.debug("method [handle] , result is : {}",block.toString());
	}

	private static String stackTop(Throwable t, int lines) {
		StringWriter sw = new StringWriter();
		t.printStackTrace(new PrintWriter(sw));
		return Arrays.stream(sw.toString().split("\n"))
				.limit(lines)
				.collect(Collectors.joining("\n"));
	}
}
