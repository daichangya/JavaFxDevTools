package com.daicy.javafxeditor;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * JavaFxEditor 主类
 * 
 * <p>可定制的代码编辑器，使用策略模式实现不同的编辑器行为。
 * 负责初始化窗口和 UI，应用策略模式配置编辑器行为。
 * 
 * @author daicy
 */
public class JavaFxEditor {

    private final AppStrategy appStrategy;

    public JavaFxEditor(AppStrategy appStrategy) {
        this.appStrategy = appStrategy;
    }

    public JavaFxEditor() {
        this.appStrategy = new EditorStrategy();
    }


    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainWindow.fxml"));
        BorderPane root = (BorderPane) loader.load();
        MainWindowController mainWindowController = loader.getController();

        Scene scene = new Scene(root);

        String syntaxCssUrl = appStrategy.getSyntaxCss().toExternalForm();
        scene.getStylesheets().add(syntaxCssUrl);

        mainWindowController.init(primaryStage, appStrategy);

        javafx.application.Platform.runLater(() -> {
            primaryStage.setScene(scene);
            primaryStage.show();
        });
    }
}
