# 插件开发指南

本文档介绍如何为 DevTools 开发自定义插件。

## 插件系统概述

DevTools 采用插件化架构，通过 `TextPlugin` 接口定义插件规范。插件可以：

- 提供自定义的 UI 界面
- 处理特定类型的文件
- 实现特定的功能（如格式化、转换等）

## 插件接口

### TextPlugin 接口

所有插件必须实现 `TextPlugin` 接口。

**包路径**: `com.daicy.devtools.TextPlugin`

**接口定义**:
```java
public interface TextPlugin {
    Pane getContentPane();
    Path getDefaultPath();
    void open(String filePath);
    void save(String filePath);
    String getContent();
    void setContent(String content);
}
```

### 接口方法说明

#### `getContentPane()`

返回插件的 UI 面板。此面板将显示在标签页中。

**返回**: `Pane` - 插件的根面板

**要求**:
- 必须返回非 `null` 值
- 面板应该包含所有必要的 UI 组件
- 面板应该在构造函数中创建

#### `getDefaultPath()`

返回插件的默认文件路径。如果插件有默认打开的文件，可以返回该路径。

**返回**: `Path` - 默认文件路径，如果没有默认路径返回 `null`

**使用场景**:
- Hosts 文件管理插件返回系统 Hosts 文件路径
- 配置文件编辑器返回默认配置文件路径

#### `open(String filePath)`

打开文件。当用户通过"打开文件"功能选择文件时调用。

**参数**:
- `filePath` (String): 要打开的文件路径

**要求**:
- 应该读取文件内容
- 应该将内容显示在 UI 中
- 应该处理文件读取异常

#### `save(String filePath)`

保存文件。当用户保存文件时调用。

**参数**:
- `filePath` (String): 要保存的文件路径

**要求**:
- 应该获取当前内容
- 应该将内容写入文件
- 应该处理文件写入异常

#### `getContent()`

获取当前编辑的内容。

**返回**: `String` - 当前内容

**要求**:
- 应该返回用户在 UI 中输入或编辑的内容
- 应该返回完整的、可保存的内容

#### `setContent(String content)`

设置要显示的内容。

**参数**:
- `content` (String): 要设置的内容

**要求**:
- 应该将内容显示在 UI 中
- 应该替换现有内容

## 开发步骤

### 1. 创建插件类

创建一个新类，实现 `TextPlugin` 接口。

```java
package com.daicy.devtools.plugin;

import com.daicy.devtools.TextPlugin;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;

import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * 示例插件：简单的文本编辑器
 * 
 * @author daicy
 */
public class SimpleTextPlugin implements TextPlugin {
    private final VBox contentPane;
    private final TextArea textArea;
    
    public SimpleTextPlugin() {
        contentPane = new VBox();
        textArea = new TextArea();
        contentPane.getChildren().add(textArea);
    }
    
    @Override
    public Pane getContentPane() {
        return contentPane;
    }
    
    @Override
    public Path getDefaultPath() {
        return null; // 没有默认路径
    }
    
    @Override
    public void open(String filePath) {
        try {
            String content = Files.readString(Paths.get(filePath));
            setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void save(String filePath) {
        try {
            Files.write(Paths.get(filePath), getContent().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public String getContent() {
        return textArea.getText();
    }
    
    @Override
    public void setContent(String content) {
        textArea.setText(content);
    }
}
```

### 2. 注册插件

在 `plugins.json` 文件中注册插件。

**文件位置**: `DevTools/src/main/resources/plugins.json`

**格式**:
```json
[
  {
    "pluginClass": "com.daicy.devtools.plugin.SimpleTextPlugin"
  }
]
```

**注意事项**:
- 使用插件的全限定类名
- 类名必须正确，否则加载失败
- 插件类必须实现 `TextPlugin` 接口

### 3. 编译和测试

1. 编译项目：
```bash
cd DevTools
mvn clean compile
```

2. 运行应用测试插件：
```bash
mvn javafx:run
```

3. 检查插件是否出现在侧边栏

## 插件示例

### 示例 1: JSON 格式化插件

参考 `JsonFormatPlugin` 的实现，了解如何：

- 使用 CodeArea 进行代码编辑
- 实现语法高亮
- 处理用户交互（按钮点击）
- 显示错误信息

**关键特性**:
- 双编辑器布局（输入/输出）
- JSON 验证和错误提示
- 语法高亮
- 生成 JavaBean 代码

### 示例 2: Markdown 编辑器插件

参考 `MarkdownEditorPlugin` 的实现，了解如何：

- 使用双栏布局（编辑/预览）
- 实时预览功能
- 使用第三方库（CommonMark）解析 Markdown
- 使用 WebView 显示 HTML

**关键特性**:
- 实时预览
- 格式化工具栏
- WebView 渲染

### 示例 3: Hosts 文件管理插件

参考 `HostsManagerPlugin` 的实现，了解如何：

- 使用默认路径
- 实现异步语法高亮
- 处理系统文件
- 使用正则表达式进行语法分析

**关键特性**:
- 自动加载系统文件
- 异步语法高亮（不阻塞 UI）
- 行号显示
- 直接保存到系统文件

## 高级功能

### 语法高亮

使用 RichTextFX 的 `CodeArea` 和 `StyleSpans` 实现语法高亮。

**示例**:
```java
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

CodeArea codeArea = new CodeArea();

// 计算语法高亮
StyleSpans<Collection<String>> highlighting = computeHighlighting(text);

// 应用语法高亮
codeArea.setStyleSpans(0, highlighting);
```

**参考**: `JsonHighlighting` 和 `HostsManagerPlugin` 的实现

### 异步处理

