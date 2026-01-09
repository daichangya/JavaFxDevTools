#!/bin/bash
# macOS 权限修复脚本
# 用于移除应用的隔离属性，解决"已损坏，无法打开"的问题

set -e

echo "=========================================="
echo "macOS 应用权限修复"
echo "=========================================="
echo ""

# 检查应用是否已安装
if [ ! -d "/Applications/DevTools.app" ] && [ ! -d "/Applications/JavaFxEditor.app" ]; then
    echo "⚠️  未找到已安装的应用"
    echo "请先安装应用后再运行此脚本"
    exit 1
fi

echo "移除应用的隔离属性..."

if [ -d "/Applications/DevTools.app" ]; then
    echo "  - 修复 DevTools..."
    xattr -cr /Applications/DevTools.app
    echo "    ✅ DevTools 权限已修复"
fi

if [ -d "/Applications/JavaFxEditor.app" ]; then
    echo "  - 修复 JavaFxEditor..."
    xattr -cr /Applications/JavaFxEditor.app
    echo "    ✅ JavaFxEditor 权限已修复"
fi

echo ""
echo "=========================================="
echo "✅ 权限修复完成！"
echo "=========================================="
echo ""
echo "现在可以正常启动应用了："
echo "  - 从 Launchpad 或 Applications 文件夹启动"
echo "  - 或使用 Spotlight 搜索应用名称"
echo ""

