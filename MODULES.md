# 模块详细说明

本文档详细介绍 JavaFxDevTools 项目的各个模块。

## 项目模块概览

JavaFxDevTools 是一个多模块 Maven 项目，包含以下模块：

- **core**: 核心模块，提供基础功能和共享组件
- **DevTools**: 开发工具模块，插件化文本编辑器
- **JavaFxEditor**: JavaFX 编辑器模块，可定制的代码编辑器

## core 模块

### 模块概述

core 模块是项目的基础模块，提供其他模块共享的基础功能和组件。

### 模块信息

- **模块路径**: `core/`
- **GroupId**: `com.daicy`
- **ArtifactId**: `core`
- **版本**: `1.0.0-SNAPSHOT`

### 主要依赖

- JavaFX Controls
- JavaFX FXML
- JavaFX Web
- RichTextFX
- Apache Commons Lang3
- Commons IO
- Jackson (Core, Databind, Annotations)
- Lombok

### 核心类

#### HelloApplication

示例 JavaFX 应用程序，展示基本的 JavaFX 应用结构。

**包路径**: `com.daicy.core.HelloApplication`

**功能**:
- 基本的 JavaFX Application 实现
- 加载 FXML 文件
- 显示简单窗口

#### HelloController

HelloApplication 的控制器类。

**包路径**: `com.daicy.core.HelloController`

**功能**:
- 处理 FXML 中定义的事件
- 管理 UI 交互

### 资源文件

- `hello-view.fxml`: HelloApplication 的 FXML 布局文件

### 使用场景

core 模块主要作为其他模块的依赖，提供：
- 基础的 JavaFX 组件
- 共享的工具类
- 通用的 FXML 资源

### 构建和运行

```bash
cd core
mvn clean package
```

## DevTools 模块

### 模块概述

DevTools 是一个插件化的文本编辑器，支持多种文件类型的编辑和处理。采用插件架构，可以动态加载和管理插件。

### 模块信息

- **模块路径**: `DevTools/`
- **GroupId**: `com.daicy`
- **ArtifactId**: `DevTools`
- **版本**: `1.0.0-SNAPSHOT`
- **主类**: `com.daicy.devtools.TextEditor`

### 主要依赖

- core 模块
- AtlantFX Base (现代化 UI 主题)
- MySQL Connector (可选，用于数据库相关功能)
- CommonMark (Markdown 解析)

### 核心类

#### TextEditor

主应用程序类，管理窗口、菜单和插件系统。

**包路径**: `com.daicy.devtools.TextEditor`

**主要功能**:
- 应用程序入口点
- 创建和管理主窗口
- 管理菜单栏和工具栏
- 集成插件系统
- 多标签页管理
- 文件操作（新建、打开、保存）

**关键方法**:
- `start(Stage primaryStage)`: 应用程序启动方法
- `createNewTab()`: 创建新标签页
- `openFile()`: 打开文件
- `saveCurrentFile()`: 保存当前文件
- `createPluginTab()`: 创建插件标签页

#### PluginManager

插件管理器，负责插件的加载、安装和卸载。

**包路径**: `com.daicy.devtools.PluginManager`

**主要功能**:
- 从配置文件加载插件
- 管理已安装插件列表
- 提供插件查询接口

**关键方法**:
- `loadPlugins()`: 从 plugins.json 加载插件
- `installPlugin(TextPlugin)`: 安装插件
- `uninstallPlugin(TextPlugin)`: 卸载插件
- `getPlugins()`: 获取已安装插件列表

#### TextPlugin

插件接口，定义插件必须实现的方法。

**包路径**: `com.daicy.devtools.TextPlugin`

**接口方法**:
- `getContentPane()`: 获取插件 UI 面板
- `getDefaultPath()`: 获取默认文件路径
- `open(String)`: 打开文件
- `save(String)`: 保存文件
- `getContent()`: 获取内容
- `setContent(String)`: 设置内容

#### PluginInfo

插件信息类，用于从 JSON 配置文件读取插件信息。

**包路径**: `com.daicy.devtools.PluginInfo`

**字段**:
- `pluginClass`: 插件类的全限定名

### 插件实现

#### JsonFormatPlugin

JSON 格式化插件，提供 JSON 格式化、验证和 JavaBean 生成功能。

**包路径**: `com.daicy.devtools.plugin.JsonFormatPlugin`

**功能**:
- JSON 格式化：将压缩的 JSON 格式化为易读格式
- JSON 验证：检查 JSON 格式是否正确，显示错误位置
- 生成 JavaBean：根据 JSON 结构生成 Java 类代码
- 语法高亮：自动高亮 JSON 关键字
- 关键字转义：处理转义字符

