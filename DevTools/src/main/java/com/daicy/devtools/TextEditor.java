package com.daicy.devtools;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daicy.core.ExceptionHandler;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * DevTools 主应用程序类
 * 
 * <p>插件化的文本编辑器，支持多种文件类型的编辑和处理。
 * 采用插件架构，可以动态加载和管理插件。
 * 
 * @author daicy
 */
public class TextEditor extends Application {
    
    private static final Logger logger = LoggerFactory.getLogger(TextEditor.class);

    private final TabPane tabPane = new TabPane();
    private final MenuBar menuBar = new MenuBar();
    private ResourceBundle bundle;
    private FlowPane sideButtonsPane;
    private final ObservableMap<String, String> recentFilesMap = FXCollections.observableHashMap();
    private PluginManager pluginManager = new PluginManager();
    // 存储标签页与插件实例的映射关系，确保每个标签页有独立的插件实例
    private final Map<Tab, TextPlugin> tabPluginMap = new HashMap<>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
//        System.setProperty("javafx.application.css", "javafx.scene.control.skin.ModenaSkin");
//        Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
//        Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        Locale.setDefault(Locale.ENGLISH);
        bundle = ResourceBundle.getBundle("TextEditor", Locale.getDefault());
        pluginManager.loadPlugins();

        Menu fileMenu = createFileMenu();
        Menu helpMenu = createHelpMenu();
        Menu settingsMenu = createSettingsMenu();

        menuBar.getMenus().addAll(fileMenu, helpMenu, settingsMenu);

        sideButtonsPane = createSideButtonsPaneWithPlugins();

        BorderPane borderPane = new BorderPane();
        borderPane.setTop(menuBar);
        borderPane.setLeft(sideButtonsPane);
        borderPane.setCenter(tabPane);

