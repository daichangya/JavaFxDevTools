# API 文档

本文档介绍 JavaFxDevTools 项目的核心 API 接口。

## TextPlugin 接口

插件系统的核心接口，所有插件必须实现此接口。

### 包路径

```
com.daicy.devtools.TextPlugin
```

### 接口定义

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

### 方法说明

#### `getContentPane()`

获取插件的 UI 面板。

**返回**: `Pane` - 插件的根面板，包含所有 UI 组件

**说明**: 此方法返回的 Pane 将显示在标签页中。插件应该在此方法中创建并返回自己的 UI 组件。

**示例**:
```java
@Override
public Pane getContentPane() {
    VBox vbox = new VBox();
    // 添加 UI 组件
    return vbox;
}
```

#### `getDefaultPath()`

获取插件的默认文件路径。

**返回**: `Path` - 默认文件路径，如果不需要默认路径，返回 `null`

**说明**: 如果插件有默认打开的文件（如 Hosts 文件），可以在此方法中返回。当用户点击插件按钮时，如果返回了默认路径，会自动打开该文件。

**示例**:
```java
@Override
public Path getDefaultPath() {
    return Path.of("/etc/hosts"); // Hosts 文件路径
}
```

#### `open(String filePath)`

打开文件。

**参数**:
- `filePath` (String): 要打开的文件路径

**说明**: 当用户通过"打开文件"功能选择文件时，会调用此方法。插件应该在此方法中读取文件内容并显示在 UI 中。

