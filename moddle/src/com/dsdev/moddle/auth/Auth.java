
package com.dsdev.moddle.auth;

import com.dsdev.moddle.GlobalDialogs;
import com.dsdev.moddle.util.Logger;
import com.dsdev.moddle.util.Util;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;

/**
 *
 * @author Diamond Sword Development
 */
public class Auth {
    
    public static String AccountName = null;
    public static String Password = null;
    
    public static String Username = null;
    public static String UUID = null;
    public static String AccessToken = null;
    
    public static boolean isLoggedIn = false;
    
    
    public static void loadFromFile() {
        try {
            if (new File("./data/auth.json").isFile()) {
                JSONObject authConfig = Util.readJSONFile("./data/auth.json");
                isLoggedIn = Boolean.parseBoolean(authConfig.get("loggedin").toString());
                AccountName = authConfig.get("account").toString();
                Password = decryptPassword(authConfig.get("password").toString());
            } else {
                JSONObject authConfig = new JSONObject();
                authConfig.put("account", "");
                authConfig.put("password", "");
                authConfig.put("loggedin", Boolean.toString(false));
                FileUtils.writeStringToFile(new File("./data/auth.json"), authConfig.toJSONString());
            }
        } catch (IOException ex) {
            Logger.error("Auth.loadFromFile", "Failed to load file 'auth.json'!", false, ex.getMessage());
        }
    }
    
    public static void saveToFile() {
        try {
            JSONObject authConfig = new JSONObject();
            authConfig.put("account", AccountName);
            authConfig.put("password", encryptPassword(Password));
            authConfig.put("loggedin", Boolean.toString(isLoggedIn));
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
    
    
    public static boolean performLogin(String uname, String pword) {
        
        //Set username and password
        if (uname != null) AccountName = uname;
        if (pword != null) Password = pword;
        
        //Initialize request and result objects
        YggdrasilRequest request = new YggdrasilRequest(AccountName, Password);
        YggdrasilResult result = null;
        
        //Submit the login request
        try {
            result = request.send();
        } catch (Exception ex) {
            isLoggedIn = false;
            GlobalDialogs.showNotification("Login failed: (" + ex.getClass().getSimpleName() + ") " + ex.getMessage());
            return false;
        }
        
        if (result.wasSuccessful) {
            isLoggedIn = true;
            Username = result.Username;
            UUID = result.UUID;
            AccessToken = result.AccessToken;
            return true;
        } else {
            isLoggedIn = false;
            Username = null;
            UUID = null;
            AccessToken = null;
            GlobalDialogs.showNotification("Login failed: " + result.ErrorDescription);
            return false;
        }
    }
    
    public static void performLogout() {
        Username = null;
        UUID = null;
        AccessToken = null;
        isLoggedIn = false;
    }
}
