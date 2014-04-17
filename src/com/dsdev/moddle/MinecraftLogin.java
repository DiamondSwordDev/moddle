package com.dsdev.moddle;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Container for all auth-related stuff.
 *
 * @author Greenlock28
 */
public class MinecraftLogin {

    public void doLogin(String username, String password) {
        try {
        
            AccountName = username;
            Password = password;
            
            Logger.info("Login", "Building payload...");
            JSONObject agent = new JSONObject();
            agent.put("name", "Minecraft");
            agent.put("version", "1");
            JSONObject payload = new JSONObject();
            payload.put("agent", agent);
            payload.put("username", username);
            payload.put("password", password);
            
            Logger.info("Login", "Performing POST request...");
            String resultString = Util.doJSONPost("https://authserver.mojang.com/authenticate", payload);
            
            JSONObject result = (JSONObject)JSONValue.parse(resultString);
            
            if (result.get("accessToken") != null) {
                AccessToken = (String)result.get("accessToken");
            }
            
            if (result.get("clientToken") != null) {
                ClientToken = (String)result.get("clientToken");
            }
            
            if (result.get("selectedProfile") != null) {
                JSONObject selectedProfile = (JSONObject)result.get("selectedProfile");
                
                if (selectedProfile.get("id") != null) {
                    UUID = (String)selectedProfile.get("id");
                }
                
                if (selectedProfile.get("name") != null) {
                    Username = (String)selectedProfile.get("name");
                }
            }
            
            Logger.info("Login", "AccessToken " + AccessToken + " generated.");
        
        } catch (Exception ex) {
            Logger.error("MinecraftLogin.doLogin", ex.getMessage(), false, ex.getMessage());
        }
    }
    
    public String Username = null;
    public String AccountName = null;
    public String Password = null;

    public String UUID = null;
    public String UserProperties = null;
    public String UserType = null;

    public String AccessToken = null;
    public String ClientToken = null;

}
