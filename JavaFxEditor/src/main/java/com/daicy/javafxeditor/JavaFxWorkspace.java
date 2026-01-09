package com.daicy.javafxeditor;

import com.daicy.core.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;

/**
 * Workspace 的具体实现，用于 JavaFX 编辑器
 * 
 * <p>实现 Workspace 的抽象方法，处理 JavaFX CodeArea 的文档操作。
 * 
 * @author daicy
 */
public class JavaFxWorkspace extends Workspace {
    
    private static final Logger logger = LoggerFactory.getLogger(JavaFxWorkspace.class);

    private final CodeArea codeEditor;

    public JavaFxWorkspace(Stage stage, FileChooser documentFileChooser, CodeArea codeEditor) {
        super(stage, documentFileChooser);
        this.codeEditor = codeEditor;
    }

    @Override
    protected boolean doNew() {
        codeEditor.clear();
        codeEditor.getUndoManager().forgetHistory();
        codeEditor.requestFocus();
        return true;
    }

    @Override
    protected boolean doOpen(File sourceFile) {
        try {
            String fileContent = new String(Files.readAllBytes(sourceFile.toPath()), StandardCharsets.UTF_8);
            codeEditor.clear();
            codeEditor.replaceText(0, 0, fileContent);
            codeEditor.getUndoManager().forgetHistory();
            codeEditor.requestFocus();
            return true;
        } catch (IOException e) {
            logger.error("打开文件失败: {}", sourceFile.getAbsolutePath(), e);
            ExceptionHandler.handleException(e, "文件打开失败", "无法打开文件", 
                    "无法读取文件: " + sourceFile.getAbsolutePath() + "\n错误: " + e.getMessage());
            return false;
        }
    }

    @Override
    protected boolean doSave(File targetFile) {
        try {
            Files.write(targetFile.toPath(), codeEditor.getText().getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            logger.error("保存文件失败: {}", targetFile.getAbsolutePath(), e);
            ExceptionHandler.handleException(e, "文件保存失败", "无法保存文件", 
                    "无法保存文件: " + targetFile.getAbsolutePath() + "\n错误: " + e.getMessage());
            return false;
        }
    }
}
