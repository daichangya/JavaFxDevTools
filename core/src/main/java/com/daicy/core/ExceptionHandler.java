package com.daicy.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.control.Alert;

/**
 * 异常处理工具类
 * 
 * <p>提供统一的异常处理和日志记录功能。
 * 
 * @author daicy
 */
public class ExceptionHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
    
    /**
     * 记录异常日志并显示用户友好的错误提示
     * 
     * @param exception 异常对象
     * @param title 错误对话框标题
     * @param header 错误对话框头部文本
     * @param userMessage 显示给用户的友好错误消息
     */
    public static void handleException(Exception exception, String title, String header, String userMessage) {
        // 记录详细错误日志
        logger.error("{}: {}", header, exception.getMessage(), exception);
        
        // 显示用户友好的错误提示
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(userMessage != null ? userMessage : exception.getMessage());
        alert.showAndWait();
    }
    
    /**
     * 记录异常日志（不显示对话框）
     * 
     * @param exception 异常对象
     * @param message 日志消息
     */
    public static void logException(Exception exception, String message) {
        logger.error("{}: {}", message, exception.getMessage(), exception);
    }
    
    /**
     * 记录警告日志
     * 
     * @param message 警告消息
     */
    public static void logWarning(String message) {
        logger.warn(message);
    }
    
    /**
     * 记录信息日志
     * 
     * @param message 信息消息
     */
    public static void logInfo(String message) {
        logger.info(message);
    }
    
    /**
     * 记录调试日志
     * 
     * @param message 调试消息
     */
    public static void logDebug(String message) {
        logger.debug(message);
    }
}

