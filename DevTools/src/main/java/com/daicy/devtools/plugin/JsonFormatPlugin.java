package com.daicy.devtools.plugin;
import com.daicy.devtools.TextPlugin;
import com.daicy.core.ExceptionHandler;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.apache.commons.lang3.StringUtils;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonFormatPlugin implements TextPlugin {
    private static final Logger logger = LoggerFactory.getLogger(JsonFormatPlugin.class);
    
    private final CodeArea inputTextArea = new CodeArea();
    private final CodeArea outputTextArea = new CodeArea();
    private final VBox contentPane;
    private final CheckBox checkBox = new CheckBox("关键字转义");

    public JsonFormatPlugin() {
        contentPane = new VBox(10);

        Label inputLabel = new Label("输入JSON：");

        // 创建单选按钮用于控制是否关键字转义 Keyword escaping
        // 设置初始状态为选中
        checkBox.setSelected(true);
        HBox hBox = new HBox(10, inputLabel,checkBox);

        inputTextArea.setPrefHeight(300);
        inputTextArea.setWrapText(true);
        inputTextArea.setParagraphGraphicFactory(LineNumberFactory.get(inputTextArea));

        Label outputLabel = new Label("格式化后：");
//        outputTextArea.setPrefRowCount(10);
        outputTextArea.setPrefHeight(300);
        outputTextArea.setWrapText(true);
        outputTextArea.setEditable(false);

        Button formatButton = new Button("格式化");
        formatButton.setOnAction(event -> formatContent());

        Button generateJavaBeanButton = new Button("生成JavaBean");
        generateJavaBeanButton.setOnAction(event -> generateJavaBean());
        contentPane.getStylesheets().add(getResource("json-keywords.css").toExternalForm());
        contentPane.getChildren().addAll(hBox, inputTextArea, outputLabel, outputTextArea, formatButton, generateJavaBeanButton);
    }

    private URL getResource(String image) {
        return getClass().getClassLoader().getResource(image);
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
        try {
            String content = Files.readString(java.nio.file.Paths.get(filePath));
            setContent(content);
        } catch (IOException e) {
            ExceptionHandler.handleException(e, "文件打开失败", "无法打开文件", 
                    "无法读取文件: " + filePath + "\n错误: " + e.getMessage());
        }
    }

    @Override
    public void save(String filePath) {
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(filePath), 
                    outputTextArea.getText().getBytes(java.nio.charset.StandardCharsets.UTF_8));
        } catch (IOException e) {
            ExceptionHandler.handleException(e, "文件保存失败", "无法保存文件", 
                    "无法保存文件: " + filePath + "\n错误: " + e.getMessage());
        }
    }

    @Override
    public String getContent() {
        return inputTextArea.getText();
    }

    @Override
    public void setContent(String content) {
        inputTextArea.clear();
        inputTextArea.replaceText(0,0,content);
    }


    private void formatContent() {
        String jsonText = inputTextArea.getText();
        try {
            String tempJson = jsonText.replaceAll("\",","\",\n");
            tempJson = tempJson.replaceAll("\",\n\n","\",\n");
            setContent(tempJson);
            if(checkBox.isSelected()){
                tempJson = jsonText.replace("\\\"", "\"");
                tempJson = tempJson.replace("\\n", "");
            }
//            JsonElement jsonElement = gson.fromJson(tempJson, JsonElement.class);
//            String formattedJson = gson.toJson(jsonElement);
            String formattedJson = validateJson(tempJson);
            outputTextArea.clear();
            outputTextArea.replaceText(0,0,formattedJson);
            // 设置样式高亮
            StyleSpans<Collection<String>> styleSpans = JsonHighlighting.highlight(formattedJson);
            outputTextArea.setStyleSpans(0, styleSpans);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("JSON格式化出错");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    private String validateJson(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            return jsonNode.toPrettyString();
        } catch (com.fasterxml.jackson.core.JsonParseException e) {
            int lineNumber = e.getLocation().getLineNr();
            int columnNumber = e.getLocation().getColumnNr();
            String errorMessage = e.getOriginalMessage();
            computeHighlighting(inputTextArea,lineNumber,columnNumber);
            return String.format("JSON格式错误，在第 %d 行，第 %d 列：%s", lineNumber, columnNumber, errorMessage);
        } catch (Exception e) {
            return "其他错误：" + e.getMessage();
        }
    }

    private void computeHighlighting(CodeArea inputTextArea, int targetLine, int columnNumber) {
        // 计算要突出显示的行和字符范围的样式
        String inputText = inputTextArea.getText();
        // 按行分割文本
        String[] lines = inputText.split("\n");
        String searchLine = lines[targetLine-1];
        Integer addStringSize = 5;
        String searchText = StringUtils.substring(searchLine,Math.max(columnNumber-addStringSize,0), columnNumber+addStringSize);
        inputTextArea.clearStyle(0, inputTextArea.getLength());
        int startPos = 0;
        String text = inputTextArea.getText();
        int foundPos = text.indexOf(searchText, startPos);
        if (foundPos == -1) {
            return;
        }
        inputTextArea.setStyleClass(foundPos, foundPos + searchText.length(), "red");
        startPos = foundPos + 1;
        inputTextArea.moveTo(startPos);
//        inputTextArea.requestFocus();
        // 使光标出现在指定位置
        inputTextArea.requestFollowCaret();
    }

    private void generateJavaBean() {
        String jsonText = inputTextArea.getText();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonText);
            if (rootNode.isObject()) {
                String javaBeanCode = JsonToBean.generateJavaBeanCode((ObjectNode) rootNode);
                outputTextArea.replaceText(0,0,javaBeanCode);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText("生成JavaBean失败");
                alert.setContentText("输入的JSON数据必须是一个对象。");
                alert.showAndWait();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("生成JavaBean出错");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}