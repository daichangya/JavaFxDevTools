#!/bin/bash
# 安装测试脚本
# 验证安装是否成功，并测试应用启动

set -e

INSTALL_DIR="$HOME/.local/share/javafxdevtools"
BIN_DIR="$HOME/.local/bin"
VERSION="1.0.1"

echo "=========================================="
echo "JavaFxDevTools 安装测试"
echo "=========================================="
echo ""

# 测试 1: 检查安装目录
echo "测试 1: 检查安装目录..."
if [ ! -d "$INSTALL_DIR" ]; then
    echo "❌ 安装目录不存在: $INSTALL_DIR"
    exit 1
fi
echo "✅ 安装目录存在"

# 测试 2: 检查 JAR 文件
echo ""
echo "测试 2: 检查 JAR 文件..."
DEVTOOLS_JAR="$INSTALL_DIR/DevTools-$VERSION.jar"
JAVAFXEDITOR_JAR="$INSTALL_DIR/JavaFxEditor-$VERSION.jar"

if [ ! -f "$DEVTOOLS_JAR" ]; then
    echo "❌ DevTools JAR 文件不存在: $DEVTOOLS_JAR"
    exit 1
fi
echo "✅ DevTools JAR 文件存在: $(du -h "$DEVTOOLS_JAR" | cut -f1)"

if [ ! -f "$JAVAFXEDITOR_JAR" ]; then
    echo "❌ JavaFxEditor JAR 文件不存在: $JAVAFXEDITOR_JAR"
    exit 1
fi
echo "✅ JavaFxEditor JAR 文件存在: $(du -h "$JAVAFXEDITOR_JAR" | cut -f1)"

# 测试 3: 检查启动脚本
echo ""
echo "测试 3: 检查启动脚本..."
if [ ! -f "$BIN_DIR/launch-devtools.sh" ]; then
    echo "❌ DevTools 启动脚本不存在"
    exit 1
fi
echo "✅ DevTools 启动脚本存在"

if [ ! -f "$BIN_DIR/launch-javafxeditor.sh" ]; then
    echo "❌ JavaFxEditor 启动脚本不存在"
    exit 1
fi
echo "✅ JavaFxEditor 启动脚本存在"

# 测试 4: 检查命令别名
echo ""
echo "测试 4: 检查命令别名..."
if [ ! -f "$BIN_DIR/devtools" ]; then
    echo "❌ devtools 命令不存在"
    exit 1
fi
echo "✅ devtools 命令存在"

if [ ! -f "$BIN_DIR/javafxeditor" ]; then
    echo "❌ javafxeditor 命令不存在"
    exit 1
fi
echo "✅ javafxeditor 命令存在"

# 测试 5: 检查 PATH
echo ""
echo "测试 5: 检查 PATH..."
if [[ ":$PATH:" != *":$BIN_DIR:"* ]]; then
    echo "⚠️  警告: $BIN_DIR 不在 PATH 中"
    echo "请运行: export PATH=\"\$PATH:$BIN_DIR\""
    echo "或添加到 ~/.zshrc / ~/.bashrc"
    USE_FULL_PATH=true
else
    echo "✅ $BIN_DIR 在 PATH 中"
    USE_FULL_PATH=false
fi

# 测试 6: 测试命令可用性
echo ""
echo "测试 6: 测试命令可用性..."
if [ "$USE_FULL_PATH" = true ]; then
    if command -v "$BIN_DIR/devtools" &> /dev/null; then
        echo "✅ devtools 命令可用（使用完整路径）"
    else
        echo "⚠️  devtools 命令不可用（需要添加到 PATH）"
    fi
else
    if command -v devtools &> /dev/null; then
        echo "✅ devtools 命令可用"
    else
        echo "⚠️  devtools 命令不可用"
    fi
fi

# 测试 7: 测试应用启动（后台运行）
echo ""
echo "测试 7: 测试应用启动..."
echo "启动 DevTools（后台运行 5 秒）..."

if [ "$USE_FULL_PATH" = true ]; then
    "$BIN_DIR/devtools" > /tmp/devtools-test.log 2>&1 &
else
    devtools > /tmp/devtools-test.log 2>&1 &
fi

APP_PID=$!
sleep 5

if ps -p $APP_PID > /dev/null 2>&1; then
    echo "✅ DevTools 应用正在运行 (PID: $APP_PID)"
    kill $APP_PID 2>/dev/null || true
    echo "✅ 应用已停止"
else
    echo "⚠️  应用可能已退出，检查日志..."
    if [ -f /tmp/devtools-test.log ]; then
        echo "最后 10 行日志:"
        tail -10 /tmp/devtools-test.log
    fi
fi

echo ""
echo "=========================================="
echo "✅ 测试完成！"
echo "=========================================="
echo ""
echo "如果所有测试通过，可以使用以下命令启动应用:"
if [ "$USE_FULL_PATH" = true ]; then
    echo "  $BIN_DIR/devtools"
    echo "  $BIN_DIR/javafxeditor"
else
    echo "  devtools"
    echo "  javafxeditor"
fi
echo ""