**示例**:
```java
@Override
public void open(String filePath) {
    try {
        String content = Files.readString(Paths.get(filePath));
        setContent(content);
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

#### `save(String filePath)`

保存文件。

**参数**:
- `filePath` (String): 要保存的文件路径

**说明**: 当用户保存文件时，会调用此方法。插件应该在此方法中将当前内容保存到指定文件。

**示例**:
```java
@Override
public void save(String filePath) {
    try {
        String content = getContent();
        Files.write(Paths.get(filePath), content.getBytes());
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

#### `getContent()`

获取当前内容。

**返回**: `String` - 当前编辑的内容

**说明**: 返回插件当前显示或编辑的内容。用于获取用户输入的数据。

**示例**:
```java
@Override
public String getContent() {
    return textArea.getText();
}
```

#### `setContent(String content)`

设置内容。

**参数**:
- `content` (String): 要设置的内容

**说明**: 设置插件显示的内容。通常用于打开文件时设置初始内容。

**示例**:
```java
@Override
public void setContent(String content) {
    textArea.setText(content);
}
```

## PluginManager 类

插件管理器，负责插件的加载、安装和卸载。

### 包路径

```
com.daicy.devtools.PluginManager
```

### 类定义

```java
class PluginManager {
    // 私有构造函数，通过静态方法或工厂方法创建实例
}
```

### 主要方法

#### `loadPlugins()`

加载所有配置的插件。

**说明**: 从 `plugins.json` 配置文件中读取插件列表，通过反射加载插件类并创建实例。

**使用示例**:
```java
PluginManager pluginManager = new PluginManager();
pluginManager.loadPlugins();
```

#### `installPlugin(TextPlugin plugin)`

安装插件。

**参数**:
- `plugin` (TextPlugin): 要安装的插件实例

**说明**: 将插件添加到已安装插件列表，并更新安装状态。

**使用示例**:
```java
TextPlugin plugin = new JsonFormatPlugin();
pluginManager.installPlugin(plugin);
```

#### `uninstallPlugin(TextPlugin plugin)`

卸载插件。

**参数**:
- `plugin` (TextPlugin): 要卸载的插件实例

**说明**: 从已安装插件列表中移除插件，并更新安装状态。

**使用示例**:
```java
pluginManager.uninstallPlugin(plugin);
```

#### `getPlugins()`

获取所有已安装的插件。

**返回**: `List<TextPlugin>` - 已安装的插件列表

**使用示例**:
```java
List<TextPlugin> plugins = pluginManager.getPlugins();
for (TextPlugin plugin : plugins) {
    System.out.println(plugin.getClass().getName());
}
```

#### `isPluginInstalled(Class<? extends TextPlugin> pluginClass)`

检查插件是否已安装。

**参数**:
- `pluginClass` (Class<? extends TextPlugin>): 插件类

**返回**: `boolean` - 如果插件已安装返回 `true`，否则返回 `false`

**使用示例**:
```java
boolean installed = pluginManager.isPluginInstalled(JsonFormatPlugin.class);
```

#### `getAvailablePluginClassNames()`

获取所有可用插件的类名列表。

**返回**: `ObservableList<String>` - 可用插件的类名列表

**说明**: 返回配置文件中定义的所有插件类名，无论是否已安装。

#### `getPluginInstallationStatus()`

获取插件安装状态映射。

**返回**: `Map<String, Boolean>` - 插件类名到安装状态的映射

**说明**: 返回一个 Map，键是插件类名，值是安装状态（`true` 表示已安装，`false` 表示未安装）。

## AppStrategy 接口

编辑器策略接口，用于定制编辑器行为。

### 包路径

```
com.daicy.javafxeditor.AppStrategy
```

### 接口定义

```java
public interface AppStrategy {
    String getTitle();
    FileChooser createSourceFileChooser();
    CodeArea createCodeEditor();
    File getSavedSourceFile(FileChooser sourceFileChooser, File selectedFile);
    boolean settingsSupported();
    void showSettings();
    URL getSyntaxCss();
    void showOnlineReference();
    void showAboutWindow();
}
```

### 方法说明

#### `getTitle()`

获取应用标题。

**返回**: `String` - 应用标题

**说明**: 返回的标题会显示在窗口标题栏中。

#### `createSourceFileChooser()`

创建源文件选择器。

**返回**: `FileChooser` - 文件选择器实例

**说明**: 创建用于打开和保存源文件的文件选择器。可以配置文件过滤器等选项。

**示例**:
```java
@Override
public FileChooser createSourceFileChooser() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.getExtensionFilters().add(
        new FileChooser.ExtensionFilter("Java files", "*.java")
    );
    return fileChooser;
}
```

#### `createCodeEditor()`

创建代码编辑器。

**返回**: `CodeArea` - 代码编辑器实例

**说明**: 创建并配置代码编辑器。可以设置初始内容、样式等。

**示例**:
```java
@Override
public CodeArea createCodeEditor() {
    CodeArea codeEditor = new CodeArea();
    codeEditor.getUndoManager().forgetHistory();
    return codeEditor;
}
```

#### `getSavedSourceFile(FileChooser sourceFileChooser, File selectedFile)`

获取实际保存的源文件。

**参数**:
- `sourceFileChooser` (FileChooser): 文件选择器
- `selectedFile` (File): 用户选择的文件

**返回**: `File` - 实际保存的文件

**说明**: 在用户保存文件时调用，可以修改文件名（如添加扩展名）。

**示例**:
```java
@Override
public File getSavedSourceFile(FileChooser sourceFileChooser, File selectedFile) {
    // 如果没有扩展名，添加默认扩展名
    if (!selectedFile.getName().contains(".")) {
        return new File(selectedFile.getParent(), selectedFile.getName() + ".txt");
    }
    return selectedFile;
}
```

#### `settingsSupported()`

是否支持设置功能。

**返回**: `boolean` - 如果支持设置返回 `true`，否则返回 `false`

**说明**: 如果返回 `true`，菜单中会显示"设置"菜单项。

#### `showSettings()`

显示设置对话框。

**说明**: 如果 `settingsSupported()` 返回 `true`，用户点击设置菜单时会调用此方法。

#### `getSyntaxCss()`

获取语法高亮 CSS 文件 URL。

**返回**: `URL` - CSS 文件的 URL

**说明**: 返回用于语法高亮的 CSS 文件路径。

**示例**:
```java
@Override
public URL getSyntaxCss() {
    return getClass().getResource("syntax.css");
}
```

#### `showOnlineReference()`

显示在线参考。

**说明**: 打开在线参考文档（通常在浏览器中）。

**示例**:
```java
@Override
public void showOnlineReference() {
    DesktopUtils.openBrowser("https://example.com/docs", null);
}
```

#### `showAboutWindow()`

显示关于窗口。

**说明**: 显示应用的关于对话框，包含版本信息、作者等。

**示例**:
```java
@Override
public void showAboutWindow() {
    AboutBox aboutBox = new AboutBox();
    aboutBox.showAndWait();
}
```

## Workspace 抽象类

工作空间抽象类，管理文档状态和文件操作。

### 包路径

```
com.daicy.javafxeditor.Workspace
```

### 类定义

```java
public abstract class Workspace {
    // 抽象方法
    protected abstract boolean doNew();
    protected abstract boolean doOpen(File sourceFile);
    protected abstract boolean doSave(File targetFile);
    
    // 公共方法
    public boolean newDocument();
    public boolean openDocument();
    public boolean saveDocument();
    public boolean saveAsDocument();
    public void closeStage();
}
```

### 主要方法

#### `newDocument()`

创建新文档。

**返回**: `boolean` - 如果成功返回 `true`，否则返回 `false`

**说明**: 创建新文档，清空编辑器内容。如果当前文档有未保存的修改，会提示用户保存。

#### `openDocument()`

打开文档。

**返回**: `boolean` - 如果成功返回 `true`，否则返回 `false`

**说明**: 打开文件选择器，让用户选择要打开的文件。如果当前文档有未保存的修改，会提示用户保存。

#### `saveDocument()`

保存文档。

**返回**: `boolean` - 如果成功返回 `true`，否则返回 `false`

**说明**: 保存当前文档。如果文档未保存过，会调用 `saveAsDocument()`。

#### `saveAsDocument()`

另存为文档。

**返回**: `boolean` - 如果成功返回 `true`，否则返回 `false`

**说明**: 打开文件选择器，让用户选择保存位置。

#### `closeStage()`

关闭窗口。

**说明**: 关闭应用窗口。如果文档有未保存的修改，会提示用户保存。

### 抽象方法

子类必须实现以下抽象方法：

#### `doNew()`

执行创建新文档的操作。

**返回**: `boolean` - 如果成功返回 `true`，否则返回 `false`

#### `doOpen(File sourceFile)`

执行打开文件的操作。

**参数**:
- `sourceFile` (File): 要打开的文件

**返回**: `boolean` - 如果成功返回 `true`，否则返回 `false`

#### `doSave(File targetFile)`

执行保存文件的操作。

**参数**:
- `targetFile` (File): 要保存到的文件

**返回**: `boolean` - 如果成功返回 `true`，否则返回 `false`

## JavaFxWorkspace 类

Workspace 的具体实现，用于 JavaFX 编辑器。

### 包路径

```
com.daicy.javafxeditor.JavaFxWorkspace
```

### 类定义

```java
public class JavaFxWorkspace extends Workspace {
    private final CodeArea codeEditor;
    
    public JavaFxWorkspace(Stage stage, FileChooser documentFileChooser, CodeArea codeEditor);
    
    @Override
    protected boolean doNew();
    
    @Override
    protected boolean doOpen(File sourceFile);
    
    @Override
    protected boolean doSave(File targetFile);
}
```

### 构造函数

#### `JavaFxWorkspace(Stage stage, FileChooser documentFileChooser, CodeArea codeEditor)`

创建 JavaFxWorkspace 实例。

**参数**:
- `stage` (Stage): JavaFX 主窗口
- `documentFileChooser` (FileChooser): 文件选择器
- `codeEditor` (CodeArea): 代码编辑器

## PluginInfo 类

插件信息类，用于从 JSON 配置文件读取插件信息。

### 包路径

```
com.daicy.devtools.PluginInfo
```

### 类定义

```java
public class PluginInfo {
    private String pluginClass;
    
    public String getPluginClass();
    public void setPluginClass(String pluginClass);
}
```

### 字段说明

#### `pluginClass`

插件类的全限定名。

**类型**: `String`

**示例**: `"com.daicy.devtools.plugin.JsonFormatPlugin"`

## 工具类

### DesktopUtils

桌面工具类，提供跨平台的桌面操作。

### 包路径

```
com.daicy.javafxeditor.desktop.DesktopUtils
```

### 主要方法

#### `openBrowser(String url, String... args)`

在默认浏览器中打开 URL。

**参数**:
- `url` (String): 要打开的 URL
- `args` (String...): 可选参数

**说明**: 跨平台方法，在不同操作系统上使用相应的命令打开浏览器。

## 使用示例

### 创建自定义插件

```java
public class MyPlugin implements TextPlugin {
    private final VBox contentPane;
    private final TextArea textArea;
    
    public MyPlugin() {
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
        return null;
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

### 创建自定义策略

```java
public class MyEditorStrategy implements AppStrategy {
    @Override
    public String getTitle() {
        return "My Editor";
    }
    
    @Override
    public FileChooser createSourceFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text files", "*.txt")
        );
        return fileChooser;
    }
    
    @Override
    public CodeArea createCodeEditor() {
        CodeArea codeEditor = new CodeArea();
        codeEditor.getUndoManager().forgetHistory();
        return codeEditor;
    }
    
    // 实现其他方法...
}
```

