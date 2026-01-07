package com.daicy.devtools;

/**
 * 插件信息类，用于从 JSON 配置文件读取插件信息
 * 
 * @author daicy
 */
public class PluginInfo {
    private String pluginClass;
    public void setPluginClass(String pluginClass) {
        this.pluginClass = pluginClass;
    }
    public String getPluginClass() {
        return pluginClass;
    }
}
