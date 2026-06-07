# gym-ui

[中文](#中文) | [日本語](#日本語) | [English](#english)

## 中文

这是与健身房管理系统配套的 Vue 2 管理端。项目基于 RuoYi-Vue 风格扩展，已移除 `dist`、`node_modules` 和 IDE 本地状态，适合重新安装依赖后启动。

### 运行前提

- Node.js 16 LTS
- npm 8+

### 安装与启动

```bash
npm install
npm run dev
```

### 构建

```bash
npm run build:stage
npm run build:prod
```

默认接口前缀由 `.env.*` 文件控制。

## 日本語

このプロジェクトは Vue 2 ベースの管理画面です。公開用に `dist` と `node_modules` を削除し、再インストール前提の状態に整えています。

### 必要環境

- Node.js 16 LTS
- npm 8 以降

### 起動

```bash
npm install
npm run dev
```

## English

This is the Vue 2 admin frontend for the gym management system. Local build output and dependencies were removed so the project can be reinstalled cleanly.

### Requirements

- Node.js 16 LTS
- npm 8+

### Run

```bash
npm install
npm run dev
```

### Build

```bash
npm run build:stage
npm run build:prod
```