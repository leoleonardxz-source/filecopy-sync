# filecopy-sync

微信微盘一个通用的“增量同步”项目：SQLServer(源) -> Nginx下载 -> WeDrive上传(目标) + MySQL落库(状态/映射)。<br>
通过企业微信自建应用的secret,agentId来进行微盘权限操作<br>
纯java springboot应用，且不包含web服务，我去除了springboot-web-starter更加轻量级.<br>
简述一下流程：从内部系统下载文件，再同步到企业微信微盘上面，按照内部的文件存放路径，在微盘上创建各级目录，并包含旧文件归档，新文件替换等功能。当然核心功能代码我已经去掉了，需要你自己补充。<br>
最后还做了扩展功能，这个jar包在运行期间任何exception，都会截取前30行异常打印信息，给固定的企业微信成员发送消息

## TIPs
cause of security,i remove all my real PWD and IP,
so if you want copy my project and add your bussiness,
pls do next TWO step:
1. search all "XXX" in this project
2. replace "XXX" to your local environments


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

##流程图
```mermaid
flowchart TD
    A[定时任务触发]
    A --> B[读取 lastSyncTime\n加回看窗口]
    B --> C[SQLServer 增量分页查询\neffective_time 与 fid]
    C --> D[MySQL 批量查询同步状态\nfile_id IN 列表 生成 Map]
    D --> E{是否需要同步}
    E -- 否 --> C
    E -- 是 --> F[下载文件到临时目录]
    F --> G[确保微盘目录存在\nensureFolders]
    G --> H{文件大小判断}
    H -- 小于 10MB --> I[直传上传]
    H -- 大于等于 10MB --> J[2MB 分块上传]
    I --> K[更新 MySQL 映射与状态]
    J --> K
    K --> L[清理或保留临时文件]
    L --> M[记录本次最大 effective_time]
    M --> C