        Scene scene = new Scene(borderPane, 800, 600);
        primaryStage.setTitle(bundle.getString("app.title"));
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image("zthinker.png"));
        // 设置窗口默认最大化
        primaryStage.setMaximized(true);
        primaryStage.show();

        createNewTab();
    }

    private Menu createFileMenu() {
        Menu fileMenu = new Menu();
        fileMenu.setId("file.menu");
        fileMenu.setText(bundle.getString("file.menu"));

        MenuItem newItem = new MenuItem();
        newItem.setId("file.new");
        newItem.setText(bundle.getString("file.new"));
        newItem.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        newItem.setOnAction(event -> createNewTab());

        MenuItem openItem = new MenuItem();
        openItem.setId("file.open");
        openItem.setText(bundle.getString("file.open"));
        openItem.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openItem.setOnAction(event -> openFile());

        MenuItem saveItem = new MenuItem();
        saveItem.setId("file.save");
        saveItem.setText(bundle.getString("file.save"));
        saveItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        saveItem.setOnAction(event -> saveCurrentFile());

        MenuItem saveAsItem = new MenuItem();
        saveAsItem.setId("file.saveAs");
        saveAsItem.setText(bundle.getString("file.saveAs"));
        saveAsItem.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN));
        saveAsItem.setOnAction(event -> saveCurrentFileAs());

        MenuItem exitItem = new MenuItem();
        exitItem.setId("file.exit");
        exitItem.setText(bundle.getString("file.exit"));
        exitItem.setAccelerator(new KeyCodeCombination(KeyCode.Q, KeyCombination.CONTROL_DOWN));
        exitItem.setOnAction(event -> System.exit(0));

        fileMenu.getItems().addAll(newItem, openItem, saveItem, saveAsItem, new SeparatorMenuItem(), exitItem);

        return fileMenu;
    }

    private Menu createHelpMenu() {
        Menu helpMenu = new Menu();
        helpMenu.setId("help.menu");
        helpMenu.setText(bundle.getString("help.menu"));

        MenuItem aboutItem = new MenuItem();
        aboutItem.setId("help.about");
        aboutItem.setText(bundle.getString("help.about"));
        aboutItem.setOnAction(event -> showAbout());

        helpMenu.getItems().add(aboutItem);

        return helpMenu;
    }

    private Menu createSettingsMenu() {
        Menu settingsMenu = new Menu();
        settingsMenu.setId("settings.menu");
        settingsMenu.setText(bundle.getString("settings.menu"));

        MenuItem pluginSettingsItem = new MenuItem();
        pluginSettingsItem.setId("settings.plugin");
        pluginSettingsItem.setText(bundle.getString("settings.plugin"));
        pluginSettingsItem.setOnAction(event -> showPluginSettingsWindow());

        MenuItem languageSettingsItem = new MenuItem();
        languageSettingsItem.setId("settings.language");
        languageSettingsItem.setText(bundle.getString("settings.language"));
        languageSettingsItem.setOnAction(event -> showLanguageSettingsWindow());

        settingsMenu.getItems().add(pluginSettingsItem);
        settingsMenu.getItems().add(languageSettingsItem);

        return settingsMenu;
    }

    private FlowPane createSideButtonsPaneWithPlugins() {
        FlowPane flowPane = new FlowPane(Orientation.VERTICAL);
        flowPane.setPrefWidth(50);
        flowPane.setHgap(5);
        flowPane.setVgap(5);
        // 创建插件按钮列表
        List<TextPlugin> plugins = pluginManager.getPlugins();
        ObservableList<Button> pluginButtons = FXCollections.observableArrayList();
        for (TextPlugin plugin : plugins) {
            Button pluginButton = new Button();;
            Image actionImage = new Image(getClass().getClassLoader().getResourceAsStream("zthinker.png"));
            ImageView actionImageView = new ImageView(actionImage);
            actionImageView.setFitWidth(30);
            actionImageView.setFitHeight(30);
            pluginButton.setGraphic(actionImageView);
            pluginButton.setMnemonicParsing(false);
            Tooltip tooltip = new Tooltip(plugin.getClass().getSimpleName());
            pluginButton.setTooltip(tooltip);
            pluginButton.setOnAction(event -> {
                selectedPlugin = plugin;
                updatePluginButtonStyle(pluginButtons, pluginButton);
                // 点击插件按钮时，创建新标签并加载插件界面
                createPluginTab();
            });
            pluginButtons.add(pluginButton);
            flowPane.getChildren().add(pluginButton);
        }

        return flowPane;
    }

    private void createPluginTab() {
        if (selectedPlugin != null) {
            if (null != selectedPlugin.getDefaultPath()) {
                openByFile(selectedPlugin.getDefaultPath().toFile());
            } else {
                // 为每个标签页创建新的插件实例，避免共享 contentPane
                Class<? extends TextPlugin> pluginClass = pluginManager.getPluginClass(selectedPlugin);
                TextPlugin newPluginInstance = pluginManager.createNewPluginInstance(pluginClass);
                
                if (newPluginInstance != null) {
                    Tab tab = new Tab(selectedPlugin.getClass().getSimpleName());
                    tab.setContent(newPluginInstance.getContentPane());
                    // 存储标签页与插件实例的映射
                    tabPluginMap.put(tab, newPluginInstance);
                    // 添加标签页关闭事件处理，清理插件实例
                    tab.setOnClosed(event -> {
                        TextPlugin plugin = tabPluginMap.remove(tab);
                        if (plugin != null) {
                            plugin.destroy();
                        }
                        recentFilesMap.remove(tab.getText());
                    });
                    tabPane.getTabs().add(tab);
                    tabPane.getSelectionModel().select(tab);
                } else {
                    ExceptionHandler.handleException(
                        new RuntimeException("无法创建插件实例"),
                        "创建标签页失败",
                        "无法创建新的插件实例",
                        "插件类: " + selectedPlugin.getClass().getName()
                    );
                }
            }
        }
    }

    private void createNewTab() {
        Tab tab = new Tab();
        tab.setId("untitled.tab");
        tab.setText(bundle.getString("untitled.tab"));
        tab.setContent(createEmptyTabContent());
        tab.setOnClosed(event -> {
            recentFilesMap.remove(tab.getText());
        });
        tabPane.getTabs().add(tab);
    }

    private Pane createEmptyTabContent() {
    // 创建一个WebView，用于展示网页
    WebView webView = new WebView();
    webView.setPrefSize(800, 600); // 设置默认大小
    // 或者根据父容器设置宽高，确保它随着父容器大小变化
    webView.setMaxWidth(Double.MAX_VALUE);
    webView.setMaxHeight(Double.MAX_VALUE);
    // 获取WebView对应的WebEngine，用于加载网页等操作
    WebEngine webEngine = webView.getEngine();
    // 设置 User-Agent，模拟平板浏览器
    webEngine.setUserAgent("Mozilla/5.0 (iPad; CPU OS 14_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/14.0 Mobile/15E148 Safari/604.1");
    // 设置 User-Agent，模拟 iPhone 浏览器
//    String iphoneUserAgent = "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/16.0 Mobile/15E148 Safari/604.1";
//    webEngine.setUserAgent(iphoneUserAgent);
    // 启用 JavaScript 支持
    webEngine.setJavaScriptEnabled(true);

        // 使用WebEngine加载百度首页
    webEngine.load("https://jsdiff.com");
    // 创建一个垂直布局容器VBox，将WebView添加进去
    VBox vbox = new VBox(webView);
    return vbox;
}


    private TextPlugin selectedPlugin;

    private void openFile() {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile!= null) {
            if (selectedPlugin!= null) {
                openByFile(selectedFile);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("提示");
                alert.setHeaderText("请先选择一个插件");
                alert.setContentText("您需要选择一个合适的插件来打开此文件。");
                alert.showAndWait();
            }
        }
    }

    private void openByFile(File selectedFile) {
        if (selectedPlugin == null || selectedFile == null) {
            return;
        }
        try {
            // 为每个文件创建新的插件实例，避免共享 contentPane
            Class<? extends TextPlugin> pluginClass = pluginManager.getPluginClass(selectedPlugin);
            TextPlugin newPluginInstance = pluginManager.createNewPluginInstance(pluginClass);
            
            if (newPluginInstance != null) {
                newPluginInstance.open(selectedFile.getAbsolutePath());
                Tab tab = new Tab(selectedFile.getName());
                tab.setId(selectedFile.getName());
                tab.setContent(newPluginInstance.getContentPane());
                // 存储标签页与插件实例的映射
                tabPluginMap.put(tab, newPluginInstance);
                // 添加标签页关闭事件处理，清理插件实例
                tab.setOnClosed(event -> {
                    TextPlugin plugin = tabPluginMap.remove(tab);
                    if (plugin != null) {
                        plugin.destroy();
                    }
                    recentFilesMap.remove(tab.getText());
                });
                tabPane.getTabs().add(tab);
                tabPane.getSelectionModel().select(tab);
                recentFilesMap.put(tab.getText(), selectedFile.getAbsolutePath());
            } else {
                ExceptionHandler.handleException(
                    new RuntimeException("无法创建插件实例"),
                    "文件打开失败",
                    "无法创建新的插件实例",
                    "插件类: " + selectedPlugin.getClass().getName()
                );
            }
        } catch (Exception e) {
            ExceptionHandler.handleException(e, "文件打开失败", "无法打开文件", 
                    "无法打开文件: " + selectedFile.getAbsolutePath() + "\n错误: " + e.getMessage());
        }
    }

    private void saveCurrentFile() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab != null) {
            String filePath = recentFilesMap.get(selectedTab.getText());
            // 从映射中获取当前标签页对应的插件实例
            TextPlugin plugin = tabPluginMap.get(selectedTab);
            if (filePath != null && plugin != null) {
                try {
                    plugin.save(filePath);
                } catch (Exception e) {
                    ExceptionHandler.handleException(e, "文件保存失败", "无法保存文件", 
                            "无法保存文件: " + filePath + "\n错误: " + e.getMessage());
                }
            } else {
                saveCurrentFileAs();
            }
        }
    }

    private void saveCurrentFileAs() {
        Tab selectedTab = tabPane.getSelectionModel().getSelectedItem();
        if (selectedTab == null) {
            return;
        }
        // 从映射中获取当前标签页对应的插件实例
        TextPlugin plugin = tabPluginMap.get(selectedTab);
        if (plugin == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText("无法保存");
            alert.setContentText("当前标签页没有关联的插件实例。");
            alert.showAndWait();
            return;
        }
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile == null) {
            return;
        }
        try {
            plugin.save(selectedFile.getAbsolutePath());
            recentFilesMap.put(selectedTab.getText(), selectedFile.getAbsolutePath());
            selectedTab.setText(selectedFile.getName());
        } catch (Exception e) {
            ExceptionHandler.handleException(e, "文件保存失败", "无法保存文件", 
                    "无法保存文件: " + selectedFile.getAbsolutePath() + "\n错误: " + e.getMessage());
        }
    }

    private void saveTextToFile(File file, String text) {
        try {
            java.nio.file.Files.write(java.nio.file.Paths.get(file.getAbsolutePath()), 
                    text.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        } catch (java.io.IOException e) {
            ExceptionHandler.handleException(e, "文件保存失败", "无法保存文件", 
                    "无法保存文件: " + file.getAbsolutePath() + "\n错误: " + e.getMessage());
        }
    }

    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(bundle.getString("about.title"));
        alert.setHeaderText(bundle.getString("about.header"));
        alert.setContentText(bundle.getString("about.content"));
        alert.showAndWait();
    }


    private void showPluginSettingsWindow() {
        Stage pluginSettingsStage = new Stage();
        pluginSettingsStage.initModality(Modality.APPLICATION_MODAL);
        pluginSettingsStage.setTitle(bundle.getString("settings.plugin.title"));

        VBox vbox = new VBox(10);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        Label pluginLabel = new Label();
        pluginLabel.setId("settings.plugin.label");
        pluginLabel.setText(bundle.getString("settings.plugin.label"));

        // 用于展示已安装插件列表及状态
        ListView<String> pluginListView = new ListView<>();
        ObservableList<String> pluginListItems = FXCollections.observableArrayList();
        pluginListView.setItems(pluginListItems);
        pluginListView.setPrefHeight(150);
        updatePluginListView(pluginListView); // 调用方法更新插件列表显示

        // 记录当前选中的插件列表项索引
        AtomicReference<Integer> selectedIndex = new AtomicReference<>();
        pluginListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!= null) {
                selectedIndex.set(pluginListView.getSelectionModel().getSelectedIndex());
            }
        });

        Button installPluginButton = new Button();
        installPluginButton.setId("settings.plugin.install");
        installPluginButton.setText(bundle.getString("settings.plugin.install"));
        installPluginButton.setOnAction(event -> {
            if (selectedIndex.get() != null && selectedIndex.get() >= 0 && selectedIndex.get() < pluginListItems.size()) {
                String selectedPluginClassName = pluginListItems.get(selectedIndex.get()).split(" - ")[0];
                try {
                    // 使用完整类名，不再硬编码包名
                    Class<?> pluginClass = Class.forName(selectedPluginClassName);
                    if (TextPlugin.class.isAssignableFrom(pluginClass)) {
                        if (!pluginManager.isPluginInstalled((Class<? extends TextPlugin>) pluginClass)) {
                            TextPlugin plugin = (TextPlugin) pluginClass.getDeclaredConstructor().newInstance();
                            pluginManager.installPlugin(plugin);
                            updatePluginButtons();
                            updatePluginListView(pluginListView);
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("提示");
                            alert.setHeaderText("插件已安装");
                            alert.setContentText(selectedPluginClassName + " 插件已经安装，无需重复安装。");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("错误");
                        alert.setHeaderText("无效插件");
                        alert.setContentText(selectedPluginClassName + " 不是合法的TextPlugin实现类。");
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("错误");
                    alert.setHeaderText("插件安装失败");
                    alert.setContentText("安装 " + selectedPluginClassName + " 插件时出现错误: " + e.getMessage());
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("提示");
                alert.setHeaderText("未选中插件");
                alert.setContentText("请先在插件列表中选中要安装的插件。");
                alert.showAndWait();
            }
        });

        Button uninstallPluginButton = new Button();
        uninstallPluginButton.setId("settings.plugin.uninstall");
        uninstallPluginButton.setText(bundle.getString("settings.plugin.uninstall"));
        uninstallPluginButton.setOnAction(event -> {
            if (selectedIndex.get() != null && selectedIndex.get() >= 0 && selectedIndex.get() < pluginListItems.size()) {
                String selectedPluginClassName = pluginListItems.get(selectedIndex.get()).split(" - ")[0];
                try {
                    // 使用完整类名，不再硬编码包名
                    Class<?> pluginClass = Class.forName(selectedPluginClassName);
                    if (TextPlugin.class.isAssignableFrom(pluginClass)) {
                        if (pluginManager.isPluginInstalled((Class<? extends TextPlugin>) pluginClass)) {
                            for (TextPlugin plugin : pluginManager.getPlugins()) {
                                if (plugin.getClass().equals(pluginClass)) {
                                    pluginManager.uninstallPlugin(plugin);
                                    updatePluginButtons();
                                    updatePluginListView(pluginListView);
                                    break;
                                }
                            }
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("提示");
                            alert.setHeaderText("插件未安装");
                            alert.setContentText(selectedPluginClassName + " 插件尚未安装，无法卸载。");
                            alert.showAndWait();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("错误");
                        alert.setHeaderText("无效插件");
                        alert.setContentText(selectedPluginClassName + " 不是合法的TextPlugin实现类。");
                        alert.showAndWait();
                    }
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("错误");
                    alert.setHeaderText("插件卸载失败");
                    alert.setContentText("卸载 " + selectedPluginClassName + " 插件时出现错误: " + e.getMessage());
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("提示");
                alert.setHeaderText("未选中插件");
                alert.setContentText("请先在插件列表中选中要卸载的插件。");
                alert.showAndWait();
            }
        });

        vbox.getChildren().addAll(pluginLabel, pluginListView, installPluginButton, uninstallPluginButton);

        Scene scene = new Scene(vbox, 300, 200);
        pluginSettingsStage.setScene(scene);
        pluginSettingsStage.show();
    }

    // 用于更新插件列表显示的方法，根据安装状态展示插件
    private void updatePluginListView(ListView<String> pluginListView) {
        ObservableList<String> pluginListItems = pluginListView.getItems();
        pluginListItems.clear();
        List<String> availablePluginClassNames = pluginManager.getAvailablePluginClassNames();
        Map<String, Boolean> installationStatus = pluginManager.getPluginInstallationStatus();
        for (String pluginClassName : availablePluginClassNames) {
            String status = installationStatus.get(pluginClassName)? "已安装" : "未安装";
            pluginListItems.add(pluginClassName + " - " + status);
        }
    }

    private void updatePluginButtons() {
        FlowPane flowPane = sideButtonsPane;
        if (flowPane!= null) {
            flowPane.getChildren().removeIf(node -> node instanceof Button && ((Button) node).getText().startsWith("插件名称前缀"));

            List<TextPlugin> plugins = pluginManager.getPlugins();
            ObservableList<Button> pluginButtons = FXCollections.observableArrayList();
            for (TextPlugin plugin : plugins) {
                Button pluginButton = new Button(plugin.getClass().getSimpleName());
                pluginButton.setOnAction(event -> {
                    selectedPlugin = plugin;
                    updatePluginButtonStyle(pluginButtons, pluginButton);
                });
                pluginButtons.add(pluginButton);
                flowPane.getChildren().add(pluginButton);
            }
        }
    }

    private void showLanguageSettingsWindow() {
        Stage languageSettingsStage = new Stage();
        languageSettingsStage.initModality(Modality.APPLICATION_MODAL);
        languageSettingsStage.setTitle(bundle.getString("settings.language.title"));

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        Label languageLabel = new Label();
        languageLabel.setId("settings.language.label");
        languageLabel.setText(bundle.getString("settings.language.label"));

        ComboBox<String> languageComboBox = new ComboBox<>();
        languageComboBox.getItems().addAll("English", "中文");
        languageComboBox.setValue("English");
        languageComboBox.setOnAction(event -> {
            String selectedLanguage = languageComboBox.getValue();
            if ("中文".equals(selectedLanguage)) {
                Locale.setDefault(Locale.SIMPLIFIED_CHINESE);
            } else {
                Locale.setDefault(Locale.ENGLISH);
            }
            reloadResourceBundle();
            updateUI();
        });

        vbox.getChildren().addAll(languageLabel, languageComboBox);

        Scene scene = new Scene(vbox, 200, 150);
        languageSettingsStage.setScene(scene);
        languageSettingsStage.show();
    }

    private void reloadResourceBundle() {
        bundle = ResourceBundle.getBundle("TextEditor", Locale.getDefault());
    }

    private void updateUI() {
        menuBar.getMenus().forEach(menu -> {
            if(null != menu.getId()){
                menu.setText(bundle.getString(menu.getId()));
            }
            menu.getItems().forEach(menuItem -> {
                if(null != menuItem.getId()){
                    menuItem.setText(bundle.getString(menuItem.getId()));
                }
            });
        });

        sideButtonsPane.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button button = (Button) node;
                if(null != button.getId()){
                    button.setText(bundle.getString(button.getId()));
                }
            }
        });

        tabPane.getTabs().forEach(tab -> {
            if(null != tab.getId()){
                tab.setText(bundle.getString(tab.getId()));
            }
        });

        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.setTitle(bundle.getString("app.title"));
    }

    private void updatePluginButtonStyle(ObservableList<Button> pluginButtons, Button clickedButton) {
        for (Button button : pluginButtons) {
            if (button == clickedButton) {
                button.getStyleClass().add("selected-plugin-button");
            } else {
                button.getStyleClass().remove("selected-plugin-button");
            }
        }
    }
}