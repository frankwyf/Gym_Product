# Gym Product Portfolio

[中文](#中文) | [English](#english)

## 中文

这是一个健身房业务多端项目仓库，包含两个后端、一个管理端和一个微信小程序端。

### 目录结构

- gym-management-system-master: Spring Boot 管理后端
- gymMaster: Spring Boot 业务后端
- gym-ui: Vue 2 管理端
- GymMaster_wx: 微信小程序端

### 当前验证状态

- gym-ui 已验证可构建，开发启动命令为 `npm run dev`。
- gymMaster 已用本机正式环境启动成功（端口 8087）。
- gym-management-system-master 已验证启动入口可用，但实际启动需要本机 MySQL/Redis 正常运行；当前机器的 3306 未开启，所以它在数据库初始化阶段退出。

### 本地运行（Windows）

1. 准备依赖
- JDK 8 或 17
- Maven 3.8+
- Node.js 16 LTS（推荐）
- MySQL、Redis

2. 前端启动

```powershell
Set-Location gym-ui
npm install
npm run dev
```

3. 后端构建

```powershell
Set-Location gymMaster
mvn spring-boot:run -DskipTests
Set-Location ..\gym-management-system-master
mvn spring-boot:run -DskipTests
```

如果本机还没有启动 MySQL/Redis，`gym-management-system-master` 会在数据源初始化阶段失败，这是运行依赖，不是编译问题。

4. 一键脚本

```powershell
powershell -ExecutionPolicy Bypass -File scripts/setup-local-maven.ps1
powershell -ExecutionPolicy Bypass -File scripts/run-gymMaster.ps1
powershell -ExecutionPolicy Bypass -File scripts/run-gym-management-system-master.ps1
powershell -ExecutionPolicy Bypass -File scripts/verify-frontend.ps1
powershell -ExecutionPolicy Bypass -File scripts/verify-mobile.ps1
powershell -ExecutionPolicy Bypass -File scripts/verify-miniapp.ps1
# 快速模式（跳过依赖安装与构建，仅跑单测+smoke）
powershell -ExecutionPolicy Bypass -File scripts/verify-frontend.ps1 -SkipInstall -SkipBuild
# 全仓健康检查（移动端 + Web + 小程序 + 后端编译可用性）
powershell -ExecutionPolicy Bypass -File scripts/verify-all.ps1
# 快速模式（不重装依赖，且跳过前端 build）
powershell -ExecutionPolicy Bypass -File scripts/verify-all.ps1 -SkipInstall -SkipFrontendBuild
```

### 环境变量模板

- gym-management-system-master/.env.example
- gymMaster/.env.example
- gym-ui/.env.local.example

### 自动化与开源改造

- 已新增 CI: .github/workflows/ci.yml
- 已新增第一批关键单测（预约与账单）:
	- gymMaster/src/test/java/com/gymmaster/controller/ReservationControllerUnitTest.java
	- gymMaster/src/test/java/com/gymmaster/controller/BillControllerUnitTest.java
- 已补充登录页行为测试：登录状态 Cookie 读写与 rememberMe 逻辑（含字符串布尔值解析）
- 已建立代码风格基线：仓库级 `.editorconfig` + 前端 `Prettier`
- 已优化前端打包分片：对 `echarts` 与编辑器相关重依赖进行独立 chunk 拆分
- 前端风格命令：
	- `npm --prefix gym-ui run lint`
	- `npm --prefix gym-ui run lint:fix`
	- `npm --prefix gym-ui run format`
	- `npm --prefix gym-ui run format:check`
- 前端测试命令：
	- `npm --prefix gym-ui run test:unit`
	- `npm --prefix gym-ui run test:e2e:smoke`（运行带 `@smoke` 标签的用例；环境缺浏览器时自动跳过）
	- `npm --prefix gym-ui run test:e2e:full`（完整 E2E）
- 已增强 E2E 稳定性：本地若未安装 Playwright 浏览器二进制，将自动跳过冒烟用例而不是直接失败。
- 已完成三端国际化基础改造（英文/中文/日文）：
	- `gym-mobile-app`：全局语言状态与持久化，导航与关键页面多语言化
	- `gym-ui`：轻量 i18n 插件、导航语言切换、登录页多语言化
	- `GymMaster_wx`：全局 i18n 工具、mine 页语言切换、home/mine 核心文案多语言化
- 详细改造计划见 OPEN_SOURCE_OPTIMIZATION_PLAN.md

## English

This repository contains a multi-client gym product portfolio:

- gym-management-system-master: Spring Boot admin backend
- gymMaster: Spring Boot business backend
- gym-ui: Vue 2 admin frontend
- GymMaster_wx: WeChat mini-program client

### Current status

- gym-ui builds successfully.
- gymMaster is buildable and startable locally.
- gym-management-system-master is buildable; startup blockers identified and partially fixed for open-source portability.

### CI and tests

- CI workflow added at .github/workflows/ci.yml
- Initial unit tests added for reservation and bill critical logic in gymMaster
- Code-style baseline added: repo-level `.editorconfig` and frontend `Prettier` scripts
- Frontend CI now runs unit tests and smoke e2e by default
- CI smoke e2e installs Chromium before execution to avoid false skip in cloud environment

See OPEN_SOURCE_OPTIMIZATION_PLAN.md for roadmap details.