package com.daicy.javafxeditor;

import com.daicy.javafxeditor.about.AboutBox;
import com.daicy.javafxeditor.desktop.DesktopUtils;
import javafx.stage.FileChooser;
import org.fxmisc.richtext.CodeArea;

import java.io.File;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

/**
 * AppStrategy 的默认实现
 * 
 * <p>提供默认的编辑器行为，包括文件选择器配置、代码编辑器创建、
 * 语法高亮样式等。
 * 
 * @author daicy
 */
public class EditorStrategy implements AppStrategy {

    private final AboutBox aboutBox = new AboutBox();

    @Override
    public String getTitle() {
        return "JavaFxEditor";
    }

    @Override
    public FileChooser createSourceFileChooser() {
        FileChooser fileChooser = new FileChooser();
//        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("Chronos source file", "*.chronos"));
        return fileChooser;
    }

    @Override
    public CodeArea createCodeEditor() {
        CodeArea codeEditor = new CodeArea();
        codeEditor.getUndoManager().forgetHistory();
        return codeEditor;
    }


    @Override
    public File getSavedSourceFile(FileChooser sourceFileChooser, File selectedFile) {
        return selectedFile;
    }

    @Override
    public boolean settingsSupported() {
        return false;
    }


    @Override
    public void showSettings() {
        // No settings implementation
    }

    @Override
    public URL getSyntaxCss() {
        return getClass().getResource("chronos-syntax.css");
    }

    @Override
    public void showOnlineReference() {
        DesktopUtils.openBrowser( "https://jsdiff.com/",null);
    }

    @Override
    public void showAboutWindow() {
        aboutBox.showAndWait();
    }
}
