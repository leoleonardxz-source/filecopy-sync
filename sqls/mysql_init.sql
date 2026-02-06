CREATE DATABASE IF NOT EXISTS wp
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

use `wp`;
drop table if exists `file_sync_state`;
drop table if exists `wedrive_folder_mapping`;
drop table if exists `wedrive_file_mapping`;


CREATE TABLE file_sync_state (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '本地自增主键',
    -- ====== 源端唯一标识 ======
    source_file_id VARCHAR(255) NOT NULL COMMENT '源系统文件ID（如 SQLServer 的 FFILEID）',
    -- ====== 路径信息 ======
    relative_path VARCHAR(1024) NOT NULL COMMENT 'nginx 下的相对路径',
    file_name VARCHAR(255) NOT NULL COMMENT '文件名',
    -- ====== 用于增量判断的核心字段 ======
    effective_time DATETIME NOT NULL COMMENT '源端有效时间（COALESCE(modify_time, create_time)）',
    source_file_size BIGINT DEFAULT NULL COMMENT '源端文件大小（字节）',
    -- ====== 本地同步信息 ======
    download_time DATETIME NOT NULL COMMENT '下载完成时间',
    sync_status TINYINT NOT NULL COMMENT '同步状态：0-成功 1-失败',
    -- ====== 维护字段 ======
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
    -- ====== 约束 & 索引 ======
    UNIQUE KEY uk_source_file (source_file_id),
    KEY idx_effective_time (effective_time),
    KEY idx_sync_status (sync_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件同步状态表';


CREATE TABLE wedrive_folder_mapping (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  space_id VARCHAR(64) NOT NULL,
  folder_file_id VARCHAR(128) NOT NULL,     -- 微盘目录的 file_id
  parent_file_id VARCHAR(128) NULL,         -- 根目录可为空（或 ROOT_FOLDER_ID）
  folder_name VARCHAR(255) NOT NULL,        -- 当前目录名（最后一级）
  full_path VARCHAR(1024) NOT NULL,         -- 标准化路径：a/b/c（不含首尾/）
  -- hash 唯一键：SHA-256 = 32 bytes
  full_path_hash BINARY(32) NOT NULL,
  depth INT NOT NULL DEFAULT 0,
  status TINYINT NOT NULL DEFAULT 1,        -- 1=有效 0=失效(被删除/冲突等)
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_space_pathhash (space_id, full_path_hash),
  UNIQUE KEY uk_space_parent_name (space_id, parent_file_id, folder_name),
  KEY idx_space_parent (space_id, parent_file_id),
  KEY idx_space_folderid (space_id, folder_file_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE wedrive_file_mapping (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  space_id VARCHAR(64) NOT NULL,
  source_file_id VARCHAR(64) NOT NULL,          -- SQLServer 的 FFILEID 或你的 file_key
  wedrive_file_id VARCHAR(128) NOT NULL,        -- 微盘文件 file_id
  relative_path VARCHAR(1024) NOT NULL,         -- 源相对路径（含文件名，可能更长）
  relative_path_hash BINARY(32) NOT NULL,       -- SHA-256(spaceId + ":" + relativePath)
  folder_full_path VARCHAR(1024) NOT NULL,      -- 文件所在目录 full_path（不含文件名）
  folder_full_path_hash BINARY(32) NOT NULL,    -- SHA-256(spaceId + ":" + folderFullPath)
  file_name VARCHAR(255) NOT NULL,
  source_effective_time DATETIME NULL,          -- 你 SQL 里算的 effective_time（增量判断）
  file_size BIGINT NULL,
  status TINYINT NOT NULL DEFAULT 1,            -- 1=已同步 2=失败待重试 3=已删除/失效
  last_sync_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  retry_count INT NOT NULL DEFAULT 0,
  last_error VARCHAR(1000) NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  UNIQUE KEY uk_space_relhash (space_id, relative_path_hash),
  KEY uk_space_source (space_id, source_file_id),
  KEY idx_space_effective (space_id, source_effective_time),
  KEY idx_space_folderhash (space_id, folder_full_path_hash),
  KEY idx_space_wedrive_file (space_id, wedrive_file_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

























