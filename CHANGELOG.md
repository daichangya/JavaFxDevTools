# 更新日志

本文档记录项目的所有重要变更。

格式基于 [Keep a Changelog](https://keepachangelog.com/zh-CN/1.0.0/)，
版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

## [1.0.0] - 2025-01-XX

### 新增

#### 核心功能
- **core 模块**: 基础模块，提供共享的 JavaFX 组件和工具类
- **DevTools 模块**: 插件化文本编辑器
  - 多标签页编辑支持
  - 插件系统，支持动态加载和管理插件
  - 国际化支持（英文/中文）
  - 文件操作（新建、打开、保存、另存为）
  - 插件管理界面

- **JavaFxEditor 模块**: 可定制的代码编辑器
  - 基于策略模式的可定制编辑器行为
  - 高性能代码编辑器（基于 RichTextFX）
  - 语法高亮支持
  - 完整的撤销/重做功能
  - 文件管理功能

#### 插件
- **JSON 格式化插件** (`JsonFormatPlugin`)
  - JSON 格式化和美化
  - JSON 验证和错误提示
  - 生成 JavaBean 代码
  - 语法高亮
  - 关键字转义处理

- **Markdown 编辑器插件** (`MarkdownEditorPlugin`)
  - 实时预览功能
  - 格式化工具栏（加粗、斜体、标题）
  - 双栏显示（编辑/预览）

- **Hosts 文件管理插件** (`HostsManagerPlugin`)
  - 编辑系统 Hosts 文件
  - 语法高亮（IP 地址、注释）
  - 行号显示
  - 自动保存到系统文件

#### 文档
- 完整的项目文档体系
  - README.md - 项目总览和快速开始
  - ARCHITECTURE.md - 系统架构设计文档
  - DEVELOPMENT.md - 开发指南
  - USER_GUIDE.md - 用户使用手册
  - API.md - 核心 API 文档
  - MODULES.md - 模块详细说明
  - PLUGIN_DEVELOPMENT.md - 插件开发指南
  - CONTRIBUTING.md - 贡献指南

#### 开发工具
- GitHub Actions CI/CD 工作流
- Issue 模板（Bug 报告、功能请求）
- Pull Request 模板
- 代码规范和 JavaDoc 注释

### 技术栈
- Java 17
- JavaFX 17.0.6
- Maven 3.x
- RichTextFX 0.11.4
- Jackson 2.15.2
- CommonMark 0.18.0
- AtlantFX 2.0.1
- Lombok 1.18.24

### 设计模式
- 插件模式（Plugin Pattern）
- 策略模式（Strategy Pattern）

---

## [未发布]

### 计划中
- 更多插件支持
- 主题定制功能
- 插件市场
- 更多文件格式支持

---

[1.0.0]: https://github.com/daichangya/JavaFxDevTools/releases/tag/v1.0.0

