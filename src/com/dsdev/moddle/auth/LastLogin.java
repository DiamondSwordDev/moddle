
package com.dsdev.moddle.auth;

import com.dsdev.moddle.Logger;
import com.dsdev.moddle.Util;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

/**
 *
 * @author LukeSmalley
 */
public class LastLogin {
    
    private static String username = null;
    private static String password = null;
    private static boolean wasValid = false;
    
    
    public static String getUsername() {
        if (username == null) {
            loadLoginInfoFromFile();
        }
        return username;
    }
    
    public static String getPassword() {
        if (password == null) {
            loadLoginInfoFromFile();
        }
        return password;
    }
    
    public static boolean getValidityOfLastLogin() {
        if (username == null) {
            loadLoginInfoFromFile();
        }
        return wasValid;
    }
    
    
    private static void loadLoginInfoFromFile() {
        try {
            JSONObject lastLoginSaveFile = Util.readJSONFile("./data/lastlogin.json");
            username = lastLoginSaveFile.get("username").toString();
            password = decryptPassword(lastLoginSaveFile.get("password").toString());
            wasValid = Boolean.parseBoolean(lastLoginSaveFile.get("valid").toString());
        } catch (IOException ex) {
            Logger.error("LastLogin.loadLoginInfoFromFile", "Failed to load lastlogin.json", false, ex.getMessage());
        }
    }
    
    private static void saveLoginInfoToFile() {
        try {
            JSONObject lastLoginSaveFile = new JSONObject();
            lastLoginSaveFile.put("username", username);
            lastLoginSaveFile.put("password", password);
            lastLoginSaveFile.put("valid", Boolean.toString(wasValid));
            FileUtils.writeStringToFile(new File("./data/lastlogin.json"), lastLoginSaveFile.toJSONString());
        } catch (IOException ex) {
            Logger.error("LastLogin.loadLoginInfoFromFile", "Failed to load lastlogin.json", false, ex.getMessage());
        }
    }
    
    private static String encryptPassword(String p) {
        String e="";for(char c:p.toCharArray())e+=Character.toString("1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik9".charAt((byte)((byte)c/32)))+Character.toString("1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik9".charAt((byte)((byte)c%32)));return e;
    }
    
    private static String decryptPassword(String p) {
        String d="";for(int i=0;i<p.length();i++){d+=Character.toString((char)((((byte)"1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik9".indexOf(Character.toString(p.charAt(i))))*32)+((byte)"1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik9".indexOf(Character.toString(p.charAt(i+1))))));i++;}return d;
    }
    
}
