#!/bin/bash
# DevTools 统一启动脚本
# 自动检测安装位置：优先使用已安装版本，否则使用项目构建版本

set -e

# 尝试多个可能的版本号
find_jar() {
    local app_name=$1
    local install_dir="$HOME/.local/share/javafxdevtools"
    local project_dir="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
    
    # 优先查找已安装版本
    for version in "1.0.1" "1.0.0-SNAPSHOT"; do
        if [ -f "$install_dir/$app_name-$version.jar" ]; then
            echo "$install_dir/$app_name-$version.jar"
            return 0
        fi
    done
    
    # 回退到项目构建版本
    for version in "1.0.1" "1.0.0-SNAPSHOT"; do
        if [ -f "$project_dir/DevTools/target/$app_name-$version.jar" ]; then
            echo "$project_dir/DevTools/target/$app_name-$version.jar"
            return 0
        fi
        # 尝试 runnable JAR
        if [ -f "$project_dir/DevTools/target/$app_name-$version-runnable.jar" ]; then
            echo "$project_dir/DevTools/target/$app_name-$version-runnable.jar"
            return 0
        fi
    done
    
    return 1
}

APP_JAR=$(find_jar "DevTools")

if [ -z "$APP_JAR" ] || [ ! -f "$APP_JAR" ]; then
    echo "❌ 错误: 未找到 DevTools JAR 文件"
    echo ""
    echo "请选择以下方式之一："
    echo "  1. 运行安装脚本: ./scripts/install/local.sh"
    echo "  2. 或先构建项目: mvn clean package -DskipTests"
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
    # 如果找到足够的 JavaFX 模块，使用模块路径运行（添加必要的导出和打开）
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

