package com.starocean.fileCopy.wechat.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FileMoveResp extends WechatBaseResp {

    @JsonProperty("file_list")
    private FileList fileList;

    public FileList getFileList() { return fileList; }
    public void setFileList(FileList fileList) { this.fileList = fileList; }

    public List<WeDriveFileItem> items() {
        return fileList == null ? List.of() : (fileList.getItem() == null ? List.of() : fileList.getItem());
    }

    public static class FileList {
        @JsonProperty("item")
        private List<WeDriveFileItem> item;

        public List<WeDriveFileItem> getItem() { return item; }
        public void setItem(List<WeDriveFileItem> item) { this.item = item; }
    }
}
