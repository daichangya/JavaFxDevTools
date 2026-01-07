# JavaFxEditor 模块

## 模块概述

JavaFxEditor 是一款基于 JavaFX 技术精心打造的多功能文本编辑器，旨在为开发者、编程爱好者以及日常文字处理需求者提供卓越、高效的编辑体验。

## 模块信息

- **GroupId**: `com.daicy`
- **ArtifactId**: `JavaFxEditor`
- **版本**: `1.0.0-SNAPSHOT`
- **主类**: `com.daicy.javafxeditor.TestEditor`

## 主要特性

- **策略模式设计**: 使用 AppStrategy 接口实现可定制的编辑器行为
- **高性能编辑**: 基于 RichTextFX 的高性能代码编辑器
- **语法高亮**: 支持自定义语法高亮样式
- **撤销/重做**: 完整的编辑历史管理
- **文件管理**: 新建、打开、保存文档功能

## 核心设计

### 策略模式

JavaFxEditor 使用策略模式实现可定制的编辑器行为：

- **AppStrategy**: 策略接口，定义编辑器行为
- **EditorStrategy**: 默认策略实现
- **可扩展性**: 可以创建自定义策略实现不同的编辑器行为

## 快速开始

### 运行

```bash
# 使用 Maven
mvn javafx:run

# 或运行主类
java -cp target/classes com.daicy.javafxeditor.TestEditor
```

### 构建

```bash
mvn clean package
```

### 打包

```bash
mvn clean package javafx:jlink
```

## 核心类

### JavaFxEditor

编辑器主类，负责初始化窗口和 UI。

**包路径**: `com.daicy.javafxeditor.JavaFxEditor`

### AppStrategy

策略接口，定义可定制的编辑器行为。

**包路径**: `com.daicy.javafxeditor.AppStrategy`

### EditorStrategy

AppStrategy 的默认实现。

**包路径**: `com.daicy.javafxeditor.EditorStrategy`

### MainWindowController

主窗口控制器，处理用户交互。

**包路径**: `com.daicy.javafxeditor.MainWindowController`

### Workspace

抽象工作空间类，管理文档状态。

**包路径**: `com.daicy.javafxeditor.Workspace`

### JavaFxWorkspace

Workspace 的具体实现。

**包路径**: `com.daicy.javafxeditor.JavaFxWorkspace`

## 使用示例

### 使用默认策略

```java
JavaFxEditor editor = new JavaFxEditor();
editor.start(primaryStage);
```

### 使用自定义策略

```java
AppStrategy customStrategy = new MyCustomStrategy();
JavaFxEditor editor = new JavaFxEditor(customStrategy);
editor.start(primaryStage);
```

## 资源文件

- **MainWindow.fxml**: 主窗口布局文件
- **chronos-syntax.css**: 语法高亮样式文件
- **actionIcons/**: 操作图标目录

## 更多信息

- [用户手册](../USER_GUIDE.md#javafxeditor-使用指南)
- [API 文档](../API.md)
- [模块说明](../MODULES.md#javafxeditor-模块)
- [架构设计](../ARCHITECTURE.md)
