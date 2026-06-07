# Gym Product Portfolio

[中文](#中文) | [日本語](#日本語) | [English](#english)

## 中文

这是一个为个人 GitHub 作品集整理后的多项目仓库，来源于早期学校课程作业。当前仓库仅保留与产品实现直接相关的代码、脚本和必要图片，已经完成一轮开源前整理：删除课程过程文档、编译产物、日志、IDE 元数据，并将硬编码凭据替换为可配置占位值。

### 项目组成

- `Source_Code/gym-management-system-master`: 基于 Spring Boot 的管理后端，配套 Vue 管理端与微信小程序。
- `Source_Code/gym-ui`: Vue 2 管理端。
- `Source_Code/gymMaster`: 另一套 Spring Boot 后端实现，包含邮件、账单和会员功能。
- `Source_Code/GymMaster_wx`: 微信小程序客户端。

### 已完成的开源整理

- 删除了课程报告、会议记录、个人贡献材料等非代码内容。
- 删除了 `.idea`、`target`、`dist`、日志目录、前端 `node_modules` 等本地生成内容。
- 脱敏了数据库密码、邮件凭据、JWT 密钥、小程序 `appid`、样例邮箱和手机号。
- 将后端敏感配置改为环境变量优先的占位写法。

### 当前验证结果

- 脱敏扫描已通过，未发现已知敏感值残留。
- 本机可用 `java`，但缺少全局 `mvn`、`node`、`npm`。
- `Source_Code/gymMaster` 自带的 Maven Wrapper 文件不完整，当前无法在无全局 Maven 的环境下直接构建。
- 因此本次整理主要完成了仓库清洁化、配置可移植化和文档开源化；完整运行仍需要补齐本地工具链。

### 建议的本地准备

1. 安装 JDK 8 或 JDK 17，并优先使用这两个长期支持版本运行旧项目。
2. 安装 Maven 3.8+。
3. 安装 Node.js 16 LTS 和 npm。
4. 准备 MySQL 5.7/8.0 与 Redis。
5. 按各子项目 README 中的说明配置环境变量和数据库。

## 日本語

これは個人 GitHub ポートフォリオ向けに整理したマルチプロジェクト構成のリポジトリです。元は大学の課題でしたが、現在は公開準備のためにコード中心の構成へ整理しています。授業用ドキュメント、ビルド成果物、ログ、IDE 固有ファイルを削除し、ハードコードされていた認証情報は環境変数ベースのプレースホルダーに置き換えました。

### 含まれるプロジェクト

- `Source_Code/gym-management-system-master`: Spring Boot ベースの管理バックエンド。
- `Source_Code/gym-ui`: Vue 2 管理画面。
- `Source_Code/gymMaster`: 別実装の Spring Boot バックエンド。
- `Source_Code/GymMaster_wx`: WeChat ミニプログラム。

### 現在の状態

- 既知の機密値は削除済みです。
- このマシンには `java` はありますが、`mvn`、`node`、`npm` は未導入です。
- `gymMaster` の Maven Wrapper は不完全なため、追加の Maven 環境なしではビルドできませんでした。

### 次に必要なもの

- JDK 8 または 17
- Maven 3.8 以降
- Node.js 16 LTS
- MySQL と Redis

## English

This repository is a cleaned-up portfolio version of an older university assignment. It now keeps only implementation-related source code, scripts, and useful assets. Course-process documents, build outputs, logs, and IDE metadata were removed, and hardcoded credentials were replaced with environment-variable-based placeholders.

### Repository layout

- `Source_Code/gym-management-system-master`: Spring Boot admin backend.
- `Source_Code/gym-ui`: Vue 2 admin frontend.
- `Source_Code/gymMaster`: Alternative Spring Boot backend implementation.
- `Source_Code/GymMaster_wx`: WeChat mini-program client.

### What was done for open-source readiness

- Removed non-code coursework materials.
- Removed generated folders such as `target`, `dist`, logs, IDE state, and local dependencies.
- Sanitized DB passwords, mail credentials, JWT secrets, sample emails, sample phone numbers, and the mini-program app id.
- Converted backend secrets to environment-variable-first configuration.

### Validation summary

- Known sensitive values were re-scanned and cleared.
- `java` is available on this machine, but global `mvn`, `node`, and `npm` are not.
- `Source_Code/gymMaster` includes an incomplete Maven Wrapper, so it cannot be built here without installing Maven first.

See the README in each subproject for setup details.