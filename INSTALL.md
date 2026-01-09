# 安装指南

本文档提供 JavaFxDevTools 的详细安装说明。

## 系统要求

- **Java**: JDK 17 或更高版本
- **操作系统**: Windows、macOS 或 Linux

## 安装方式

### 方式一：使用安装脚本（推荐）

安装脚本会自动下载、安装并配置应用程序。

#### Linux/macOS

1. **下载安装脚本**:
```bash
# 获取最新版本号
VERSION=$(curl -s https://api.github.com/repos/daichangya/JavaFxDevTools/releases/latest | grep '"tag_name":' | sed -E 's/.*"([^"]+)".*/\1/' | sed 's/v//')

# 下载安装脚本
curl -L -o install.sh https://github.com/daichangya/JavaFxDevTools/releases/download/v${VERSION}/install-${VERSION}.sh
```

2. **运行安装脚本**:
```bash
chmod +x install.sh
./install.sh
```

或者直接运行（自动获取最新版本）:
```bash
curl -L https://github.com/daichangya/JavaFxDevTools/releases/latest/download/install-$(curl -s https://api.github.com/repos/daichangya/JavaFxDevTools/releases/latest | grep '"tag_name":' | sed -E 's/.*"([^"]+)".*/\1/' | sed 's/v//').sh | bash
```

3. **添加到 PATH**（如果提示）:
```bash
# 添加到 ~/.bashrc 或 ~/.zshrc
export PATH="$PATH:$HOME/.local/bin"
```

4. **运行应用**:
```bash
devtools
javafxeditor
```

#### Windows

