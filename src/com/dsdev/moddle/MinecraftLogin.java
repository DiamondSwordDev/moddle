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
        
            Logger.info("Login", "Building payload...");
            JSONObject agent = new JSONObject();
            agent.put("name", "Minecraft");
            agent.put("version", "1");
            JSONObject payload = new JSONObject();
            payload.put("agent", agent);
            payload.put("username", username);
            payload.put("password", password);
            
            Logger.info("Login", "Performing POST request...");
            String resultString = Util.doJSONPost("url", payload);
            
            JSONObject result = (JSONObject)JSONValue.parse(resultString);
        
        } catch (Exception ex) {
            Logger.error("Login", ex.getMessage());
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
