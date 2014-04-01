
package com.dsdev.moddle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Greenlock28
 */
public class Settings {
    
    public Settings() {
        //Nothing here yet.
    }
    
    public boolean loadSettingsFromFile(String path) {
        try {
            
            JSONObject jsonFile = (JSONObject)JSONValue.parse(FileUtils.readFileToString(new File(path)));
            JSONArray settingsArray = (JSONArray)jsonFile.get("settings");

            for (Object obj : settingsArray) {
                JSONObject setting = (JSONObject)obj;
                if (((String)setting.get("func")).equalsIgnoreCase("setMemory")) {
                    this.JavaMemory = Integer.parseInt(((String)setting.get("value")));
                } else if (((String)setting.get("func")).equalsIgnoreCase("setJavaPath")) {
                    this.JavaExecutablePath = (String)setting.get("value");
                } else if (((String)setting.get("func")).equalsIgnoreCase("addArgument")) {
                    this.JavaArguments.add((String)setting.get("value"));
                }
            }

            return true;
            
        } catch (Exception ex) {
            Logger.error("LoadSettingsFile", ex.getMessage());
            return false;
        }
    }
    
    
    
    public void appendSettings(Settings newSettings) {
        
        if (newSettings.JavaMemory > this.JavaMemory)
            this.JavaMemory = newSettings.JavaMemory;
        
        if (newSettings.JavaExecutablePath != null)
            this.JavaExecutablePath = newSettings.JavaExecutablePath;
        
        
        
    }
    
    
    
    public String Username = null;
    public String SessionID = null;
    
    public List<String> JavaArguments = new ArrayList();
    public int JavaMemory = 1024;
    public String JavaExecutablePath = null;
}
