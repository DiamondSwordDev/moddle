package com.dsdev.moddle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Old class for settings stuff.
 *
 * @author Greenlock28
 */
public class Settings {

    public Settings() {
        //Nothing here yet.
    }

    public boolean loadSettingsFromFile(String path) {
        try {

            JSONObject jsonFile = (JSONObject) JSONValue.parse(FileUtils.readFileToString(new File(path)));
            JSONArray settingsArray = (JSONArray) jsonFile.get("settings");

            for (Object obj : settingsArray) {
                JSONObject setting = (JSONObject) obj;
                if (((String) setting.get("func")).equalsIgnoreCase("setMemory")) {
                    this.MinimumMemory = Integer.parseInt(((String) setting.get("value")));
                } else if (((String) setting.get("func")).equalsIgnoreCase("setJavaPath")) {
                    this.JavaExecutablePath = (String) setting.get("value");
                }
            }

            return true;

        } catch (Exception ex) {
            Logger.error("LoadSettingsFile", ex.getMessage());
            return false;
        }
    }

    public void appendSettings(Settings newSettings) {

        if (newSettings.MinimumMemory > this.MinimumMemory) {
            this.MinimumMemory = newSettings.MinimumMemory;
        }

        if (newSettings.JavaExecutablePath != null) {
            this.JavaExecutablePath = newSettings.JavaExecutablePath;
        }

    }

    //Launch Args
    public int MinimumMemory = 1024;
    public String TweakClass = null;
    public String JavaExecutablePath = null;
}
