# SQL Notes

[中文](#中文) | [日本語](#日本語) | [English](#english)

## 中文

原仓库中的 `ry-vue.sql` 体积超过 GitHub 单文件限制，已从公开仓库版本中移除。

这不会影响项目代码运行本身。应用启动时使用的是数据库连接配置，而不是自动读取本目录下的 SQL 文件。

如果你需要本地初始化数据库，可以采用以下方式之一：

1. 从你自己的开发环境重新导出精简后的建表与示例数据脚本。
2. 只保留表结构、基础字典数据和演示账号，避免导出大量业务数据。
3. 按公开仓库版本重新生成一份适合分发的初始化脚本。

## 日本語

元の `ry-vue.sql` は GitHub の単一ファイル制限を超えていたため、公開版リポジトリから削除しました。

これはアプリ実行そのものには影響しません。起動時に使われるのはデータベース接続設定であり、このディレクトリの SQL ファイルを自動実行するわけではありません。

## English

The original `ry-vue.sql` file was removed from the public version of this repository because it exceeded GitHub's single-file size limit.

This does not affect application runtime. The application uses database connection settings and does not automatically execute SQL files from this directory.