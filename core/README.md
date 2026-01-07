# core 模块

## 模块概述

core 模块是 JavaFxDevTools 项目的基础模块，提供其他模块共享的基础功能和组件。

## 模块信息

- **GroupId**: `com.daicy`
- **ArtifactId**: `core`
- **版本**: `1.0.0-SNAPSHOT`

## 主要功能

- 提供基础的 JavaFX 组件
- 共享的工具类和工具方法
- 通用的 FXML 资源

## 核心类

### HelloApplication

示例 JavaFX 应用程序，展示基本的 JavaFX 应用结构。

**包路径**: `com.daicy.core.HelloApplication`

### HelloController

HelloApplication 的控制器类。

**包路径**: `com.daicy.core.HelloController`

## 依赖

- JavaFX Controls
- JavaFX FXML
- JavaFX Web
- RichTextFX
- Apache Commons Lang3
- Commons IO
- Jackson (Core, Databind, Annotations)
- Lombok

## 构建

```bash
mvn clean package
```

## 使用

其他模块通过 Maven 依赖引入 core 模块：

```xml
<dependency>
    <groupId>com.daicy</groupId>
    <artifactId>core</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>
```

## 更多信息

详细说明请参考 [模块说明文档](../MODULES.md#core-模块)。

