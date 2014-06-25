
package com.dsdev.moddle.auth;

import com.dsdev.moddle.Dialogs;
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
public class Auth {
    
    public static String AccountName = null;
    public static String Password = null;
    
    public static String Username = null;
    public static String UUID = null;
    public static String AccessToken = null;
    
    public static boolean wasLastLoginSuccessful = false;
    
    
    public static void loadFromFile() {
        try {
            JSONObject authConfig = Util.readJSONFile("./data/auth.json");
            AccountName = authConfig.get("account").toString();
            Password = decryptPassword(authConfig.get("password").toString());
            wasLastLoginSuccessful = Boolean.parseBoolean(authConfig.get("successful").toString());
        } catch (IOException ex) {
            Logger.error("Auth.loadFromFile", "Failed to load file 'auth.json'!", false, ex.getMessage());
        }
    }
    
    public static void saveToFile() {
        try {
            JSONObject authConfig = new JSONObject();
            authConfig.put("account", AccountName);
            authConfig.put("password", Password);
            authConfig.put("successful", Boolean.toString(wasLastLoginSuccessful));
            FileUtils.writeStringToFile(new File("./data/auth.json"), authConfig.toJSONString());
        } catch (IOException ex) {
            Logger.error("AuthCache.saveToFile", "Failed to save auth data to file 'auth.json'!", false, ex.getMessage());
        }
    }
    
    private static String encryptPassword(String p) {
        String e="";for(char c:p.toCharArray())e+=Character.toString("1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik9".charAt((byte)((byte)c/32)))+Character.toString("1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik9".charAt((byte)((byte)c%32)));return e;
    }
    
    private static String decryptPassword(String p) {
        String d="";for(int i=0;i<p.length();i++){d+=Character.toString((char)((((byte)"1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik9".indexOf(Character.toString(p.charAt(i))))*32)+((byte)"1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik9".indexOf(Character.toString(p.charAt(i+1))))));i++;}return d;
    }
    
    
    private boolean performLogin(String uname, String pword) {
        if (uname != null) {
            AccountName = uname;
        }
        
        if (pword != null) {
            Password = pword;
        }
        
        YggdrasilRequest request = new YggdrasilRequest(AccountName, Password);
        YggdrasilResult result = null;
        
        try {
            result = request.send();
        } catch (Exception ex) {
            wasLastLoginSuccessful = false;
            return false;
        }
        
        if (result.wasSuccessful) {
            wasLastLoginSuccessful = true;
            Username = result.Username;
            UUID = result.UUID;
            AccessToken = result.AccessToken;
            return true;
        } else {
            wasLastLoginSuccessful = false;
            Username = null;
            UUID = null;
            AccessToken = null;
            Dialogs.showNotification("Error: " + result.ErrorDescription);
            return false;
        }
    }
}
