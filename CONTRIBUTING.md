# 贡献指南

感谢您对 JavaFxDevTools 项目的关注！我们欢迎各种形式的贡献。

## 如何贡献

### 报告问题

如果您发现了 bug 或有功能建议，请：

1. 检查 [Issues](https://github.com/daichangya/JavaFxDevTools/issues) 中是否已有相关问题
2. 如果没有，请创建新的 Issue，详细描述问题或建议
3. 如果是 bug，请提供复现步骤和环境信息

### 提交代码

1. **Fork 项目**
   ```bash
   # 在 GitHub 上 Fork 项目
   ```

2. **克隆你的 Fork**
   ```bash
   git clone https://github.com/YOUR_USERNAME/JavaFxDevTools.git
   cd JavaFxDevTools
   ```

3. **创建分支**
   ```bash
   git checkout -b feature/your-feature-name
   # 或
   git checkout -b fix/your-bug-fix
   ```

4. **进行开发**
   - 编写代码
   - 添加必要的测试
   - 更新相关文档
   - 遵循项目的代码规范

5. **提交更改**
   ```bash
   git add .
   git commit -m "描述你的更改"
   ```

6. **推送分支**
   ```bash
   git push origin feature/your-feature-name
   ```

7. **创建 Pull Request**
   - 在 GitHub 上创建 Pull Request
   - 详细描述你的更改
   - 等待代码审查

## 代码规范

- 遵循 Java 编码规范
- 使用 4 个空格缩进（不使用 Tab）
- 类名使用 PascalCase
- 方法名和变量名使用 camelCase
- 常量使用 UPPER_SNAKE_CASE
- 所有公共类和方法必须包含 JavaDoc 注释
- 注释必须包含 `@author` 标签

## 开发插件

如果要开发新插件，请参考 [插件开发指南](PLUGIN_DEVELOPMENT.md)。

## 测试

- 确保所有测试通过
- 添加新功能时，请添加相应的测试
- 运行测试：`mvn test`

## 文档

- 更新相关文档（README、API 文档等）
- 添加代码注释
- 更新变更日志（如果有）

## 问题反馈

如果您有任何问题，可以通过以下方式联系：

- 创建 [Issue](https://github.com/daichangya/JavaFxDevTools/issues)
- 发送邮件（如果有）

再次感谢您的贡献！