1. **下载安装脚本**:
   - 访问 [Releases](https://github.com/daichangya/JavaFxDevTools/releases) 页面
   - 下载 `install-{version}.bat`

2. **运行安装脚本**:
   - 双击 `install-{version}.bat` 文件
   - 按照提示完成安装

3. **添加到 PATH**（可选）:
   - 打开"系统属性" → "环境变量"
   - 编辑"Path"变量
   - 添加: `%USERPROFILE%\JavaFxDevTools\bin`

4. **运行应用**:
   - 打开命令提示符或 PowerShell
   - 运行: `devtools.bat` 或 `javafxeditor.bat`

### 方式二：手动安装

#### 步骤 1: 下载文件

访问 [Releases](https://github.com/daichangya/JavaFxDevTools/releases) 页面，下载以下文件：

- `DevTools-{version}.jar`
- `JavaFxEditor-{version}.jar`
- `DevTools-{version}.jar.sha256`（可选，用于验证）
- `JavaFxEditor-{version}.jar.sha256`（可选，用于验证）

#### 步骤 2: 验证文件（推荐）

**Linux/macOS**:
```bash
sha256sum -c DevTools-{version}.jar.sha256
sha256sum -c JavaFxEditor-{version}.jar.sha256
```

**Windows (PowerShell)**:
```powershell
$expected = Get-Content DevTools-{version}.jar.sha256
$actual = (Get-FileHash -Algorithm SHA256 DevTools-{version}.jar).Hash
if ($expected -eq $actual) { Write-Host "Checksum verified" } else { Write-Host "Checksum mismatch!" }
```

#### 步骤 3: 创建启动脚本

**Linux/macOS**:

创建 `devtools` 脚本:
```bash
#!/bin/bash
java -jar /path/to/DevTools-{version}.jar "$@"
```

创建 `javafxeditor` 脚本:
```bash
#!/bin/bash
java -jar /path/to/JavaFxEditor-{version}.jar "$@"
```

设置执行权限:
```bash
chmod +x devtools javafxeditor
```

**Windows**:

创建 `devtools.bat`:
```batch
@echo off
java -jar "C:\path\to\DevTools-{version}.jar" %*
```

创建 `javafxeditor.bat`:
```batch
@echo off
java -jar "C:\path\to\JavaFxEditor-{version}.jar" %*
```

#### 步骤 4: 运行应用

**Linux/macOS**:
```bash
./devtools
./javafxeditor
```

**Windows**:
```cmd
devtools.bat
javafxeditor.bat
```

### 方式三：使用原生安装包（macOS/Windows/Linux）

原生安装包包含完整的 JRE 运行时，**无需安装 JDK**。

#### macOS (.dmg)

1. **下载 DMG 文件**:
   - 访问 [Releases](https://github.com/daichangya/JavaFxDevTools/releases) 页面
   - 下载 `DevTools-{version}.dmg` 和 `JavaFxEditor-{version}.dmg`

2. **安装应用**:
   - 双击 DMG 文件
   - 将应用拖拽到 Applications 文件夹

3. **修复权限**（如果提示"已损坏，无法打开"）:
```bash
# 方法 1: 使用修复脚本
curl -L -o fix-macos-permissions.sh https://raw.githubusercontent.com/daichangya/JavaFxDevTools/main/scripts/fix-macos-permissions.sh
chmod +x fix-macos-permissions.sh
./fix-macos-permissions.sh

# 方法 2: 手动修复
xattr -cr /Applications/DevTools.app
xattr -cr /Applications/JavaFxEditor.app
```

4. **启动应用**:
   - 从 Launchpad 或 Applications 文件夹启动
   - 或使用 Spotlight 搜索应用名称

#### Windows (.msi)

1. **下载 MSI 文件**:
   - 访问 [Releases](https://github.com/daichangya/JavaFxDevTools/releases) 页面
   - 下载 `DevTools-{version}.msi` 和 `JavaFxEditor-{version}.msi`

2. **安装应用**:
   - 双击 MSI 文件
   - 按照安装向导完成安装

3. **启动应用**:
   - 从开始菜单启动
   - 或从安装目录运行

#### Linux (.deb / .rpm)

1. **下载安装包**:
   - 访问 [Releases](https://github.com/daichangya/JavaFxDevTools/releases) 页面
   - 下载 `.deb` (Debian/Ubuntu) 或 `.rpm` (RedHat/CentOS) 文件

2. **安装应用**:
```bash
# Debian/Ubuntu
sudo dpkg -i DevTools-{version}.deb
sudo dpkg -i JavaFxEditor-{version}.deb

# RedHat/CentOS
sudo rpm -i DevTools-{version}.rpm
sudo rpm -i JavaFxEditor-{version}.rpm
```

3. **启动应用**:
   - 从应用程序菜单启动
   - 或使用命令行: `devtools` / `javafxeditor`

## 验证安装

### 检查 Java 版本

```bash
java -version
```

应该显示 Java 17 或更高版本。

### 测试应用程序

运行应用程序，确认可以正常启动：

```bash
# Linux/macOS
devtools --help
javafxeditor --help

# Windows
devtools.bat --help
javafxeditor.bat --help
```

## 卸载

### Linux/macOS

```bash
# 删除安装目录
rm -rf ~/.local/share/javafxdevtools

# 删除启动脚本
rm ~/.local/bin/devtools
rm ~/.local/bin/javafxeditor
```

### Windows

1. 删除安装目录: `%USERPROFILE%\JavaFxDevTools`
2. 从 PATH 中移除 `%USERPROFILE%\JavaFxDevTools\bin`（如果已添加）

## 常见问题

### Q: macOS 提示 "已损坏，无法打开"

**A**: 这是 macOS 的 Gatekeeper 安全机制导致的。未签名的应用会被标记为"已损坏"。

**解决方法**（选择其一）：

1. **使用修复脚本**（推荐）:
```bash
# 下载修复脚本
curl -L -o fix-macos-permissions.sh https://raw.githubusercontent.com/daichangya/JavaFxDevTools/main/scripts/fix-macos-permissions.sh

# 运行修复脚本
chmod +x fix-macos-permissions.sh
./fix-macos-permissions.sh
```

2. **手动移除隔离属性**:
```bash
# 移除应用的隔离属性
xattr -cr /Applications/DevTools.app
xattr -cr /Applications/JavaFxEditor.app
```

3. **通过系统设置允许运行**:
   - 右键点击应用 → 选择"打开"
   - 在弹出对话框中点击"打开"
   - 或者：系统偏好设置 → 安全性与隐私 → 允许运行

**注意**: 如果是从 DMG 安装的，也需要移除 DMG 的隔离属性：
```bash
xattr -cr /path/to/DevTools-1.0.1.dmg
```

### Q: 提示 "Java is not installed"

**A**: 需要安装 JDK 17 或更高版本。

- **下载地址**: https://adoptium.net/
- **验证安装**: 运行 `java -version`

### Q: 提示 "Java 17 or higher is required"

**A**: 当前 Java 版本过低，需要升级到 Java 17+。

**检查当前版本**:
```bash
java -version
```

**升级 Java**:
- macOS: 使用 Homebrew: `brew install openjdk@17`
- Linux: 使用包管理器安装 openjdk-17
- Windows: 从 Adoptium 下载安装

### Q: 命令未找到 (command not found)

**A**: 启动脚本不在 PATH 中。

**Linux/macOS**:
```bash
# 添加到 PATH
export PATH="$PATH:$HOME/.local/bin"

# 永久添加（添加到 ~/.bashrc 或 ~/.zshrc）
echo 'export PATH="$PATH:$HOME/.local/bin"' >> ~/.bashrc
source ~/.bashrc
```

**Windows**:
- 将 `%USERPROFILE%\JavaFxDevTools\bin` 添加到系统 PATH

### Q: 如何更新到新版本？

**A**: 

1. **使用安装脚本**（推荐）:
   - 重新运行安装脚本，它会自动下载新版本

2. **手动更新**:
   - 下载新版本的 JAR 文件
   - 替换旧文件
   - 更新启动脚本中的版本号

### Q: 校验和验证失败

**A**: 文件可能下载不完整或损坏。

**解决方法**:
1. 重新下载文件
2. 确保网络连接稳定
3. 如果问题持续，检查 GitHub Releases 页面是否有更新

## 高级配置

### 自定义安装目录

**Linux/macOS**:
编辑安装脚本，修改 `INSTALL_DIR` 变量。

**Windows**:
编辑安装脚本，修改 `INSTALL_DIR` 变量。

### 添加桌面快捷方式

**Linux** (使用 .desktop 文件):
```ini
[Desktop Entry]
Name=DevTools
Exec=/path/to/devtools
Icon=/path/to/icon.png
Type=Application
Categories=Development;
```

**Windows**:
创建快捷方式，指向 `devtools.bat` 或 `javafxeditor.bat`。

**macOS**:
使用 Automator 创建应用程序。

## 相关文档

- [用户手册](USER_GUIDE.md) - 使用说明
- [打包指南](PACKAGING.md) - 打包和发布说明
- [开发指南](DEVELOPMENT.md) - 开发环境搭建

## 获取帮助

如果遇到问题：

1. 查看 [常见问题](#常见问题) 部分
2. 查看 [GitHub Issues](https://github.com/daichangya/JavaFxDevTools/issues)
3. 创建新的 Issue 描述问题

