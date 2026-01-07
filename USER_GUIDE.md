# 用户使用手册

本文档介绍如何使用 JavaFxDevTools 项目中的各个应用程序。

## DevTools 使用指南

DevTools 是一个插件化的文本编辑器，支持多种文件类型的编辑和处理。

### 启动应用

#### Windows

双击 `DevTools.exe` 或在命令行运行：

```bash
java -jar DevTools-1.0.0-SNAPSHOT-runnable.jar
```

#### macOS/Linux

```bash
java -jar DevTools-1.0.0-SNAPSHOT-runnable.jar
```

### 界面介绍

DevTools 主界面包含以下部分：

- **菜单栏**: 文件操作、设置、帮助菜单
- **侧边栏**: 插件按钮列表
- **标签页区域**: 显示打开的文件和插件界面

### 基本操作

#### 新建标签页

- **菜单**: `File` -> `New` 或快捷键 `Ctrl+N` (Windows/Linux) / `Cmd+N` (macOS)
- 创建一个新的空白标签页

#### 打开文件

- **菜单**: `File` -> `Open` 或快捷键 `Ctrl+O` (Windows/Linux) / `Cmd+O` (macOS)
- 选择插件后，点击打开文件按钮
- 注意：必须先选择一个插件才能打开文件

#### 保存文件

- **菜单**: `File` -> `Save` 或快捷键 `Ctrl+S` (Windows/Linux) / `Cmd+S` (macOS)
- 如果文件已保存过，直接保存到原文件
- 如果文件未保存，会弹出"另存为"对话框

#### 另存为

- **菜单**: `File` -> `Save As` 或快捷键 `Ctrl+Shift+S` (Windows/Linux) / `Cmd+Shift+S` (macOS)
- 选择保存位置和文件名

#### 退出应用

- **菜单**: `File` -> `Exit` 或快捷键 `Ctrl+Q` (Windows/Linux) / `Cmd+Q` (macOS)

### 插件使用

#### 选择插件

1. 在左侧边栏点击插件按钮
2. 插件界面会在新的标签页中打开

#### JSON 格式化插件

**功能**:
- JSON 格式化：将压缩的 JSON 格式化为易读的格式
- JSON 验证：检查 JSON 格式是否正确
- 生成 JavaBean：根据 JSON 结构生成 Java Bean 代码
- 语法高亮：自动高亮 JSON 关键字

**使用方法**:
1. 点击侧边栏的 JSON 插件按钮
2. 在输入框中输入或粘贴 JSON 文本
3. 点击"格式化"按钮进行格式化
4. 勾选"关键字转义"选项可以处理转义字符
5. 点击"生成JavaBean"按钮可以生成对应的 Java 类代码

**快捷键**:
- 格式化：无（点击按钮）
- 生成JavaBean：无（点击按钮）

**注意事项**:
- 如果 JSON 格式错误，会在输出区域显示错误信息，并在输入区域高亮错误位置
- 生成的 JavaBean 代码可以直接复制使用

#### Markdown 编辑器插件

**功能**:
- 实时预览：编辑 Markdown 时实时显示渲染结果
- 格式化工具：快速插入 Markdown 语法
- 双栏显示：左侧编辑，右侧预览

**使用方法**:
1. 点击侧边栏的 Markdown 插件按钮
2. 在左侧编辑区域输入 Markdown 文本
3. 右侧会自动显示渲染后的 HTML 预览
4. 使用工具栏按钮快速插入格式：
   - **B**: 加粗选中文本
   - **I**: 斜体选中文本
   - **H1**: 将选中文本设为一级标题

**快捷键**:
- 加粗：选中文本后点击 B 按钮
- 斜体：选中文本后点击 I 按钮
- 标题：选中文本后点击 H1 按钮

**注意事项**:
- 预览区域使用 WebView 渲染，支持大部分 Markdown 语法
- 实时预览可能会有轻微延迟

#### Hosts 文件管理插件

