package com.starocean.fileCopy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wechat.cloud")
public class WechatCloudProperties {
	
	private int downloadTimeoutSeconds = 120;

    private Company company = new Company();
    private Service service = new Service();

    // ===== company =====
    public static class Company {
        private String id;
        private String softwareUserId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

		public String getSoftwareUserId() {
			return softwareUserId;
		}

		public void setSoftwareUserId(String softwareUserId) {
			this.softwareUserId = softwareUserId;
		}
        
    }

    // ===== service =====
    public static class Service {
        private String agentId;
        private String spaceId;
        private String rootFolderId;
        private String secret;
        private Integer apiTimeoutSeconds;

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

		public Integer getApiTimeoutSeconds() {
			return apiTimeoutSeconds;
		}

		public void setApiTimeoutSeconds(Integer apiTimeoutSeconds) {
			this.apiTimeoutSeconds = apiTimeoutSeconds;
		}

		public String getSpaceId() {
			return spaceId;
		}

		public void setSpaceId(String spaceId) {
			this.spaceId = spaceId;
		}

		public String getRootFolderId() {
			return rootFolderId;
		}

		public void setRootFolderId(String rootFolderId) {
			this.rootFolderId = rootFolderId;
		}
        
    }

    // ===== getters / setters =====
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

	public int getDownloadTimeoutSeconds() {
		return downloadTimeoutSeconds;
	}

	public void setDownloadTimeoutSeconds(int downloadTimeoutSeconds) {
		this.downloadTimeoutSeconds = downloadTimeoutSeconds;
	}
    
}

