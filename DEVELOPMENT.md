# 开发指南

本文档介绍如何搭建开发环境、构建项目以及开发规范。

## 开发环境要求

### 必需软件

- **JDK**: 17 或更高版本
  - 推荐使用 OpenJDK 17 或 Oracle JDK 17
  - 验证安装: `java -version`
- **Maven**: 3.6 或更高版本
  - 验证安装: `mvn -version`
- **IDE**: 推荐使用 IntelliJ IDEA 或 Eclipse
  - IntelliJ IDEA 2021.3 或更高版本（推荐）
  - Eclipse 2021-12 或更高版本

### 可选工具

- **Git**: 用于版本控制
- **JavaFX Scene Builder**: 用于可视化编辑 FXML 文件（可选）

## 项目导入

### IntelliJ IDEA

1. 打开 IntelliJ IDEA
2. 选择 `File` -> `Open`
3. 选择项目根目录（包含 `pom.xml` 的目录）
4. 选择 "Open as Project"
5. 等待 Maven 自动导入依赖

### Eclipse

1. 打开 Eclipse
2. 选择 `File` -> `Import`
3. 选择 `Maven` -> `Existing Maven Projects`
4. 选择项目根目录
5. 点击 `Finish`

## 项目配置

### Maven 配置

项目使用父 POM 管理依赖版本，子模块继承父 POM 的配置。

#### 父 POM 位置
```
JavaFxDevTools/pom.xml
```

#### 子模块
- `core/pom.xml`
- `DevTools/pom.xml`
- `JavaFxEditor/pom.xml`

### IDE 配置

#### IntelliJ IDEA

1. **JDK 配置**
   - `File` -> `Project Structure` -> `Project`
   - 设置 `Project SDK` 为 JDK 17
   - 设置 `Project language level` 为 17

2. **Maven 配置**
   - `File` -> `Settings` -> `Build, Execution, Deployment` -> `Build Tools` -> `Maven`
   - 确认 Maven 路径和设置正确

3. **代码风格**
   - 推荐使用项目自带的代码格式化配置
   - `File` -> `Settings` -> `Editor` -> `Code Style` -> `Java`

#### Eclipse

1. **JDK 配置**
   - `Window` -> `Preferences` -> `Java` -> `Installed JREs`
   - 添加 JDK 17

2. **Maven 配置**
   - `Window` -> `Preferences` -> `Maven`
   - 确认 Maven 设置正确

## 构建项目

### 命令行构建

#### 编译所有模块

```bash
mvn clean compile
```

#### 打包所有模块

```bash
mvn clean package
```

#### 跳过测试打包

```bash
mvn clean package -DskipTests
```

#### 安装到本地仓库

```bash
mvn clean install
```

### 单独构建模块

#### 构建 core 模块

```bash
cd core
mvn clean package
```

#### 构建 DevTools 模块

```bash
cd DevTools
mvn clean package
```

#### 构建 JavaFxEditor 模块

```bash
cd JavaFxEditor
mvn clean package
```

## 运行项目

### 运行 DevTools

#### 方式一：使用 Maven 插件

```bash
cd DevTools
mvn javafx:run
```

#### 方式二：运行打包后的 JAR

```bash
java -jar DevTools/target/DevTools-1.0.0-SNAPSHOT-runnable.jar
```

#### 方式三：在 IDE 中运行

1. 打开 `DevTools/src/main/java/com/daicy/devtools/TextEditor.java`
2. 运行 `main` 方法

### 运行 JavaFxEditor

#### 方式一：使用 Maven 插件

```bash
cd JavaFxEditor
mvn javafx:run
```

#### 方式二：在 IDE 中运行

1. 打开 `JavaFxEditor/src/main/java/com/daicy/javafxeditor/TestEditor.java`
2. 运行 `main` 方法

## 代码规范

### 命名规范

- **类名**: 使用 PascalCase（大驼峰），如 `TextEditor`、`PluginManager`
- **方法名**: 使用 camelCase（小驼峰），如 `loadPlugins()`、`createNewTab()`
- **变量名**: 使用 camelCase，如 `pluginManager`、`codeEditor`
- **常量名**: 使用 UPPER_SNAKE_CASE，如 `OUTPUT_REFRESH_RATE`
- **包名**: 使用小写字母，如 `com.daicy.devtools`

### 代码风格

- **缩进**: 使用 4 个空格，不使用 Tab
- **行长度**: 建议不超过 120 个字符
- **大括号**: 使用 K&R 风格（开括号不换行）

```java
// 正确示例
public void method() {
    if (condition) {
        // code
    }
}

// 错误示例
public void method()
{
    if (condition)
    {
        // code
    }
}
```

### 注释规范

