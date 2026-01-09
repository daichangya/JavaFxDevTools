package com.daicy.devtools;

import java.nio.file.Path;

import javafx.scene.layout.Pane;

/**
 * 插件接口，定义插件必须实现的方法
 * 
 * <p>所有 DevTools 插件都必须实现此接口。插件可以提供自定义的 UI 界面，
 * 处理特定类型的文件，实现特定的功能（如格式化、转换等）。
 * 
 * @author daicy
 */
public interface TextPlugin {
    /**
     * 获取插件的 UI 面板
     * 
     * @return 插件的根面板，包含所有 UI 组件
     */
    Pane getContentPane();
    
    /**
     * 获取插件的默认文件路径
     * 
     * @return 默认文件路径，如果不需要默认路径，返回 null
     */
    Path getDefaultPath();
    
    /**
     * 打开文件
     * 
     * @param filePath 要打开的文件路径
     */
    void open(String filePath);
    
    /**
     * 保存文件
     * 
     * @param filePath 要保存的文件路径
     */
    void save(String filePath);
    
    /**
     * 获取当前编辑的内容
     * 
     * @return 当前内容
     */
    String getContent();
    
    /**
     * 设置要显示的内容
     * 
     * @param content 要设置的内容
     */
    void setContent(String content);
    
    /**
     * 初始化插件
     * 
     * <p>在插件被安装后调用，用于执行初始化操作。
     * 默认实现为空，子类可以重写此方法。
     */
    default void initialize() {
        // 默认实现为空
    }
    
    /**
     * 销毁插件
     * 
     * <p>在插件被卸载前调用，用于清理资源（如关闭线程池、释放文件句柄等）。
     * 默认实现为空，子类应该重写此方法以正确清理资源。
     */
    default void destroy() {
        // 默认实现为空
    }
}