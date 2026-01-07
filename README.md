# JavaFxDevTools

JavaFxDevTools 是一个基于 JavaFX 技术开发的多功能开发工具集，包含插件化的文本编辑器（DevTools）和可定制的代码编辑器（JavaFxEditor）。

## 项目简介

本项目是一个多模块的 Maven 项目，旨在为开发者提供一套功能丰富、可扩展的 JavaFX 桌面应用程序开发工具。

### 主要特性

- **插件化架构**：DevTools 采用插件系统，支持动态加载和管理插件
- **策略模式设计**：JavaFxEditor 使用策略模式实现可定制的编辑器行为
- **多模块结构**：清晰的项目结构，便于维护和扩展
- **国际化支持**：DevTools 支持多语言（英文/中文）
- **丰富的插件生态**：
  - JSON 格式化插件（支持格式化、验证、生成 JavaBean）
  - Markdown 编辑器插件（实时预览）
  - Hosts 文件管理插件（语法高亮、自动保存）

## 技术栈

- **Java**: JDK 17
- **构建工具**: Maven 3.x
- **UI 框架**: JavaFX 17.0.6
- **核心依赖**:
  - RichTextFX - 代码编辑器组件
  - Jackson - JSON 处理
  - CommonMark - Markdown 解析
  - AtlantFX - 现代化 UI 主题
  - Lombok - 简化代码

## 项目结构

```
JavaFxDevTools/
├── core/                    # 核心模块
│   └── 提供基础功能和共享组件
├── DevTools/                # 开发工具模块
│   └── 插件化文本编辑器，支持多种插件
├── JavaFxEditor/            # JavaFX 编辑器模块
│   └── 基于策略模式的可定制代码编辑器
└── pom.xml                  # 父 POM 配置
```

### 模块说明

- **core**: 核心模块，提供基础 JavaFX 组件和工具类
- **DevTools**: 插件化的文本编辑器，支持 JSON、Markdown、Hosts 等多种文件类型的编辑
- **JavaFxEditor**: 可定制的代码编辑器，使用策略模式实现不同的编辑器行为

## 快速开始

### 环境要求

- JDK 17 或更高版本
- Maven 3.6 或更高版本

### 构建项目

```bash
# 克隆项目
git clone <repository-url>
cd JavaFxDevTools

# 编译所有模块
mvn clean compile

# 打包所有模块
mvn clean package
```

### 运行 DevTools

```bash
cd DevTools
mvn javafx:run
```

或者直接运行打包后的 JAR 文件：

```bash
java -jar DevTools/target/DevTools-1.0.0-SNAPSHOT-runnable.jar
```

### 运行 JavaFxEditor

```bash
cd JavaFxEditor
mvn javafx:run
```

## 功能概览

### DevTools

- **多标签页编辑**：支持同时打开多个文件
- **插件系统**：通过配置文件动态加载插件
- **文件操作**：新建、打开、保存、另存为
- **国际化**：支持英文和中文界面
- **插件管理**：可视化的插件安装和卸载

### JavaFxEditor

- **代码编辑**：基于 RichTextFX 的高性能代码编辑器
- **语法高亮**：支持自定义语法高亮样式
- **撤销/重做**：完整的编辑历史管理
- **文件管理**：新建、打开、保存文档
- **策略模式**：通过 AppStrategy 接口定制编辑器行为

## 文档

- [架构设计文档](ARCHITECTURE.md) - 系统架构、模块关系、设计模式
- [开发指南](DEVELOPMENT.md) - 开发环境搭建、构建说明、开发规范
- [用户手册](USER_GUIDE.md) - 使用说明、功能介绍、操作指南
- [API 文档](API.md) - 核心 API 接口说明
- [模块说明](MODULES.md) - 各模块详细说明
- [插件开发指南](PLUGIN_DEVELOPMENT.md) - 如何开发自定义插件

## 贡献指南

欢迎贡献代码！请遵循以下步骤：

1. Fork 本项目
2. 创建特性分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 许可证

本项目采用 MIT 许可证。

## 作者

@author daicy

## 相关链接

- [JavaFX 官方文档](https://openjfx.io/)
- [RichTextFX 项目](https://github.com/FXMisc/RichTextFX)
- [项目主页](https://zthinker.com)

