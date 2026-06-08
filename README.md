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

- gym-ui 可构建通过（Node 16 推荐，Node 24 需 OpenSSL 兼容参数）。
- gymMaster 可在本机启动（端口 8087）。
- gym-management-system-master 可构建通过，启动时已修复 YAML 配置问题；当前主要运行阻塞来自历史日志路径配置，已改为相对路径。

### 本地运行（Windows）

1. 准备依赖
- JDK 8 或 17
- Maven 3.8+
- Node.js 16 LTS（推荐）
- MySQL、Redis

2. 前端启动

```powershell
npm --prefix gym-ui install --legacy-peer-deps --no-audit --no-fund
$env:NODE_OPTIONS="--openssl-legacy-provider"
npm --prefix gym-ui run build:prod
```

3. 后端构建

```powershell
mvn -f gymMaster/pom.xml test
mvn -f gym-management-system-master/pom.xml -DskipTests -Dspring-boot.repackage.skip=true package
```

4. 一键脚本

```powershell
powershell -ExecutionPolicy Bypass -File scripts/setup-local-maven.ps1
powershell -ExecutionPolicy Bypass -File scripts/run-gymMaster.ps1
powershell -ExecutionPolicy Bypass -File scripts/run-gym-management-system-master.ps1
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
- 已建立代码风格基线：仓库级 `.editorconfig` + 前端 `Prettier`
- 前端风格命令：
	- `npm --prefix gym-ui run lint`
	- `npm --prefix gym-ui run lint:fix`
	- `npm --prefix gym-ui run format`
	- `npm --prefix gym-ui run format:check`
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

See OPEN_SOURCE_OPTIMIZATION_PLAN.md for roadmap details.