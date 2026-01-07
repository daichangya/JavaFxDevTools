package com.daicy.javafxeditor;

import javafx.application.Application;
import javafx.stage.Stage;
import org.fxmisc.richtext.StyleClassedTextArea;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * JavaFxEditor 应用程序入口点
 * 
 * <p>创建 JavaFxEditor 实例并启动应用程序。
 * 在 macOS 上设置 Dock 图标。
 * 
 * @author daicy
 */
public class TestEditor extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        JavaFxEditor javaFxEditor = new JavaFxEditor();
        javaFxEditor.start(primaryStage);
        // 设置 Dock 图标
        setDockIcon();
    }

    // 设置 macOS Dock 图标
    private void setDockIcon() {
        try {
            // 读取自定义图标
            BufferedImage image = ImageIO.read(getClass().getResource("/zthinker.png"));

            // 设置图标到 Taskbar（Dock）
            Taskbar taskbar = Taskbar.getTaskbar();
            if (taskbar.isSupported(Taskbar.Feature.ICON_IMAGE)) {
                taskbar.setIconImage(image); // 设置 Dock 图标
            }
        } catch (IOException e) {
            System.out.println("Error setting dock icon: " + e.getMessage());
        }
    }


}

