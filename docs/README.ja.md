# Gym Product（日本語ドキュメント）

本リポジトリは、バックエンド 2 系統、Web 管理画面、モバイルアプリ、WeChat ミニプログラムを含むオープンソースのジム管理プラットフォームです。

## 構成

- `gymMaster`: Spring Boot 業務バックエンド
- `gym-management-system-master`: Spring Boot 管理バックエンド
- `gym-ui`: Vue 2 管理フロントエンド
- `gym-mobile-app`: React Native モバイルアプリ
- `GymMaster_wx`: WeChat ミニプログラム

## 必要環境

- JDK 17
- Maven 3.8+
- Node.js 20
- MySQL / Redis

## クイックスタート

### 1. 全体ビルド

```powershell
mvn -f pom.xml -DskipTests compile
```

### 2. フロントエンド起動

```powershell
Set-Location gym-ui
npm install
npm run dev
```

### 3. バックエンド起動

```powershell
Set-Location ..\gymMaster
mvn spring-boot:run -DskipTests

Set-Location ..\gym-management-system-master
mvn spring-boot:run -DskipTests
```

## 検証スクリプト

```powershell
powershell -ExecutionPolicy Bypass -File scripts/verify-all.ps1
powershell -ExecutionPolicy Bypass -File scripts/verify-frontend.ps1 -SkipInstall -SkipBuild
```

## OSS 主要ファイル

- [LICENSE](../LICENSE)
- [CONTRIBUTING](../CONTRIBUTING.md)
- [CODE_OF_CONDUCT](../CODE_OF_CONDUCT.md)
- [SECURITY](../SECURITY.md)
- [SUPPORT](../SUPPORT.md)

## Problems 数が急増する理由

Problems が一気に増える（例: 540 件）のは、主に既存コードへの静的解析警告（raw generics、未使用 import、スタイル規約）です。コンパイル失敗とは別問題のため、CI/ビルド成功を基準に段階的に整理する方針を推奨します。
