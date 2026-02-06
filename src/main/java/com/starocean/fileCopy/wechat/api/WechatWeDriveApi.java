package com.starocean.fileCopy.wechat.api;

import org.springframework.http.HttpMethod;

public enum WechatWeDriveApi implements WechatApiDef {

	/**
	 * 暂时用不到，手动在postman里面调用，创建好默认的空间，把对应的space_id记录下来，保存在application.properties里面。
	 */
    SPACE_CREATE(HttpMethod.POST, "/cgi-bin/wedrive/space_create", true, "新建空间"),
    SPACE_INFO(HttpMethod.POST, "/cgi-bin/wedrive/space_info", true, "获取空间信息"),

    FILE_LIST(HttpMethod.POST, "/cgi-bin/wedrive/file_list", true, "获取文件列表"),

    FILE_UPLOAD(HttpMethod.POST, "/cgi-bin/wedrive/file_upload", true, "上传文件"),
    FILE_UPLOAD_INIT(HttpMethod.POST, "/cgi-bin/wedrive/file_upload_init", true, "分块上传初始化"),
    FILE_UPLOAD_PART(HttpMethod.POST, "/cgi-bin/wedrive/file_upload_part", true, "分块上传文件"),
    WEDRIVE_FILE_UPLOAD_FINISH(HttpMethod.POST, "/cgi-bin/wedrive/file_upload_finish", true, "分块上传完成"),

    FILE_CREATE(HttpMethod.POST, "/cgi-bin/wedrive/file_create", true, "新建文件夹/文档"),
    FILE_RENAME(HttpMethod.POST, "/cgi-bin/wedrive/file_rename", true, "重命名文件"),
    FILE_MOVE(HttpMethod.POST, "/cgi-bin/wedrive/file_move", true, "移动文件"),
    FILE_DELETE(HttpMethod.POST, "/cgi-bin/wedrive/file_delete", true, "删除文件"),
    FILE_INFO(HttpMethod.POST, "/cgi-bin/wedrive/file_info", true, "获取文件信息"),
    MESSAGE_SEND(HttpMethod.POST, "/cgi-bin/message/send", true, "给用户发送消息")
    ;

    private final HttpMethod method;
    private final String path;
    private final boolean needAccessToken;
    private final String desc;

    WechatWeDriveApi(HttpMethod method, String path, boolean needAccessToken, String desc) {
        this.method = method;
        this.path = path;
        this.needAccessToken = needAccessToken;
        this.desc = desc;
    }

    @Override
    public HttpMethod method() {
        return method;
    }

    @Override
    public String path() {
        return path;
    }

    @Override
    public boolean needAccessToken() {
        return needAccessToken;
    }

    @Override
    public String desc() {
        return desc;
    }
}
