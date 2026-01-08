# 测试脚本说明

## 本地测试脚本

### test-package.sh

本地测试打包流程，无需创建 GitHub Release。

#### 使用方法

```bash
# 使用默认版本 1.0.1
./scripts/test-package.sh

# 指定版本
./scripts/test-package.sh 1.0.2
```

#### 功能

1. **检查环境**
   - 验证 Java 版本（需要 JDK 17+）
   - 检查 jpackage 是否可用

2. **构建项目**
   - 运行 `mvn clean package -DskipTests`
   - 生成可执行 JAR 文件

3. **验证和测试**
   - 重命名 JAR 文件为指定版本
   - 生成 SHA256 校验和
   - 测试 JAR 文件是否可运行

4. **原生应用打包**（如果 jpackage 可用）
   - 自动检测平台（Linux/macOS/Windows）
   - 生成对应的安装包：
     - Linux: `.deb`
     - macOS: `.dmg`
     - Windows: `.msi`

#### 输出

- JAR 文件: `DevTools/target/DevTools-{version}.jar`
- 校验和: `DevTools/target/DevTools-{version}.jar.sha256`
- 原生安装包: `dist/native-{platform}/`

#### 示例

```bash
# 测试版本 1.0.1
./scripts/test-package.sh 1.0.1

# 输出示例:
# ✅ Java 版本: openjdk version "17.0.8"
# ✅ jpackage 可用
# ✅ DevTools JAR: DevTools/target/DevTools-1.0.1.jar
# ✅ JavaFxEditor JAR: JavaFxEditor/target/JavaFxEditor-1.0.1.jar
# ✅ DevTools 原生包创建成功
# ✅ JavaFxEditor 原生包创建成功
```

## GitHub Actions 手动触发

除了本地测试，你也可以在 GitHub 上手动触发 workflow：

1. 访问 GitHub 仓库的 **Actions** 标签页
2. 选择 **Release** workflow
3. 点击 **Run workflow** 按钮
4. 输入版本号（如 `1.0.1`）
5. 点击 **Run workflow**

这样可以在 GitHub Actions 环境中测试完整的打包流程，包括多平台构建。

## 注意事项

- **本地测试**：只能测试当前平台的原生安装包
- **GitHub Actions**：可以测试所有平台（Linux、macOS、Windows）
- **jpackage 要求**：
  - JDK 14+（已包含在 JDK 17 中）
  - Linux 上需要 `fakeroot` 和 `dpkg-dev`（脚本会自动提示）
  - macOS 上可能需要代码签名（测试时可能跳过）

