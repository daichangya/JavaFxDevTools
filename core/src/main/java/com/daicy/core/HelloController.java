package com.daicy.core;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * HelloApplication 的控制器类
 * 
 * <p>处理 FXML 中定义的事件，管理 UI 交互。
 * 
 * @author daicy
 */
public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}