# DevTools 模块

## 模块概述

DevTools 是一个插件化的文本编辑器，支持多种文件类型的编辑和处理。采用插件架构，可以动态加载和管理插件。

## 模块信息

- **GroupId**: `com.daicy`
- **ArtifactId**: `DevTools`
- **版本**: `1.0.0-SNAPSHOT`
- **主类**: `com.daicy.devtools.TextEditor`

## 主要特性

- **插件化架构**: 支持动态加载和管理插件
- **多标签页编辑**: 同时打开多个文件
- **国际化支持**: 支持英文和中文界面
- **丰富的插件生态**: JSON 格式化、Markdown 编辑、Hosts 管理等

## 内置插件

### JSON 格式化插件

- JSON 格式化和验证
- 生成 JavaBean 代码
- 语法高亮

### Markdown 编辑器插件

- 实时预览
- 格式化工具
- 双栏显示

### Hosts 文件管理插件

- 编辑系统 Hosts 文件
- 语法高亮
- 自动保存

## 快速开始

### 运行

```bash
# 使用 Maven
mvn javafx:run

# 或运行 JAR
java -jar target/DevTools-1.0.0-SNAPSHOT-runnable.jar
```

### 构建

```bash
mvn clean package
```

## 插件开发

要开发自定义插件，请参考 [插件开发指南](../PLUGIN_DEVELOPMENT.md)。

## 配置文件

### plugins.json

插件配置文件，定义要加载的插件列表。

**位置**: `src/main/resources/plugins.json`

**格式**:
```json
[
  {
    "pluginClass": "com.daicy.devtools.plugin.JsonFormatPlugin"
  }
]
```

### 国际化资源

- `TextEditor.properties`: 英文资源
- `TextEditor_zh_CN.properties`: 中文资源

## 核心类

- **TextEditor**: 主应用程序类
- **PluginManager**: 插件管理器
- **TextPlugin**: 插件接口

## 更多信息

- [用户手册](../USER_GUIDE.md#devtools-使用指南)
- [API 文档](../API.md)
- [模块说明](../MODULES.md#devtools-模块)
- [插件开发指南](../PLUGIN_DEVELOPMENT.md)