对于耗时的操作（如语法高亮、文件解析），使用异步任务避免阻塞 UI。

**示例**:
```java
import javafx.concurrent.Task;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

ExecutorService executor = Executors.newSingleThreadExecutor();

Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
    @Override
    protected StyleSpans<Collection<String>> call() throws Exception {
        return computeHighlighting(text);
    }
};

executor.execute(task);

// 使用 ReactFX 处理结果
codeArea.richChanges()
    .successionEnds(Duration.ofMillis(500))
    .supplyTask(() -> task)
    .awaitLatest(codeArea.richChanges())
    .subscribe(highlighting -> codeArea.setStyleSpans(0, highlighting));
```

**参考**: `HostsManagerPlugin` 的实现

### 文件操作

#### 读取文件

```java
import java.nio.file.Files;
import java.nio.file.Paths;

String content = Files.readString(Paths.get(filePath));
```

#### 写入文件

```java
import java.nio.file.Files;
import java.nio.file.Paths;

Files.write(Paths.get(filePath), content.getBytes());
```

#### 处理异常

```java
try {
    String content = Files.readString(Paths.get(filePath));
    setContent(content);
} catch (IOException e) {
    // 显示错误提示
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("错误");
    alert.setHeaderText("文件读取失败");
    alert.setContentText(e.getMessage());
    alert.showAndWait();
}
```

### UI 组件

#### 使用 CodeArea

`CodeArea` 是 RichTextFX 提供的代码编辑器组件，支持：

- 语法高亮
- 撤销/重做
- 行号显示
- 虚拟化滚动（大文件性能优化）

```java
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.flowless.VirtualizedScrollPane;

CodeArea codeArea = new CodeArea();
codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));

// 使用 VirtualizedScrollPane 包装以优化性能
VirtualizedScrollPane<CodeArea> scrollPane = new VirtualizedScrollPane<>(codeArea);
```

#### 使用 WebView

用于显示 HTML 内容（如 Markdown 预览）。

```java
import javafx.scene.web.WebView;

WebView webView = new WebView();
webView.getEngine().loadContent(htmlContent);
```

### 样式和主题

#### 加载 CSS 文件

```java
contentPane.getStylesheets().add(
    getClass().getClassLoader().getResource("my-plugin.css").toExternalForm()
);
```

#### 定义 CSS 类

在 CSS 文件中定义样式类，然后在代码中应用：

```css
.json-property {
    -fx-fill: #a626a4;
}

.json-string {
    -fx-fill: #50a14f;
}

.json-number {
    -fx-fill: #986801;
}
```

在代码中应用：

```java
spansBuilder.add(Collections.singleton("json-property"), length);
```

## 最佳实践

### 1. 错误处理

- 所有文件操作都应该有异常处理
- 向用户显示友好的错误消息
- 记录错误日志（使用 `printStackTrace()` 或日志框架）

### 2. 性能优化

- 大文件使用虚拟化组件（VirtualizedScrollPane）
- 耗时操作使用异步任务
- 避免在 UI 线程中执行长时间运行的操作

### 3. 用户体验

- 提供清晰的 UI 布局
- 添加工具提示（Tooltip）
- 提供快捷键支持（如果适用）
- 显示加载状态（对于耗时操作）

### 4. 代码组织

- 将复杂功能拆分为独立方法
- 使用有意义的变量和方法名
- 添加必要的注释
- 遵循 Java 编码规范

### 5. 资源管理

- 正确管理资源（如 ExecutorService）
- 在插件销毁时清理资源
- 避免内存泄漏

## 调试技巧

### 1. 检查插件是否加载

在 `PluginManager.loadPlugins()` 方法中添加日志输出，检查插件是否成功加载。

### 2. 测试插件功能

- 创建测试文件
- 测试打开、编辑、保存功能
- 测试边界情况（空文件、大文件等）

### 3. 使用 IDE 调试器

- 在插件代码中设置断点
- 使用 IDE 调试功能逐步执行
- 检查变量值

## 常见问题

### Q: 插件没有出现在侧边栏？

A: 检查以下几点：
1. `plugins.json` 中的类名是否正确
2. 插件类是否实现了 `TextPlugin` 接口
3. 插件类是否在正确的包中
4. 编译是否成功

### Q: 打开文件失败？

A: 检查：
1. 文件路径是否正确
2. 文件是否存在
3. 是否有读取权限
4. 异常处理是否正确

### Q: 语法高亮不工作？

A: 检查：
1. CSS 文件是否正确加载
2. CSS 类名是否匹配
3. StyleSpans 是否正确构建
4. 是否在正确的时机应用样式

### Q: UI 布局混乱？

A: 检查：
1. 布局管理器是否正确使用
2. 组件大小设置是否合理
3. CSS 样式是否冲突

## 插件发布

### 1. 代码审查

- 确保代码质量
- 检查错误处理
- 验证功能完整性

### 2. 文档编写

- 编写插件使用说明
- 添加代码注释
- 更新插件列表文档

### 3. 测试

- 单元测试
- 集成测试
- 用户测试

### 4. 提交

- 提交代码到版本控制系统
- 更新 `plugins.json`
- 更新项目文档

## 参考资源

- [TextPlugin 接口文档](API.md#textplugin-接口)
- [现有插件实现](../DevTools/src/main/java/com/daicy/devtools/plugin/)
- [RichTextFX 文档](https://github.com/FXMisc/RichTextFX)
- [JavaFX 文档](https://openjfx.io/)

## 贡献

欢迎贡献新插件！请遵循以下步骤：

1. Fork 项目
2. 创建插件
3. 添加测试
4. 提交 Pull Request