**依赖类**:
- `JsonHighlighting`: JSON 语法高亮实现
- `JsonToBean`: JSON 转 JavaBean 代码生成器

#### MarkdownEditorPlugin

Markdown 编辑器插件，提供实时预览功能。

**包路径**: `com.daicy.devtools.plugin.MarkdownEditorPlugin`

**功能**:
- 实时预览：编辑 Markdown 时实时显示渲染结果
- 格式化工具：快速插入 Markdown 语法（加粗、斜体、标题）
- 双栏显示：左侧编辑，右侧预览

**技术实现**:
- 使用 CommonMark 解析 Markdown
- 使用 WebView 显示 HTML 预览

#### HostsManagerPlugin

Hosts 文件管理插件，用于编辑系统 Hosts 文件。

**包路径**: `com.daicy.devtools.plugin.HostsManagerPlugin`

**功能**:
- 编辑系统 Hosts 文件
- 语法高亮：自动高亮 IP 地址和注释
- 行号显示：显示行号便于定位
- 自动保存：支持直接保存到系统 Hosts 文件

**技术实现**:
- 使用异步任务进行语法高亮，避免阻塞 UI
- 使用正则表达式匹配 IP 地址和注释
- 使用 VirtualizedScrollPane 优化性能

**依赖类**:
- `HostsUtil`: Hosts 文件操作工具类

### 资源文件

#### 配置文件

- `plugins.json`: 插件配置文件，定义要加载的插件列表

**格式**:
```json
[
  {
    "pluginClass": "com.daicy.devtools.plugin.JsonFormatPlugin"
  },
  {
    "pluginClass": "com.daicy.devtools.plugin.MarkdownEditorPlugin"
  },
  {
    "pluginClass": "com.daicy.devtools.plugin.HostsManagerPlugin"
  }
]
```

#### 国际化资源

- `TextEditor.properties`: 英文资源文件
- `TextEditor_zh_CN.properties`: 中文资源文件

**支持的语言**:
- English (英文)
- 中文 (简体中文)

#### 样式文件

- `json-keywords.css`: JSON 语法高亮样式
- `hosts-keywords.css`: Hosts 文件语法高亮样式

#### 其他资源

- `zthinker.png`: 应用图标

### 插件系统架构

DevTools 采用插件化架构，具有以下特点：

1. **动态加载**: 插件通过配置文件定义，应用启动时自动加载
2. **接口隔离**: 插件通过 `TextPlugin` 接口与主程序交互
3. **独立 UI**: 每个插件提供自己的 UI 面板
4. **文件操作**: 插件可以处理特定类型的文件

### 国际化支持

DevTools 支持多语言界面：

1. **资源文件**: 使用 Java ResourceBundle 机制
2. **动态切换**: 可以在运行时切换语言
3. **支持语言**: 英文、中文（简体）

### 构建和运行

#### 构建

```bash
cd DevTools
mvn clean package
```

#### 运行

```bash
# 使用 Maven 插件
mvn javafx:run

# 或运行打包后的 JAR
java -jar target/DevTools-1.0.0-SNAPSHOT-runnable.jar
```

#### 打包为可执行文件

DevTools 配置了 `javapackager` 插件，可以打包为可执行文件：

```bash
mvn clean package
```

打包后的文件位于 `target/DevTools/` 目录。

## JavaFxEditor 模块

### 模块概述

JavaFxEditor 是一个可定制的代码编辑器，使用策略模式实现不同的编辑器行为。

### 模块信息

- **模块路径**: `JavaFxEditor/`
- **GroupId**: `com.daicy`
- **ArtifactId**: `JavaFxEditor`
- **版本**: `1.0.0-SNAPSHOT`
- **主类**: `com.daicy.javafxeditor.TestEditor`

### 主要依赖

- core 模块

### 核心类

#### JavaFxEditor

编辑器主类，负责初始化窗口和 UI。

**包路径**: `com.daicy.javafxeditor.JavaFxEditor`

**主要功能**:
- 加载 FXML 文件
- 初始化主窗口控制器
- 应用策略模式配置编辑器行为

**关键方法**:
- `start(Stage primaryStage)`: 启动编辑器

#### AppStrategy

策略接口，定义可定制的编辑器行为。

**包路径**: `com.daicy.javafxeditor.AppStrategy`

