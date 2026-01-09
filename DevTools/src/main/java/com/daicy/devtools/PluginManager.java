package com.daicy.devtools;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 插件管理器，负责插件的加载、安装和卸载
 * 
 * <p>从 plugins.json 配置文件中读取插件列表，通过反射加载插件类并创建实例。
 * 管理已安装插件列表，提供插件查询接口。
 * 
 * @author daicy
 */
class PluginManager {
    private static final Logger logger = LoggerFactory.getLogger(PluginManager.class);
    
    private final ObservableList<TextPlugin> installedPlugins = FXCollections.observableArrayList();
    private final ObservableList<String> availablePluginClassNames = FXCollections.observableArrayList();
    private final Map<String, Boolean> pluginInstallationStatus;

    public PluginManager() {
        // 初始化插件安装状态映射，默认都为空
        // 实际可用插件类名将在 loadPlugins() 时从配置文件加载
        pluginInstallationStatus = new HashMap<>();
    }


    public void loadPlugins() {
        // 从资源文件中读取 plugins.json（支持 JAR 文件中的资源）
        try (java.io.InputStream inputStream = getClass().getClassLoader().getResourceAsStream("plugins.json")) {
            if (inputStream == null) {
                logger.error("插件配置文件不存在: plugins.json");
                return;
            }
            
            ObjectMapper objectMapper = new ObjectMapper();
            String json = new String(inputStream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
            List<PluginInfo> pluginInfos = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, PluginInfo.class));
            
            // 更新可用插件类名列表
            availablePluginClassNames.clear();
            for (PluginInfo pluginInfo : pluginInfos) {
                String pluginClassName = pluginInfo.getPluginClass();
                availablePluginClassNames.add(pluginClassName);
                // 初始化插件安装状态映射
                if (!pluginInstallationStatus.containsKey(pluginClassName)) {
                    pluginInstallationStatus.put(pluginClassName, false);
                }
            }
            
            for (PluginInfo pluginNode : pluginInfos) {
                String pluginClassName = pluginNode.getPluginClass();
                try {
                    // 通过反射加载插件类
                    Class<?> pluginClass = Class.forName(pluginClassName);
                    if (TextPlugin.class.isAssignableFrom(pluginClass)) {
                        TextPlugin plugin = (TextPlugin) pluginClass.getDeclaredConstructor().newInstance();
                        installPlugin(plugin);
                    } else {
                        logger.warn("{} 不是合法的TextPlugin实现类", pluginClassName);
                    }
                } catch (Exception e) {
                    logger.error("加载插件 {} 时出错", pluginClassName, e);
                }
            }
        } catch (IOException e) {
            logger.error("读取插件配置文件出错", e);
        }
    }

    public void installPlugin(TextPlugin plugin) {
        if (plugin != null) {
            // 在安装后调用插件的初始化方法
            plugin.initialize();
            installedPlugins.add(plugin);
            String pluginClassName = plugin.getClass().getName();
            pluginInstallationStatus.put(pluginClassName, true);
        }
    }

    public void uninstallPlugin(TextPlugin plugin) {
        if (plugin != null) {
            // 在卸载前调用插件的销毁方法以清理资源
            plugin.destroy();
            installedPlugins.remove(plugin);
            String pluginClassName = plugin.getClass().getName();
            pluginInstallationStatus.put(pluginClassName, false);
        }
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
    
    /**
     * 根据类名获取插件类
     * 
     * @param className 插件类的完整类名
     * @return 插件类，如果不存在则返回 null
     */
    public Class<? extends TextPlugin> getPluginClass(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            if (TextPlugin.class.isAssignableFrom(clazz)) {
                return clazz.asSubclass(TextPlugin.class);
            }
        } catch (ClassNotFoundException e) {
            // 类不存在
        }
        return null;
    }
    
    /**
     * 创建新的插件实例
     * 
     * <p>为每个标签页创建独立的插件实例，避免多个标签页共享同一个 contentPane。
     * 
     * @param pluginClass 插件类
     * @return 新的插件实例，如果创建失败返回 null
     */
    public TextPlugin createNewPluginInstance(Class<? extends TextPlugin> pluginClass) {
        try {
            TextPlugin plugin = pluginClass.getDeclaredConstructor().newInstance();
            plugin.initialize();
            return plugin;
        } catch (Exception e) {
            logger.error("创建插件实例失败: {}", pluginClass.getName(), e);
            return null;
        }
    }
    
    /**
     * 根据插件实例获取其类
     * 
     * @param plugin 插件实例
     * @return 插件类
     */
    public Class<? extends TextPlugin> getPluginClass(TextPlugin plugin) {
        @SuppressWarnings("unchecked")
        Class<? extends TextPlugin> pluginClass = (Class<? extends TextPlugin>) plugin.getClass();
        return pluginClass;
    }
}
