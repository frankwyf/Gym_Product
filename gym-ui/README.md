# gym-ui

[中文](#中文) | [日本語](#日本語) | [English](#english)

## 中文

这是与健身房管理系统配套的 Vue 2 管理端。项目基于 RuoYi-Vue 风格扩展，已移除 `dist`、`node_modules` 和 IDE 本地状态，适合重新安装依赖后启动。

当前已接入轻量多语言基础能力（英文/中文/日文），并在顶部导航提供语言切换入口（登录页、注册页、导航与 401/404 页面文案已完成三语化示例）。

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

### 国际化校验

```bash
npm run i18n:check
```

用于检查 `en/zh/ja` 词条键是否一致，避免新增文案时遗漏翻译。

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

A lightweight i18n baseline (English/Chinese/Japanese) is integrated, with a language switch entry in the top navigation. Login, register, navbar, and 401/404 pages are localized.

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

### i18n Check

```bash
npm run i18n:check
```

This checks key consistency across `en/zh/ja` locale dictionaries.