# 打包指南

本文档介绍如何将 JavaFxDevTools 项目打包为可执行程序。

## 打包方式

项目支持多种打包方式：

1. **Fat JAR（推荐）**: 包含所有依赖的可执行 JAR 文件
2. **原生应用**: 使用 jpackage 打包为平台特定的可执行文件（需要 JDK 14+）
3. **jlink 镜像**: 创建自定义 Java 运行时镜像

## 环境要求

- **JDK 17** 或更高版本（必需）
- **Maven 3.6+**
- 对于原生应用打包，需要 JDK 14+ 和 jpackage 工具

## Fat JAR 打包（推荐）

### DevTools

Fat JAR 包含所有依赖，可以直接运行，无需额外配置类路径。

```bash
cd DevTools
mvn clean package -DskipTests
```

**输出文件**:
- `target/DevTools-1.0.0-SNAPSHOT-runnable.jar` - 可执行 JAR 文件

**运行方式**:
```bash
java -jar target/DevTools-1.0.0-SNAPSHOT-runnable.jar
```

### JavaFxEditor

```bash
cd JavaFxEditor
mvn clean package -DskipTests
```

**输出文件**:
- `target/JavaFxEditor-1.0.0-SNAPSHOT-runnable.jar` - 可执行 JAR 文件

**运行方式**:
```bash
java -jar target/JavaFxEditor-1.0.0-SNAPSHOT-runnable.jar
```

## 原生应用打包

### 使用 jpackage（JDK 14+）

jpackage 是 JDK 14+ 提供的工具，可以创建平台特定的安装包。

#### DevTools

```bash
cd DevTools
mvn clean package -DskipTests

# 使用 jpackage 打包（macOS）
jpackage --input target \
  --name DevTools \
  --main-jar DevTools-1.0.0-SNAPSHOT-runnable.jar \
  --main-class com.daicy.devtools.TextEditor \
  --type dmg \
  --dest dist

# Windows
jpackage --input target \
  --name DevTools \
  --main-jar DevTools-1.0.0-SNAPSHOT-runnable.jar \
  --main-class com.daicy.devtools.TextEditor \
  --type msi \
  --dest dist

# Linux
jpackage --input target \
  --name DevTools \
  --main-jar DevTools-1.0.0-SNAPSHOT-runnable.jar \
  --main-class com.daicy.devtools.TextEditor \
  --type deb \
  --dest dist
```

#### JavaFxEditor

```bash
cd JavaFxEditor
mvn clean package -DskipTests

# 使用 jpackage 打包
jpackage --input target \
  --name JavaFxEditor \
  --main-jar JavaFxEditor-1.0.0-SNAPSHOT-runnable.jar \
  --main-class com.daicy.javafxeditor.TestEditor \
  --type dmg \
  --dest dist
```

### 使用 javapackager 插件（已配置）

DevTools 已配置 `javapackager` 插件，会在 `package` 阶段自动打包：

```bash
cd DevTools
mvn clean package -DskipTests
```

**输出位置**: `target/DevTools/` 目录

**注意**: 
- 插件会自动检测平台（`platform=auto`）
- 在 macOS 上会生成 `.app` 或 `.dmg`
- 在 Windows 上会生成 `.exe` 或安装程序
- 在 Linux 上会生成相应的包格式

## jlink 镜像打包

使用 jlink 创建自定义运行时镜像，只包含应用需要的模块。

### JavaFxEditor

```bash
cd JavaFxEditor
mvn clean package javafx:jlink
```

**输出位置**: `target/app/` 目录

**运行方式**:
```bash
target/app/bin/app
```

## 打包配置说明

### DevTools 打包配置

- **maven-shade-plugin**: 创建 Fat JAR
- **javapackager**: 创建原生应用（可选）

### JavaFxEditor 打包配置

- **maven-shade-plugin**: 创建 Fat JAR
- **javafx-maven-plugin**: 支持 jlink 打包

## 常见问题

### Q: 为什么打包失败？

A: 检查以下几点：
1. 确保使用 JDK 17 或更高版本
2. 检查 JAVA_HOME 环境变量
3. 运行 `mvn -version` 确认 Maven 使用的 Java 版本

### Q: 如何切换 Java 版本？