- **类注释**: 必须包含 `@author` 标签
- **方法注释**: 公共方法必须包含 JavaDoc 注释
- **复杂逻辑**: 必须添加行内注释说明

```java
/**
 * 插件管理器，负责插件的加载、安装和卸载
 * 
 * @author daicy
 */
public class PluginManager {
    /**
     * 加载所有配置的插件
     */
    public void loadPlugins() {
        // 实现代码
    }
}
```

### 异常处理

- 必须捕获并处理所有可能的异常
- 使用有意义的异常消息
- 记录异常日志（使用 `printStackTrace()` 或日志框架）

```java
try {
    // 可能抛出异常的操作
} catch (IOException e) {
    e.printStackTrace();
    // 或者使用日志框架
    // logger.error("文件操作失败", e);
}
```

## 测试

### 单元测试

项目使用 JUnit 5 进行单元测试。

#### 运行测试

```bash
mvn test
```

#### 运行特定模块的测试

```bash
cd <module-name>
mvn test
```

### 测试文件位置

测试文件应放在 `src/test/java` 目录下，包结构应与主代码保持一致。

## 调试技巧

### IntelliJ IDEA

1. **设置断点**: 在代码行号左侧点击
2. **调试运行**: 右键点击类或方法，选择 `Debug`
3. **查看变量**: 在调试窗口中查看变量值
4. **步进调试**: 使用 F8（Step Over）、F7（Step Into）、Shift+F8（Step Out）

### Eclipse

1. **设置断点**: 在代码行号左侧双击
2. **调试运行**: 右键点击类或方法，选择 `Debug As` -> `Java Application`
3. **查看变量**: 在 Variables 视图中查看变量值
4. **步进调试**: 使用 F6（Step Over）、F5（Step Into）、F7（Step Return）

### 常见问题

#### JavaFX 运行时模块路径问题

如果遇到 `java.lang.module.FindException` 错误，需要添加 JavaFX 模块路径：

```bash
--module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml
```

#### Maven 依赖下载失败

1. 检查网络连接
2. 配置 Maven 镜像（推荐使用阿里云镜像）
3. 清理 Maven 缓存: `mvn clean`

#### 插件加载失败

1. 检查 `plugins.json` 文件格式是否正确
2. 确认插件类名是否正确
3. 检查插件类是否实现了 `TextPlugin` 接口

## 版本控制

### Git 工作流

1. **创建分支**: `git checkout -b feature/your-feature-name`
2. **提交更改**: `git commit -m "描述你的更改"`
3. **推送分支**: `git push origin feature/your-feature-name`
4. **创建 Pull Request**

### 提交信息规范

- 使用清晰、简洁的提交信息
- 第一行简要描述更改（不超过 50 个字符）
- 如果需要，在空行后添加详细描述

```
示例：
添加 JSON 格式化插件

- 实现 JSON 格式化和验证功能
- 支持生成 JavaBean 代码
- 添加语法高亮支持
```

## 性能优化建议

1. **避免阻塞 UI 线程**: 长时间运行的操作应使用后台线程
2. **使用虚拟化组件**: 大列表使用 VirtualizedScrollPane
3. **延迟加载**: 插件和资源按需加载
4. **缓存机制**: 对频繁访问的数据使用缓存

## 依赖管理

### 添加新依赖

1. 在父 POM 的 `<dependencyManagement>` 中定义版本
2. 在子模块的 `pom.xml` 中声明依赖（不指定版本）

### 更新依赖版本

1. 在父 POM 中更新版本号
2. 运行 `mvn clean install` 更新依赖

## 构建可执行文件

### DevTools 打包

DevTools 模块已配置 `javapackager` 插件，可以打包为可执行文件：

```bash
cd DevTools
mvn clean package
```

打包后的文件位于 `DevTools/target/DevTools/` 目录。

### JavaFxEditor 打包

JavaFxEditor 使用 `javafx-maven-plugin`，可以打包为可执行文件：

```bash
cd JavaFxEditor
mvn clean package javafx:jlink
```

## 常见开发任务

### 添加新插件

1. 创建插件类实现 `TextPlugin` 接口
2. 在 `plugins.json` 中注册插件
3. 参考 [插件开发指南](PLUGIN_DEVELOPMENT.md)

### 修改 UI 布局

1. 编辑 FXML 文件（如 `MainWindow.fxml`）
2. 使用 JavaFX Scene Builder 可视化编辑（可选）
3. 在 Controller 中处理事件

### 添加新功能

1. 确定功能所属模块
2. 创建或修改相应的类
3. 添加必要的测试
4. 更新文档

## 获取帮助

- 查看项目文档
- 提交 Issue
- 联系项目维护者

