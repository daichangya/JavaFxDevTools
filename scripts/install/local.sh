#!/bin/bash
# 本地安装脚本
# 将 DevTools 和 JavaFxEditor 安装到用户本地目录

set -e

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/.." && pwd)"
VERSION="1.0.1"

# 安装目录
INSTALL_DIR="$HOME/.local/share/javafxdevtools"
BIN_DIR="$HOME/.local/bin"

echo "=========================================="
echo "JavaFxDevTools 本地安装"
echo "=========================================="
echo "版本: $VERSION"
echo "安装目录: $INSTALL_DIR"
echo "命令目录: $BIN_DIR"
echo ""

# 检查 Java 版本
echo "检查 Java 版本..."
if ! command -v java &> /dev/null; then
    echo "❌ 错误: 未找到 Java"
    echo "请先安装 JDK 17 或更高版本"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ 错误: 需要 JDK 17 或更高版本，当前版本: $JAVA_VERSION"
    exit 1
fi

echo "✅ Java 版本: $(java -version 2>&1 | head -n 1)"
echo ""

# 检查 JAR 文件
echo "检查 JAR 文件..."
DEVTOOLS_JAR="$PROJECT_DIR/DevTools/target/DevTools-$VERSION.jar"
JAVAFXEDITOR_JAR="$PROJECT_DIR/JavaFxEditor/target/JavaFxEditor-$VERSION.jar"

if [ ! -f "$DEVTOOLS_JAR" ]; then
    echo "❌ 错误: 未找到 DevTools JAR 文件: $DEVTOOLS_JAR"
    echo "请先运行: mvn clean package -DskipTests"
    exit 1
fi

if [ ! -f "$JAVAFXEDITOR_JAR" ]; then
    echo "❌ 错误: 未找到 JavaFxEditor JAR 文件: $JAVAFXEDITOR_JAR"
    echo "请先运行: mvn clean package -DskipTests"
    exit 1
fi

echo "✅ 找到 JAR 文件"
echo ""

# 创建安装目录
echo "创建安装目录..."
mkdir -p "$INSTALL_DIR"
mkdir -p "$BIN_DIR"
echo "✅ 目录已创建"
echo ""

# 复制 JAR 文件
echo "复制 JAR 文件..."
cp "$DEVTOOLS_JAR" "$INSTALL_DIR/"
cp "$JAVAFXEDITOR_JAR" "$INSTALL_DIR/"
echo "✅ JAR 文件已复制"
echo ""

# 创建启动脚本（适配安装目录）
echo "创建启动脚本..."

# DevTools 启动脚本（使用统一的启动脚本）
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "$SCRIPT_DIR/../../.." && pwd)"
cat > "$BIN_DIR/launch-devtools.sh" << LAUNCH_EOF
#!/bin/bash
# DevTools 启动脚本（安装版本）

INSTALL_DIR="$HOME/.local/share/javafxdevtools"
VERSION="1.0.1"
APP_JAR="$INSTALL_DIR/DevTools-$VERSION.jar"

if [ ! -f "$APP_JAR" ]; then
    echo "❌ 错误: 未找到 JAR 文件: $APP_JAR"
    echo "请先运行安装脚本: ./scripts/install-local.sh"
    exit 1
fi

# 检查 Java 版本
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ 错误: 需要 JDK 17 或更高版本，当前版本: $JAVA_VERSION"
    exit 1
fi

# 尝试查找 JavaFX 模块路径（收集所有模块到同一目录）
JAVAFX_TEMP=$(mktemp -d)
JAVAFX_MODULES=("base" "controls" "fxml" "graphics" "web" "media")
FOUND_COUNT=0

for module in "${JAVAFX_MODULES[@]}"; do
    JAVAFX_JAR=$(find ~/.m2/repository/org/openjfx -name "javafx-$module-17.0.6-mac-aarch64.jar" -type f 2>/dev/null | head -1)
    if [ -n "$JAVAFX_JAR" ] && [ -f "$JAVAFX_JAR" ]; then
        cp "$JAVAFX_JAR" "$JAVAFX_TEMP/" 2>/dev/null && FOUND_COUNT=$((FOUND_COUNT + 1))
    fi
done

# 运行应用
if [ "$FOUND_COUNT" -ge 5 ] && [ -d "$JAVAFX_TEMP" ] && [ "$(ls -1 "$JAVAFX_TEMP"/*.jar 2>/dev/null | wc -l)" -ge 5 ]; then
    # 如果找到 JavaFX 模块，使用模块路径运行
    java --module-path "$JAVAFX_TEMP" \
         --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,javafx.web,javafx.media \
         --add-exports javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED \
         --add-exports javafx.base/com.sun.javafx.binding=ALL-UNNAMED \
         --add-exports javafx.graphics/com.sun.javafx.util=ALL-UNNAMED \
         --add-opens javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED \
         -jar "$APP_JAR" "$@" 2>&1 | grep -v "中没有主清单属性" || true
    EXIT_CODE=${PIPESTATUS[0]}
    # 清理临时目录
    rm -rf "$JAVAFX_TEMP" 2>/dev/null
    exit $EXIT_CODE
else
    # 否则直接运行（Fat JAR 应该包含所有依赖，但可能缺少原生库）
    echo "⚠️  警告: 未找到足够的 JavaFX 模块，尝试直接运行..."
    echo "如果失败，请确保 JavaFX 已正确安装"
    java -jar "$APP_JAR" "$@"
fi
LAUNCH_EOF
    chmod +x "$BIN_DIR/launch-devtools.sh"
    echo "✅ DevTools 启动脚本已创建（回退版本）"
fi