**功能**:
- 编辑系统 Hosts 文件
- 语法高亮：自动高亮 IP 地址和注释
- 自动保存：支持直接保存到系统 Hosts 文件
- 行号显示：显示行号便于定位

**使用方法**:
1. 点击侧边栏的 Hosts 插件按钮
2. 插件会自动加载系统 Hosts 文件内容
3. 编辑 Hosts 文件内容
4. 保存文件：
   - 如果使用默认路径，会直接保存到系统 Hosts 文件
   - 如果指定路径，会保存到指定文件

**快捷键**:
- 保存：`Ctrl+S` (Windows/Linux) / `Cmd+S` (macOS)

**注意事项**:
- 修改系统 Hosts 文件可能需要管理员权限
- 修改前建议备份原始 Hosts 文件
- IP 地址会自动高亮显示

### 设置

#### 插件设置

1. 点击菜单 `Settings` -> `Plugins`
2. 在插件设置窗口中：
   - 查看已安装和未安装的插件列表
   - 点击"安装插件"按钮安装插件
   - 点击"卸载插件"按钮卸载插件

#### 语言设置

1. 点击菜单 `Settings` -> `Language`
2. 在语言设置窗口中选择语言：
   - English（英文）
   - 中文（简体中文）
3. 选择后界面会立即更新

### 帮助

#### 关于

点击菜单 `Help` -> `About` 查看应用信息。

## JavaFxEditor 使用指南

JavaFxEditor 是一个可定制的代码编辑器，基于策略模式实现。

### 启动应用

#### 使用 Maven

```bash
cd JavaFxEditor
mvn javafx:run
```

#### 运行 JAR 文件

```bash
java -jar JavaFxEditor-1.0.0-SNAPSHOT.jar
```

### 界面介绍

JavaFxEditor 主界面包含：

- **菜单栏**: 文件、编辑、帮助菜单
- **工具栏**: 常用操作按钮
- **编辑区域**: 代码编辑区域

### 基本操作

#### 新建文档

- **菜单**: `File` -> `New` 或快捷键 `Ctrl+N` (Windows/Linux) / `Cmd+N` (macOS)
- **工具栏**: 点击新建按钮

#### 打开文档

- **菜单**: `File` -> `Open...` 或快捷键 `Ctrl+O` (Windows/Linux) / `Cmd+O` (macOS)
- **工具栏**: 点击打开按钮
- 选择要打开的文件

#### 保存文档

- **菜单**: `File` -> `Save` 或快捷键 `Ctrl+S` (Windows/Linux) / `Cmd+S` (macOS)
- **工具栏**: 点击保存按钮
- 如果文档未保存过，会弹出"另存为"对话框

#### 另存为

- **菜单**: `File` -> `Save as...` 或快捷键 `Ctrl+Shift+S` (Windows/Linux) / `Cmd+Shift+S` (macOS)
- **工具栏**: 点击另存为按钮

#### 退出程序

- **菜单**: `File` -> `Exit`

### 编辑功能

#### 撤销/重做

- **撤销**: `Edit` -> `Undo` 或快捷键 `Ctrl+Z` (Windows/Linux) / `Cmd+Z` (macOS)
- **重做**: `Edit` -> `Redo` 或快捷键 `Ctrl+Y` (Windows/Linux) / `Cmd+Y` (macOS)
- **工具栏**: 点击撤销/重做按钮

#### 剪切/复制/粘贴

- **剪切**: `Edit` -> `Cut` 或快捷键 `Ctrl+X` (Windows/Linux) / `Cmd+X` (macOS)
- **复制**: `Edit` -> `Copy` 或快捷键 `Ctrl+C` (Windows/Linux) / `Cmd+C` (macOS)
- **粘贴**: `Edit` -> `Paste` 或快捷键 `Ctrl+V` (Windows/Linux) / `Cmd+V` (macOS)
- **工具栏**: 点击相应按钮

#### 全选

