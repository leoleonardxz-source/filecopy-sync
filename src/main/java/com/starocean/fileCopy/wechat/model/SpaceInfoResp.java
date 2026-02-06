package com.starocean.fileCopy.wechat.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpaceInfoResp extends WechatBaseResp {

    @JsonProperty("space_info")
    private SpaceInfo spaceInfo;

    public SpaceInfo getSpaceInfo() { return spaceInfo; }
    public void setSpaceInfo(SpaceInfo spaceInfo) { this.spaceInfo = spaceInfo; }

    public static class SpaceInfo {
        @JsonProperty("spaceid")
        private String spaceId;

        @JsonProperty("space_name")
        private String spaceName;

        @JsonProperty("auth_list")
        private AuthList authList;

        @JsonProperty("space_sub_type")
        private Integer spaceSubType;

        public String getSpaceId() { return spaceId; }
        public void setSpaceId(String spaceId) { this.spaceId = spaceId; }

        public String getSpaceName() { return spaceName; }
        public void setSpaceName(String spaceName) { this.spaceName = spaceName; }

        public AuthList getAuthList() { return authList; }
        public void setAuthList(AuthList authList) { this.authList = authList; }

        public Integer getSpaceSubType() { return spaceSubType; }
        public void setSpaceSubType(Integer spaceSubType) { this.spaceSubType = spaceSubType; }
    }

    public static class AuthList {
        @JsonProperty("auth_info")
        private List<AuthInfo> authInfo;

        @JsonProperty("quit_userid")
        private List<String> quitUserId;

        public List<AuthInfo> getAuthInfo() { return authInfo; }
        public void setAuthInfo(List<AuthInfo> authInfo) { this.authInfo = authInfo; }

        public List<String> getQuitUserId() { return quitUserId; }
        public void setQuitUserId(List<String> quitUserId) { this.quitUserId = quitUserId; }
    }

    public static class AuthInfo {
        @JsonProperty("type")
        private Integer type;

        @JsonProperty("userid")
        private String userId;

        @JsonProperty("departmentid")
        private Integer departmentId;

        @JsonProperty("auth")
        private Integer auth;

        public Integer getType() { return type; }
        public void setType(Integer type) { this.type = type; }

        public String getUserId() { return userId; }
        public void setUserId(String userId) { this.userId = userId; }

        public Integer getDepartmentId() { return departmentId; }
        public void setDepartmentId(Integer departmentId) { this.departmentId = departmentId; }

        public Integer getAuth() { return auth; }
        public void setAuth(Integer auth) { this.auth = auth; }
    }
}

