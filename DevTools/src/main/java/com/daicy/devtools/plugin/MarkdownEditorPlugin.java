package com.daicy.devtools.plugin;

import com.daicy.devtools.TextPlugin;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import java.nio.file.Path;

// Markdown编辑器插件
public class MarkdownEditorPlugin implements TextPlugin {
    private final TextArea inputTextArea = new TextArea();
    private final WebView previewWebView = new WebView();
    private final BorderPane contentPane;

    public MarkdownEditorPlugin() {
        contentPane = new BorderPane();

        VBox leftVBox = new VBox(5);
        leftVBox.getChildren().add(inputTextArea);

        // 格式设置按钮
        HBox buttonBox = new HBox(3);
        Button boldButton = new Button("B");
        boldButton.setOnAction(event -> {
            int start = inputTextArea.getSelection().getStart();
            int end = inputTextArea.getSelection().getEnd();
            if (start!= end) {
                String selectedText = inputTextArea.getSelectedText();
                inputTextArea.replaceText(start, end, "**" + selectedText + "**");
            }
        });
        Button italicButton = new Button("I");
        italicButton.setOnAction(event -> {
            int start = inputTextArea.getSelection().getStart();
            int end = inputTextArea.getSelection().getEnd();
            if (start!= end) {
                String selectedText = inputTextArea.getSelectedText();
                inputTextArea.replaceText(start, end, "_" + selectedText + "_");
            }
        });
        Button h1Button = new Button("H1");
        h1Button.setOnAction(event -> {
            int start = inputTextArea.getSelection().getStart();
            int end = inputTextArea.getSelection().getEnd();
            if (start!= end) {
                String selectedText = inputTextArea.getSelectedText();
                inputTextArea.replaceText(start, end, "# " + selectedText);
            }
        });
        buttonBox.getChildren().addAll(boldButton, italicButton, h1Button);

        leftVBox.getChildren().add(buttonBox);

        contentPane.setLeft(leftVBox);
        contentPane.setCenter(previewWebView);

        // 监听文本变化，实时更新预览
        inputTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
            updatePreview(newValue);
        });
    }

    @Override
    public Pane getContentPane() {
        return contentPane;
    }

    @Override
    public Path getDefaultPath() {
        return null;
    }

    @Override
    public void open(String filePath) {
        // 这里可以添加从文件读取 Markdown 内容并填充到输入框的逻辑，暂未实现
    }

    @Override
    public void save(String filePath) {
        // 这里可以添加将输入框中的 Markdown 内容保存到文件的逻辑，暂未实现
    }

    @Override
    public String getContent() {
        return inputTextArea.getText();
    }

    @Override
    public void setContent(String content) {
        inputTextArea.setText(content);
    }

    private void updatePreview(String markdownText) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdownText);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        String html = renderer.render(document);
        Platform.runLater(() -> previewWebView.getEngine().loadContent(html));
    }
}