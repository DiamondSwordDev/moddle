
package com.dsdev.moddle;

import java.io.IOException;
import org.json.simple.JSONObject;

/**
 *
 * @author LukeSmalley
 */
public class LastLogin {
    
    private static String username = null;
    private static String password = null;
    private static String accessToken = null;
    
    
    public static String getUsername() { return username; }
    
    public static String getPassword() { return password; }
    
    public static String getAccessToken() { return accessToken; }
    
    
    private static void loadLoginInfoFromSavefile() {
        try {
            JSONObject lastLoginSaveFile = Util.readJSONFile("./data/lastlogin.json");
            username = lastLoginSaveFile.get("username").toString();
            password = decryptPassword(lastLoginSaveFile.get("password").toString());
            accessToken = lastLoginSaveFile.get("accesstoken").toString();
        } catch (IOException ex) {
            
        }
    }
    
    private static String encryptPassword(String p) {
        String e="";for(char c:p.toCharArray())e+=Character.toString("1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik9".charAt((byte)((byte)c/32)))+Character.toString("1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik9".charAt((byte)((byte)c%32)));return e;
    }
    
    private static String decryptPassword(String p) {
        String d="";for(int i=0;i<p.length();i++){d+=Character.toString((char)((((byte)"1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik9".indexOf(Character.toString(p.charAt(i))))*32)+((byte)"1qaz2wsx3edc4rfv5tgb6yhn7ujm8ik9".indexOf(Character.toString(p.charAt(i+1))))));i++;}return d;
    }
    
}
