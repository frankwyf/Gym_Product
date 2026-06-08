# gym-management-system-master

[中文](#中文) | [日本語](#日本語) | [English](#english)

## 中文

这是一个基于 Spring Boot 的健身房管理后端，原始项目和 `gym-ui`、`GymMaster_wx` 配套使用。为适应公开仓库，本项目已移除本地构建产物和 IDE 元数据，并把敏感配置改为环境变量优先。

### 运行前提

- JDK 8 或 17
- Maven 3.8+
- MySQL
- Redis

### 关键配置

- 数据库 URL: `GYM_DB_URL`
- 数据库用户: `GYM_DB_USERNAME`
- 数据库密码: `GYM_DB_PASSWORD`
- JWT 密钥: `GYM_JWT_SECRET`
- Druid 管理用户: `GYM_DRUID_USERNAME`
- Druid 管理密码: `GYM_DRUID_PASSWORD`

默认配置文件位于 `src/main/resources/application.yml` 与 `src/main/resources/application-druid.yml`。

### 启动方式

```bash
mvn clean spring-boot:run -DskipTests
```

### 说明

- 管理前端位于上级目录中的 `../gym-ui`
- 微信小程序位于上级目录中的 `../GymMaster_wx`
- 超大数据库导出文件已从公开仓库版本中移除，说明见 `sql/README.md`

## 日本語

このプロジェクトは Spring Boot ベースの管理バックエンドです。公開向けに機密設定は環境変数化され、生成物は削除されています。

### 必要環境

- JDK 8 または 17
- Maven 3.8 以降
- MySQL
- Redis

### 起動

```bash
mvn clean spring-boot:run -DskipTests
```

## English

This is the Spring Boot admin backend for the gym management system. Generated files were removed and sensitive values were replaced with environment-variable-based placeholders.

### Requirements

- JDK 8 or 17
- Maven 3.8+
- MySQL
- Redis

### Start

```bash
mvn clean spring-boot:run -DskipTests
```


