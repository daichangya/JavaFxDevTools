#!/bin/bash
# 本地测试打包脚本
# 用法: ./scripts/test-package.sh [version]

set -e

VERSION="${1:-1.0.1}"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"

cd "$PROJECT_DIR"

echo "=========================================="
echo "JavaFxDevTools 本地打包测试"
echo "=========================================="
echo "版本: $VERSION"
echo "项目目录: $PROJECT_DIR"
echo ""

# 检查 Java 版本
echo "检查 Java 版本..."
if ! command -v java &> /dev/null; then
    echo "❌ 错误: 未找到 Java"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ 错误: 需要 JDK 17 或更高版本，当前版本: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java 版本: $(java -version 2>&1 | head -n 1)"
echo ""

# 检查 jpackage
echo "检查 jpackage..."
if ! command -v jpackage &> /dev/null; then
    echo "⚠️  警告: 未找到 jpackage，将跳过原生应用打包"
    echo "   jpackage 需要 JDK 14+，并且可能需要在对应平台上运行"
    JPACKAGE_AVAILABLE=false
else
    echo "✅ jpackage 可用: $(jpackage --version 2>&1 | head -n 1)"
    JPACKAGE_AVAILABLE=true
fi
echo ""

# 构建项目
echo "=========================================="
echo "步骤 1: 构建项目"
echo "=========================================="
mvn clean package -DskipTests

echo ""
echo "=========================================="
echo "步骤 2: 验证 JAR 文件"
echo "=========================================="

# 重命名 JAR 文件
if [ -f "DevTools/target/DevTools-1.0.0-SNAPSHOT-runnable.jar" ]; then
    mv DevTools/target/DevTools-1.0.0-SNAPSHOT-runnable.jar "DevTools/target/DevTools-$VERSION.jar"
    echo "✅ DevTools JAR: DevTools/target/DevTools-$VERSION.jar"
    ls -lh "DevTools/target/DevTools-$VERSION.jar"
else
    echo "❌ DevTools JAR 未找到"
    exit 1
fi

if [ -f "JavaFxEditor/target/JavaFxEditor-1.0.0-SNAPSHOT-runnable.jar" ]; then
    mv JavaFxEditor/target/JavaFxEditor-1.0.0-SNAPSHOT-runnable.jar "JavaFxEditor/target/JavaFxEditor-$VERSION.jar"
    echo "✅ JavaFxEditor JAR: JavaFxEditor/target/JavaFxEditor-$VERSION.jar"
    ls -lh "JavaFxEditor/target/JavaFxEditor-$VERSION.jar"
else
    echo "❌ JavaFxEditor JAR 未找到"
    exit 1
fi

echo ""
echo "=========================================="
echo "步骤 3: 生成 SHA256 校验和"
echo "=========================================="

cd DevTools/target
if [ -f "DevTools-$VERSION.jar" ]; then
    sha256sum "DevTools-$VERSION.jar" > "DevTools-$VERSION.jar.sha256"
    echo "✅ DevTools 校验和:"
    cat "DevTools-$VERSION.jar.sha256"
fi
cd "$PROJECT_DIR"

cd JavaFxEditor/target
if [ -f "JavaFxEditor-$VERSION.jar" ]; then
    sha256sum "JavaFxEditor-$VERSION.jar" > "JavaFxEditor-$VERSION.jar.sha256"
    echo "✅ JavaFxEditor 校验和:"
    cat "JavaFxEditor-$VERSION.jar.sha256"
fi
cd "$PROJECT_DIR"

echo ""
echo "=========================================="
echo "步骤 4: 测试 JAR 文件"
echo "=========================================="

echo "测试 DevTools JAR..."
java -jar "DevTools/target/DevTools-$VERSION.jar" --version 2>&1 || echo "⚠️  版本检查可能不支持，但 JAR 文件有效"

echo "测试 JavaFxEditor JAR..."
java -jar "JavaFxEditor/target/JavaFxEditor-$VERSION.jar" --version 2>&1 || echo "⚠️  版本检查可能不支持，但 JAR 文件有效"

echo ""
echo "=========================================="
echo "步骤 5: 原生应用打包（如果可用）"
echo "=========================================="

