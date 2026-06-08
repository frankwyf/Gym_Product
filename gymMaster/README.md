# gymMaster

[中文](#中文) | [日本語](#日本語) | [English](#english)

## 中文

这是另一套 Spring Boot 健身房系统后端实现，包含会员、设施、签到、邮件验证码和账单相关功能。本次整理已完成配置脱敏、日志与构建产物清理，但当前目录中的 Maven Wrapper 文件不完整，因此需要全局 Maven 才能重新构建。

### 运行前提

- JDK 8 或 17
- Maven 3.8+
- MySQL
- Redis
- 可选邮件服务账号

### 关键环境变量

- `GYMMASTER_DB_URL`
- `GYMMASTER_DB_USERNAME`
- `GYMMASTER_DB_PASSWORD`
- `GYMMASTER_MAIL_USERNAME`
- `GYMMASTER_MAIL_PASSWORD`
- `GYMMASTER_STATIC_LOCATIONS`

### 数据库

示例数据库脚本位于 `gymmaster.sql`。其中示例邮箱与手机号已替换为公开占位值。

### 启动

```bash
mvn clean package -DskipTests
mvn spring-boot:run -DskipTests
```

如果你希望继续使用 Maven Wrapper，需要先补齐 `.mvn/wrapper` 目录。

## 日本語

これは別実装の Spring Boot バックエンドです。機密情報は環境変数化され、ログと生成物は削除済みです。ただし Maven Wrapper が不完全なため、再ビルドにはグローバル Maven が必要です。

## English

This is an alternative Spring Boot backend implementation for the gym system. Sensitive configuration was sanitized and generated content was removed. The current Maven Wrapper is incomplete, so a global Maven installation is required to rebuild it.