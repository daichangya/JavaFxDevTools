package com.daicy.devtools.plugin.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;

import org.apache.commons.lang3.SystemUtils;

/**
 * Hosts 文件工具类
 * 
 * <p>提供读取和保存系统 hosts 文件的功能。
 * 
 * @author daicy
 */
public class HostsUtil {
    // sudo password
    private static String sudoPwd = null;
    
    public static String getHostsContent(){
        String fileName = null;
        String fileContent = "";
        
        if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_UNIX){
            fileName = "/etc/hosts";
        } else if(SystemUtils.IS_OS_WINDOWS){
            fileName = "C:\\Windows\\System32\\drivers\\etc\\hosts";
        } else {
            return "未知系统";
        }
        
        try {
            fileContent = Files.readString(Path.of(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }
    
    public static Boolean saveHostsContent(String fileContent){
        File file = HostsUtil.getHostsFile();
        
        if(file == null){
            return false;
        } else {
            try {
                if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_UNIX){
                    if(sudoPwd == null){
                        TextInputDialog dialog = new TextInputDialog("");
                        dialog.setTitle("Sudo Dialog");
                        dialog.setHeaderText("Look, a Sudo Input Dialog");
                        dialog.setContentText("Please enter your sudo pwd:");

                        Optional<String> result = dialog.showAndWait();

                        result.ifPresent(pwd -> sudoPwd = pwd);
                        
                        String[] cmds = {"/bin/bash", "-c", "echo \"" + sudoPwd + "\" | " + "sudo" + " -S " + " chmod 777 " + file};
                        Process process = Runtime.getRuntime().exec(cmds);
                        
                        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getErrorStream()));
                        String line;
                        Boolean isSucc = true;
                        while((line=bufferedReader.readLine())!=null)
                        {
                            if(line.contains("try again")){
                                isSucc = false;
                            }
                        }
                        
                        if(!isSucc){
                            sudoPwd = null;
                            
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Wrong Password");
                            alert.setHeaderText("Wrong Password");
                            alert.setContentText("Please try agian");
                            alert.showAndWait();
                            
                            return false;
                        }
                    }
                }

                Files.writeString(file.toPath(), fileContent, StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }
    }
    
    public static File getHostsFile(){
        String fileName = null;
        if(SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_MAC){
            fileName = "/etc/hosts";
            return new File(fileName);
        } else if(SystemUtils.IS_OS_WINDOWS){
            fileName = System.getenv("windir") + "\\System32\\drivers\\etc\\hosts";
            return new File(fileName);
        } else {
            System.out.println("未知系统");
            return null;
        }
    }
}