- **菜单**: `Edit` -> `Select all` 或快捷键 `Ctrl+A` (Windows/Linux) / `Cmd+A` (macOS)

### 帮助功能

#### 在线参考

- **菜单**: `Help` -> `Online reference` 或快捷键 `F1`
- 在浏览器中打开在线参考文档

#### 关于

- **菜单**: `Help` -> `About...` 或快捷键 `F12`
- 显示应用信息和版本

### 窗口标题

窗口标题会显示：
- 应用名称
- 当前打开的文件名（如果有）
- 修改标记（`*`）：表示文档已修改但未保存

### 快捷键汇总

#### DevTools

| 功能 | Windows/Linux | macOS |
|------|---------------|-------|
| 新建 | Ctrl+N | Cmd+N |
| 打开 | Ctrl+O | Cmd+O |
| 保存 | Ctrl+S | Cmd+S |
| 另存为 | Ctrl+Shift+S | Cmd+Shift+S |
| 退出 | Ctrl+Q | Cmd+Q |

#### JavaFxEditor

| 功能 | Windows/Linux | macOS |
|------|---------------|-------|
| 新建 | Ctrl+N | Cmd+N |
| 打开 | Ctrl+O | Cmd+O |
| 保存 | Ctrl+S | Cmd+S |
| 另存为 | Ctrl+Shift+S | Cmd+Shift+S |
| 撤销 | Ctrl+Z | Cmd+Z |
| 重做 | Ctrl+Y | Cmd+Y |
| 剪切 | Ctrl+X | Cmd+X |
| 复制 | Ctrl+C | Cmd+C |
| 粘贴 | Ctrl+V | Cmd+V |
| 全选 | Ctrl+A | Cmd+A |
| 在线参考 | F1 | F1 |
| 关于 | F12 | F12 |

## 常见问题

### DevTools

**Q: 为什么无法打开文件？**

A: 请先选择一个插件。在侧边栏点击插件按钮，然后再尝试打开文件。

**Q: 如何添加新插件？**

A: 目前插件需要通过配置文件添加。请参考 [插件开发指南](PLUGIN_DEVELOPMENT.md) 了解如何开发插件。

**Q: 修改 Hosts 文件后没有生效？**

A: 修改系统 Hosts 文件可能需要管理员权限。请以管理员身份运行应用，或者手动刷新 DNS 缓存。

**Q: JSON 格式化失败？**

A: 请检查 JSON 格式是否正确。错误信息会显示在输出区域，并高亮错误位置。

### JavaFxEditor

**Q: 如何更改编辑器主题？**

A: 目前主题通过 CSS 文件配置。可以修改 `EditorStrategy` 中的 `getSyntaxCss()` 方法返回的 CSS 文件。

**Q: 支持哪些文件类型？**

A: JavaFxEditor 是一个通用文本编辑器，可以编辑任何文本文件。语法高亮取决于配置的 CSS 文件。

**Q: 如何自定义编辑器行为？**

A: 实现 `AppStrategy` 接口并传入 `JavaFxEditor` 构造函数。参考 [架构设计文档](ARCHITECTURE.md) 了解策略模式的使用。

## 技巧和提示

### DevTools

1. **快速切换插件**: 点击侧边栏的插件按钮可以快速切换插件
2. **多标签页编辑**: 可以同时打开多个文件，在不同标签页间切换
3. **JSON 格式化技巧**: 如果 JSON 包含转义字符，勾选"关键字转义"选项可以自动处理
4. **Markdown 实时预览**: 编辑 Markdown 时，右侧预览会自动更新

### JavaFxEditor

1. **修改标记**: 窗口标题中的 `*` 表示文档已修改但未保存
2. **撤销历史**: 编辑器支持完整的撤销/重做历史
3. **自动保存提示**: 关闭窗口时，如果有未保存的修改，会提示保存

## 反馈和支持

如果您在使用过程中遇到问题或有改进建议，请：

1. 查看项目文档
2. 提交 Issue
3. 联系项目维护者

