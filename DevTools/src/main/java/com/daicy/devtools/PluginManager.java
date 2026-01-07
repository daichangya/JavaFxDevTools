package com.daicy.devtools;


import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 插件管理器，负责插件的加载、安装和卸载
 * 
 * <p>从 plugins.json 配置文件中读取插件列表，通过反射加载插件类并创建实例。
 * 管理已安装插件列表，提供插件查询接口。
 * 
 * @author daicy
 */
class PluginManager {
    private final ObservableList<TextPlugin> installedPlugins = FXCollections.observableArrayList();
    private final ObservableList<String> availablePluginClassNames = FXCollections.observableArrayList();
    private final Map<String, Boolean> pluginInstallationStatus;

    public PluginManager() {
        // 初始化时获取所有可用插件的类名，这里可以从配置文件等地方加载，示例中简单模拟几个类名
        availablePluginClassNames.addAll(List.of("JsonFormatPlugin", "MarkdownEditorPlugin"));
        // 初始化插件安装状态映射，默认都为未安装
        pluginInstallationStatus = availablePluginClassNames.stream()
                .collect(Collectors.toMap(className -> className, className -> false));
    }

    private URL getResource(String image) {
        return getClass().getClassLoader().getResource(image);
    }

    public void loadPlugins() {
            // 定义配置文件路径，这里假设配置文件在项目根目录下名为plugins.json，你可以根据实际情况调整路径
            String configFilePath = getResource("plugins.json").getPath();
            System.out.println(configFilePath);
            File configFile = new File(configFilePath);
            if (configFile.exists()) {
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    String json = Files.readString(Path.of(configFilePath));
                    List<PluginInfo> pluginInfos = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, PluginInfo.class));
                    for (PluginInfo pluginNode : pluginInfos) {
                        String pluginClassName = pluginNode.getPluginClass();
                        try {
                            // 通过反射加载插件类
                            Class<?> pluginClass = Class.forName(pluginClassName);
                            if (TextPlugin.class.isAssignableFrom(pluginClass)) {
                                TextPlugin plugin = (TextPlugin) pluginClass.getDeclaredConstructor().newInstance();
                                installPlugin(plugin);
                            } else {
                                System.err.println(pluginClassName + " 不是合法的TextPlugin实现类");
                            }
                        } catch (Exception e) {
                            System.err.println("加载插件 " + pluginClassName + " 时出错: " + e.getMessage());
                        }
                    }
                } catch (IOException e) {
                    System.err.println("读取插件配置文件出错: " + e.getMessage());
                }
            } else {
                System.err.println("插件配置文件不存在");
            }

    }

    public void installPlugin(TextPlugin plugin) {
        installedPlugins.add(plugin);
        pluginInstallationStatus.put(plugin.getClass().getSimpleName(), true);
    }

    public void uninstallPlugin(TextPlugin plugin) {
        installedPlugins.remove(plugin);
        pluginInstallationStatus.put(plugin.getClass().getSimpleName(), false);
    }

    public List<TextPlugin> getPlugins() {
        return installedPlugins;
    }

    public boolean isPluginInstalled(Class<? extends TextPlugin> pluginClass) {
        return installedPlugins.stream().anyMatch(plugin -> plugin.getClass().equals(pluginClass));
    }

    public ObservableList<String> getAvailablePluginClassNames() {
        return availablePluginClassNames;
    }

    public Map<String, Boolean> getPluginInstallationStatus() {
        return pluginInstallationStatus;
    }
}
