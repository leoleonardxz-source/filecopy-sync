# filecopy-sync

微信企业微盘一个通用的“增量同步”项目：SQLServer(源) -> Nginx下载 -> WeDrive上传(目标) + MySQL落库(状态/映射)。
该项目出现任何异常，会给指定的人发送企业微信消息

## Features
- 双数据源：SQLServer + MySQL
- 增量分页：effective_time + fid
- 幂等：sync_state / mapping 表
- 临时文件复用：上传失败不删
- WeDrive：小文件直传 / 大文件分块（2MB）+ 累积 SHA1

## Quick Start
1. 配置 application-prod.properties
2. 启动 MySQL（docker-compose 可选）
3. 启动 app（web-application-type=none）

## TIPs
cause of security,i remove all my real PWD and IP,
so if you want copy my project and add your bussiness,
pls do next TWO step:
1. search all "XXX" in this project
2. replace "XXX" to your local environments