**接口方法**:
- `getTitle()`: 获取应用标题
- `createSourceFileChooser()`: 创建文件选择器
- `createCodeEditor()`: 创建代码编辑器
- `getSavedSourceFile()`: 获取保存的文件
- `settingsSupported()`: 是否支持设置
- `showSettings()`: 显示设置
- `getSyntaxCss()`: 获取语法高亮 CSS
- `showOnlineReference()`: 显示在线参考
- `showAboutWindow()`: 显示关于窗口

#### EditorStrategy

AppStrategy 的默认实现。

**包路径**: `com.daicy.javafxeditor.EditorStrategy`

**实现功能**:
- 提供默认的编辑器行为
- 配置文件选择器
- 创建代码编辑器
- 提供语法高亮 CSS

#### MainWindowController

主窗口控制器，处理用户交互。

**包路径**: `com.daicy.javafxeditor.MainWindowController`

**主要功能**:
- 管理菜单和工具栏
- 处理文件操作（新建、打开、保存）
- 处理编辑操作（撤销、重做、剪切、复制、粘贴）
- 绑定 UI 状态

**关键方法**:
- `init(Stage stage, AppStrategy appStrategy)`: 初始化控制器
- `newDocument()`: 新建文档
- `openDocument()`: 打开文档
- `saveDocument()`: 保存文档
- `undo()`: 撤销
- `redo()`: 重做

#### Workspace

抽象工作空间类，管理文档状态。

**包路径**: `com.daicy.javafxeditor.Workspace`

**主要功能**:
- 管理文档文件状态
- 管理修改状态
- 处理文档操作（新建、打开、保存）
- 处理窗口关闭事件

**关键属性**:
- `documentFileOptional`: 当前文档文件（Optional）
- `modified`: 文档是否已修改

#### JavaFxWorkspace

Workspace 的具体实现，用于 JavaFX 编辑器。

**包路径**: `com.daicy.javafxeditor.JavaFxWorkspace`

**实现方法**:
- `doNew()`: 创建新文档
- `doOpen(File)`: 打开文件
- `doSave(File)`: 保存文件

#### TestEditor

应用程序入口点。

**包路径**: `com.daicy.javafxeditor.TestEditor`

**主要功能**:
- 应用程序主类
- 创建 JavaFxEditor 实例
- 设置 Dock 图标（macOS）

### 资源文件

#### FXML 文件

- `MainWindow.fxml`: 主窗口布局文件

**包含组件**:
- 菜单栏（File、Edit、Help）
- 工具栏
- 编辑区域

#### 样式文件

- `chronos-syntax.css`: 语法高亮样式文件（由 EditorStrategy 指定）

#### 图标资源

- `actionIcons/`: 操作图标目录
  - `new.png`: 新建图标
  - `open.png`: 打开图标
  - `save.png`: 保存图标
  - 等等

#### 其他资源

- `zthinker.png`: 应用图标

### 策略模式实现

JavaFxEditor 使用策略模式实现可定制的编辑器行为：

1. **策略接口**: `AppStrategy` 定义编辑器行为接口
2. **策略实现**: `EditorStrategy` 提供默认实现
3. **上下文**: `JavaFxEditor` 和 `MainWindowController` 使用策略
4. **可扩展性**: 可以创建自定义策略实现不同的编辑器行为

### 构建和运行

#### 构建

```bash
cd JavaFxEditor
mvn clean package
```

#### 运行

```bash
# 使用 Maven 插件
mvn javafx:run

# 或运行主类
java -cp target/classes com.daicy.javafxeditor.TestEditor
```

#### 打包

使用 `javafx-maven-plugin` 可以打包为可执行文件：

```bash
mvn clean package javafx:jlink
```

## 模块间关系

### 依赖关系

```
JavaFxDevTools (父POM)
├── core (基础模块)
├── DevTools (依赖 core)
└── JavaFxEditor (依赖 core)
```

### 模块职责划分

- **core**: 提供基础功能和共享组件
- **DevTools**: 插件化文本编辑器，专注于插件系统
- **JavaFxEditor**: 可定制的代码编辑器，专注于策略模式实现

### 共享资源

- core 模块提供的基础组件可以被其他模块使用
- 各模块的资源文件独立管理
- 依赖版本由父 POM 统一管理

## 模块开发建议

### 添加新功能到现有模块

1. 确定功能所属模块
2. 在相应模块中添加代码
3. 更新模块文档
4. 添加必要的测试

### 创建新模块

1. 在父 POM 中添加模块声明
2. 创建模块目录和 pom.xml
3. 配置模块依赖
4. 实现模块功能
5. 更新项目文档

### 模块间通信

- 通过接口定义模块间交互
- 使用依赖注入管理模块依赖
- 避免循环依赖