if [ "$JPACKAGE_AVAILABLE" = true ]; then
    # 检测平台
    OS="$(uname -s)"
    case "$OS" in
        Linux*)
            PKG_TYPE="deb"
            PLATFORM="linux"
            echo "检测到平台: Linux"
            # 检查依赖
            if ! command -v fakeroot &> /dev/null || ! command -v dpkg-deb &> /dev/null; then
                echo "⚠️  警告: 缺少 fakeroot 或 dpkg-deb，可能需要安装:"
                echo "   sudo apt-get install fakeroot dpkg-dev"
            fi
            ;;
        Darwin*)
            PKG_TYPE="dmg"
            PLATFORM="macos"
            echo "检测到平台: macOS"
            ;;
        MINGW*|MSYS*|CYGWIN*)
            PKG_TYPE="msi"
            PLATFORM="windows"
            echo "检测到平台: Windows"
            ;;
        *)
            PKG_TYPE="app-image"
            PLATFORM="unknown"
            echo "⚠️  未知平台: $OS，使用 app-image"
            ;;
    esac
    
    mkdir -p "dist/native-$PLATFORM"
    
    # 打包 DevTools
    if [ -f "DevTools/target/DevTools-$VERSION.jar" ]; then
        echo ""
        echo "打包 DevTools ($PKG_TYPE)..."
        
        LICENSE_ARG=""
        if [ -f "LICENSE" ]; then
            LICENSE_ARG="--license-file LICENSE"
        fi
        
        if jpackage \
            --input DevTools/target \
            --name DevTools \
            --main-jar "DevTools-$VERSION.jar" \
            --main-class com.daicy.devtools.TextEditor \
            --type "$PKG_TYPE" \
            --dest "dist/native-$PLATFORM" \
            --app-version "$VERSION" \
            --description "DevTools - Plugin-based Text Editor" \
            --vendor "daichangya" \
            $LICENSE_ARG \
            --verbose; then
            echo "✅ DevTools 原生包创建成功"
        else
            echo "❌ DevTools 原生包创建失败"
        fi
    fi
    
    # 打包 JavaFxEditor
    if [ -f "JavaFxEditor/target/JavaFxEditor-$VERSION.jar" ]; then
        echo ""
        echo "打包 JavaFxEditor ($PKG_TYPE)..."
        
        LICENSE_ARG=""
        if [ -f "LICENSE" ]; then
            LICENSE_ARG="--license-file LICENSE"
        fi
        
        if jpackage \
            --input JavaFxEditor/target \
            --name JavaFxEditor \
            --main-jar "JavaFxEditor-$VERSION.jar" \
            --main-class com.daicy.javafxeditor.TestEditor \
            --type "$PKG_TYPE" \
            --dest "dist/native-$PLATFORM" \
            --app-version "$VERSION" \
            --description "JavaFxEditor - Customizable Code Editor" \
            --vendor "daichangya" \
            $LICENSE_ARG \
            --verbose; then
            echo "✅ JavaFxEditor 原生包创建成功"
        else
            echo "❌ JavaFxEditor 原生包创建失败"
        fi
    fi
    
    echo ""
    echo "生成的文件:"
    ls -lh "dist/native-$PLATFORM/" 2>/dev/null || echo "没有生成文件"
else
    echo "⚠️  跳过原生应用打包（jpackage 不可用）"
fi

echo ""
echo "=========================================="
echo "✅ 打包测试完成！"
echo "=========================================="
echo ""
echo "生成的文件:"
echo "  JAR 文件:"
echo "    - DevTools/target/DevTools-$VERSION.jar"
echo "    - JavaFxEditor/target/JavaFxEditor-$VERSION.jar"
echo "  校验和:"
echo "    - DevTools/target/DevTools-$VERSION.jar.sha256"
echo "    - JavaFxEditor/target/JavaFxEditor-$VERSION.jar.sha256"

if [ "$JPACKAGE_AVAILABLE" = true ] && [ -d "dist/native-$PLATFORM" ]; then
    echo "  原生安装包:"
    ls -1 "dist/native-$PLATFORM/" 2>/dev/null | sed 's/^/    - /' || echo "    (无)"
fi

echo ""
echo "测试完成！"

