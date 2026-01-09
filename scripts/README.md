# 脚本说明文档

本文档说明 `scripts` 目录下所有脚本的用途和使用方法。

## 目录结构

```
scripts/
├── install/          # 安装脚本
│   ├── from-github.sh    # 从 GitHub Releases 安装（Linux/macOS）
│   ├── from-github.bat   # 从 GitHub Releases 安装（Windows）
│   └── local.sh          # 本地安装（从项目构建）
├── launch/          # 启动脚本
│   ├── devtools.sh       # 启动 DevTools（自动检测位置）
│   └── javafxeditor.sh  # 启动 JavaFxEditor（自动检测位置）
├── utils/           # 工具脚本
│   ├── fix-macos-permissions.sh  # 修复 macOS 权限问题
│   └── test-install.sh           # 测试安装是否成功
└── dev/             # 开发脚本
    └── test-package.sh   # 测试打包流程
```

## 安装脚本 (install/)

### from-github.sh / from-github.bat

从 GitHub Releases 下载并安装应用。

**使用场景**: 最终用户从 GitHub 下载安装

**使用方法**:
```bash
# Linux/macOS
curl -L https://github.com/daichangya/JavaFxDevTools/releases/latest/download/install-1.0.1.sh | bash

# Windows
# 下载 install-1.0.1.bat 并双击运行
```

**功能**:
- 自动检测最新版本
- 下载 JAR 文件
- 创建启动脚本
- 配置 PATH

### local.sh

从本地项目构建的 JAR 文件安装到用户目录。

**使用场景**: 开发者本地安装测试

**使用方法**:
```bash
# 先构建项目
mvn clean package -DskipTests

# 运行安装脚本
./scripts/install/local.sh
```

**功能**:
- 检查 Java 版本
- 复制 JAR 文件到 `~/.local/share/javafxdevtools/`
- 创建启动脚本到 `~/.local/bin/`
- 配置 PATH

## 启动脚本 (launch/)

### devtools.sh

统一的 DevTools 启动脚本，自动检测 JAR 文件位置。

**使用场景**: 启动 DevTools 应用

**使用方法**:
```bash
# 从项目目录运行
./scripts/launch/devtools.sh

# 或添加到 PATH 后
devtools
```

**自动检测逻辑**:
1. 优先查找已安装版本 (`~/.local/share/javafxdevtools/`)
2. 回退到项目构建版本 (`DevTools/target/`)
3. 支持多个版本号（1.0.1, 1.0.0-SNAPSHOT）

### javafxeditor.sh

统一的 JavaFxEditor 启动脚本，自动检测 JAR 文件位置。

**使用场景**: 启动 JavaFxEditor 应用

**使用方法**:
```bash
# 从项目目录运行
./scripts/launch/javafxeditor.sh

# 或添加到 PATH 后
javafxeditor
```

**自动检测逻辑**: 同 `devtools.sh`

## 工具脚本 (utils/)

### fix-macos-permissions.sh

修复 macOS 应用的权限问题（解决"已损坏，无法打开"错误）。

**使用场景**: macOS 用户遇到权限问题

**使用方法**:
```bash
./scripts/utils/fix-macos-permissions.sh
```

**功能**:
- 移除应用的隔离属性
- 修复 `/Applications/DevTools.app` 和 `/Applications/JavaFxEditor.app`

### test-install.sh

测试安装是否成功。

**使用场景**: 验证安装结果

**使用方法**:
```bash
./scripts/utils/test-install.sh
```

**功能**:
- 检查安装目录和文件
- 验证启动脚本
- 测试应用启动

## 开发脚本 (dev/)

### test-package.sh

本地测试打包流程，无需创建 GitHub Release。

**使用场景**: 开发者本地测试打包

**使用方法**:
```bash
# 使用默认版本 1.0.1
./scripts/dev/test-package.sh

# 指定版本
./scripts/dev/test-package.sh 1.0.2
```

**功能**:
1. 检查环境（Java 版本、jpackage）
2. 构建项目
3. 验证和测试 JAR 文件
4. 生成原生应用安装包（如果 jpackage 可用）

**输出**:
- JAR 文件: `DevTools/target/DevTools-{version}.jar`
- 校验和: `DevTools/target/DevTools-{version}.jar.sha256`
- 原生安装包: `dist/native-{platform}/`

## 脚本关系图

```
用户场景
├── 从 GitHub 安装
│   └── install/from-github.sh
│       └── 创建启动脚本 → launch/devtools.sh
│
├── 本地开发安装
│   └── install/local.sh
│       └── 创建启动脚本 → launch/devtools.sh
│
└── 直接运行（已安装）
    └── launch/devtools.sh
        ├── 查找已安装版本
        └── 或回退到项目版本
```

## 快速参考

### 开发者工作流

```bash
# 1. 构建项目
mvn clean package -DskipTests

# 2. 本地安装
./scripts/install/local.sh

# 3. 启动应用
./scripts/launch/devtools.sh

# 4. 测试打包
./scripts/dev/test-package.sh
```

### 最终用户工作流

```bash
# 1. 从 GitHub 安装
curl -L https://github.com/daichangya/JavaFxDevTools/releases/latest/download/install-1.0.1.sh | bash

# 2. 启动应用
devtools
# 或
javafxeditor
```

## 注意事项

1. **启动脚本自动检测**: `launch/` 目录下的脚本会自动检测 JAR 文件位置，无需手动指定路径
2. **版本兼容性**: 启动脚本支持多个版本号，优先使用最新版本
3. **JavaFX 模块**: 启动脚本会自动查找并使用 JavaFX 模块（如果可用）
4. **macOS 权限**: 如果遇到权限问题，运行 `utils/fix-macos-permissions.sh`

## 更新日志

- **2026-01-08**: 重新组织脚本结构，创建统一的启动脚本