**macOS**:
```bash
# 使用 jenv（如果已安装）
jenv local 17

# 或设置 JAVA_HOME
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

**Linux**:
```bash
# 使用 update-alternatives
sudo update-alternatives --config java
```

**Windows**:
- 在系统环境变量中设置 `JAVA_HOME` 指向 JDK 17

### Q: Fat JAR 文件太大？

A: 这是正常的，因为包含了所有依赖。可以考虑：
1. 使用 jlink 创建自定义运行时
2. 使用 jpackage 创建原生应用
3. 排除不必要的依赖

### Q: 如何为 Release 打包？

A: 建议流程：
1. 更新版本号
2. 运行 `mvn clean package -DskipTests`
3. 将 Fat JAR 上传到 GitHub Releases
4. 可选：创建平台特定的安装包

## 发布到 GitHub Releases

### 自动发布（推荐）

项目已配置 GitHub Actions workflow，在创建 Release 时会自动：

1. **自动构建**: 构建所有模块并生成可执行 JAR
2. **自动重命名**: 将 SNAPSHOT 版本号替换为 Release 版本号
3. **生成校验和**: 自动生成 SHA256 校验和文件
4. **创建安装脚本**: 自动生成安装脚本
5. **自动上传**: 将所有文件上传到 Release Assets

**使用方法**:
1. 在 GitHub 上创建新的 Release（带版本标签，如 `v1.0.0`）
2. 发布 Release
3. GitHub Actions 会自动运行并上传所有文件

**上传的文件包括**:
- `DevTools-{version}.jar` - DevTools 可执行 JAR
- `DevTools-{version}.jar.sha256` - 校验和文件
- `JavaFxEditor-{version}.jar` - JavaFxEditor 可执行 JAR
- `JavaFxEditor-{version}.jar.sha256` - 校验和文件
- `install-{version}.sh` - Linux/macOS 安装脚本
- `install-{version}.bat` - Windows 安装脚本

### 手动发布

如果需要手动上传文件：

1. **构建所有模块**:
```bash
mvn clean package -DskipTests
```

2. **收集打包文件**:
- `DevTools/target/DevTools-1.0.0-SNAPSHOT-runnable.jar`
- `JavaFxEditor/target/JavaFxEditor-1.0.0-SNAPSHOT-runnable.jar`

3. **生成校验和**:
```bash
# Linux/macOS
sha256sum DevTools/target/DevTools-1.0.0-SNAPSHOT-runnable.jar > DevTools-1.0.0.jar.sha256
sha256sum JavaFxEditor/target/JavaFxEditor-1.0.0-SNAPSHOT-runnable.jar > JavaFxEditor-1.0.0.jar.sha256

# Windows (PowerShell)
Get-FileHash -Algorithm SHA256 DevTools\target\DevTools-1.0.0-SNAPSHOT-runnable.jar | Out-File DevTools-1.0.0.jar.sha256
Get-FileHash -Algorithm SHA256 JavaFxEditor\target\JavaFxEditor-1.0.0-SNAPSHOT-runnable.jar | Out-File JavaFxEditor-1.0.0.jar.sha256
```

4. **上传到 GitHub Releases**:
   - 访问 Releases 页面
   - 编辑或创建 Release
   - 上传文件到 Assets

### 验证下载的文件

下载文件后，可以验证校验和：

**Linux/macOS**:
```bash
sha256sum -c DevTools-1.0.0.jar.sha256
sha256sum -c JavaFxEditor-1.0.0.jar.sha256
```

**Windows (PowerShell)**:
```powershell
$expected = Get-Content DevTools-1.0.0.jar.sha256
$actual = (Get-FileHash -Algorithm SHA256 DevTools-1.0.0.jar).Hash
if ($expected -eq $actual) { Write-Host "Checksum verified" }
```

## 最佳实践

1. **版本号管理**: 发布前更新版本号
2. **测试**: 打包后测试可执行文件
3. **文档**: 在 Release 说明中提供下载和使用说明
4. **多平台**: 为不同平台提供相应的打包文件

## 多平台原生应用打包

### 使用 jpackage（需要 JDK 14+）

jpackage 可以创建平台特定的安装包，包含 JRE，用户无需安装 Java。

#### 在 GitHub Actions 中自动构建

Release workflow 支持多平台构建（可选）。要启用多平台构建，需要：

1. 在 workflow 中添加矩阵策略
2. 为每个平台配置 jpackage 命令
3. 上传平台特定的安装包

#### 本地构建多平台应用

**macOS**:
```bash
cd DevTools
mvn clean package -DskipTests
jpackage --input target \
  --name DevTools \
  --main-jar DevTools-1.0.0-SNAPSHOT-runnable.jar \
  --main-class com.daicy.devtools.TextEditor \
  --type dmg \
  --dest dist \
  --java-options '--enable-preview'
```

**Windows**:
```cmd
cd DevTools
mvn clean package -DskipTests
jpackage --input target ^
  --name DevTools ^
  --main-jar DevTools-1.0.0-SNAPSHOT-runnable.jar ^
  --main-class com.daicy.devtools.TextEditor ^
  --type msi ^
  --dest dist
```

**Linux**:
```bash
cd DevTools
mvn clean package -DskipTests
jpackage --input target \
  --name DevTools \
  --main-jar DevTools-1.0.0-SNAPSHOT-runnable.jar \
  --main-class com.daicy.devtools.TextEditor \
  --type deb \
  --dest dist
```

## 相关文档

- [开发指南](DEVELOPMENT.md) - 构建和开发说明
- [README](README.md) - 项目总览

