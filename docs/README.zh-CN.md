# Gym Product（中文文档）

这是一个开源的健身房多端管理平台，包含双后端、Web 管理端、移动端与微信小程序端。

## 项目结构

- `gymMaster`：Spring Boot 业务后端
- `gym-management-system-master`：Spring Boot 管理后端
- `gym-ui`：Vue 2 管理前端
- `gym-mobile-app`：React Native 移动端
- `GymMaster_wx`：微信小程序端

## 环境要求

- JDK 17
- Maven 3.8+
- Node.js 20
- MySQL / Redis

## 快速开始

### 1. 编译全仓

```powershell
mvn -f pom.xml -DskipTests compile
```

### 2. 启动前端

```powershell
Set-Location gym-ui
npm install
npm run dev
```

### 3. 启动后端

```powershell
Set-Location ..\gymMaster
mvn spring-boot:run -DskipTests

Set-Location ..\gym-management-system-master
mvn spring-boot:run -DskipTests
```

## 验证脚本

```powershell
powershell -ExecutionPolicy Bypass -File scripts/verify-all.ps1
powershell -ExecutionPolicy Bypass -File scripts/verify-frontend.ps1 -SkipInstall -SkipBuild
```

## 开源治理文件

- [LICENSE](../LICENSE)
- [贡献指南](../CONTRIBUTING.md)
- [行为准则](../CODE_OF_CONDUCT.md)
- [安全策略](../SECURITY.md)
- [支持说明](../SUPPORT.md)

## 关于 Problems 数量

一次性出现大量 Problems（如 540）主要来自历史代码的静态分析警告，不代表项目不可编译。建议以 CI 与构建结果为基线，分批治理 warnings。