# JavaFxEditor 启动脚本
if [ -f "$UNIFIED_LAUNCH_DIR/javafxeditor.sh" ]; then
    cp "$UNIFIED_LAUNCH_DIR/javafxeditor.sh" "$BIN_DIR/launch-javafxeditor.sh"
    chmod +x "$BIN_DIR/launch-javafxeditor.sh"
    echo "✅ JavaFxEditor 启动脚本已创建（使用统一脚本）"
else
    # 回退：创建简化版本的启动脚本
    cat > "$BIN_DIR/launch-javafxeditor.sh" << LAUNCH_EOF
#!/bin/bash
# JavaFxEditor 启动脚本（安装版本）

INSTALL_DIR="$HOME/.local/share/javafxdevtools"
VERSION="1.0.1"
APP_JAR="$INSTALL_DIR/JavaFxEditor-$VERSION.jar"

if [ ! -f "$APP_JAR" ]; then
    echo "❌ 错误: 未找到 JAR 文件: $APP_JAR"
    echo "请先运行安装脚本: ./scripts/install-local.sh"
    exit 1
fi

# 检查 Java 版本
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | sed '/^1\./s///' | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ 错误: 需要 JDK 17 或更高版本，当前版本: $JAVA_VERSION"
    exit 1
fi

# 尝试查找 JavaFX 模块路径（收集所有模块到同一目录）
JAVAFX_TEMP=$(mktemp -d)
JAVAFX_MODULES=("base" "controls" "fxml" "graphics" "web" "media")
FOUND_COUNT=0

for module in "${JAVAFX_MODULES[@]}"; do
    JAVAFX_JAR=$(find ~/.m2/repository/org/openjfx -name "javafx-$module-17.0.6-mac-aarch64.jar" -type f 2>/dev/null | head -1)
    if [ -n "$JAVAFX_JAR" ] && [ -f "$JAVAFX_JAR" ]; then
        cp "$JAVAFX_JAR" "$JAVAFX_TEMP/" 2>/dev/null && FOUND_COUNT=$((FOUND_COUNT + 1))
    fi
done

# 运行应用
if [ "$FOUND_COUNT" -ge 5 ] && [ -d "$JAVAFX_TEMP" ] && [ "$(ls -1 "$JAVAFX_TEMP"/*.jar 2>/dev/null | wc -l)" -ge 5 ]; then
    # 如果找到 JavaFX 模块，使用模块路径运行
    java --module-path "$JAVAFX_TEMP" \
         --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.base,javafx.web,javafx.media \
         --add-exports javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED \
         --add-exports javafx.base/com.sun.javafx.binding=ALL-UNNAMED \
         --add-exports javafx.graphics/com.sun.javafx.util=ALL-UNNAMED \
         --add-opens javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED \
         -jar "$APP_JAR" "$@" 2>&1 | grep -v "中没有主清单属性" || true
    EXIT_CODE=${PIPESTATUS[0]}
    # 清理临时目录
    rm -rf "$JAVAFX_TEMP" 2>/dev/null
    exit $EXIT_CODE
else
    # 否则直接运行（Fat JAR 应该包含所有依赖，但可能缺少原生库）
    echo "⚠️  警告: 未找到足够的 JavaFX 模块，尝试直接运行..."
    echo "如果失败，请确保 JavaFX 已正确安装"
    java -jar "$APP_JAR" "$@"
fi
LAUNCH_EOF
    chmod +x "$BIN_DIR/launch-javafxeditor.sh"
    echo "✅ JavaFxEditor 启动脚本已创建（回退版本）"
fi

echo ""

# 创建命令别名（符号链接）
echo "创建命令别名..."
ln -sf "$BIN_DIR/launch-devtools.sh" "$BIN_DIR/devtools"
ln -sf "$BIN_DIR/launch-javafxeditor.sh" "$BIN_DIR/javafxeditor"
chmod +x "$BIN_DIR/devtools"
chmod +x "$BIN_DIR/javafxeditor"
echo "✅ 命令别名已创建"
echo ""

# 检查 PATH
echo "检查 PATH 配置..."
if [[ ":$PATH:" != *":$BIN_DIR:"* ]]; then
    echo "⚠️  警告: $BIN_DIR 不在 PATH 中"
    echo ""
    echo "请将以下内容添加到 ~/.bashrc 或 ~/.zshrc:"
    echo "  export PATH=\"\$PATH:$BIN_DIR\""
    echo ""
    echo "然后运行:"
    echo "  source ~/.bashrc  # 或 source ~/.zshrc"
    echo ""
    
    # 自动添加到 zshrc（如果使用 zsh）
    if [ -n "$ZSH_VERSION" ] || [ -f "$HOME/.zshrc" ]; then
        read -p "是否自动添加到 ~/.zshrc? (y/n) " -n 1 -r
        echo
        if [[ $REPLY =~ ^[Yy]$ ]]; then
            if ! grep -q "$BIN_DIR" "$HOME/.zshrc" 2>/dev/null; then
                echo "export PATH=\"\$PATH:$BIN_DIR\"" >> "$HOME/.zshrc"
                echo "✅ 已添加到 ~/.zshrc"
                echo "请运行: source ~/.zshrc"
            else
                echo "✅ PATH 配置已存在"
            fi
        fi
    fi
else
    echo "✅ $BIN_DIR 已在 PATH 中"
fi

echo ""
echo "=========================================="
echo "✅ 安装完成！"
echo "=========================================="
echo ""
echo "使用方法:"
echo "  devtools          # 启动 DevTools"
echo "  javafxeditor      # 启动 JavaFxEditor"
echo ""
echo "或者:"
echo "  launch-devtools.sh"
echo "  launch-javafxeditor.sh"
echo ""
echo "安装位置:"
echo "  JAR 文件: $INSTALL_DIR"
echo "  启动脚本: $BIN_DIR"
echo ""

