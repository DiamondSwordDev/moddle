
package com.dsdev.moddle;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Greenlock28
 */
public class LoginHelper {
    
    public static String doPasswordLogin(String username, String password) {
        
        AccountName = username;
        Password = password;

        Logger.info("LoginHelper.doPasswordLogin", "Building payload...");
        JSONObject agent = new JSONObject();
        agent.put("name", "Minecraft");
        agent.put("version", "1");
        JSONObject payload = new JSONObject();
        payload.put("agent", agent);
        payload.put("username", username);
        payload.put("password", password);

        Logger.info("LoginHelper.doPasswordLogin", "Performing POST request...");
        String resultString = null;
        try {
            resultString = Util.doJSONPost("https://authserver.mojang.com/authenticate", payload);
        } catch (Exception ex) {
            Logger.error("LoginHelper.doPasswordLogin", "POST request failed!", false, ex.getMessage());
            return "Request failed!";
        }

        JSONObject result = (JSONObject)JSONValue.parse(resultString);

        if (result.get("error") == null) {
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

            Logger.info("LoginHelper.doPasswordLogin", "AccessToken " + AccessToken + " generated.");
        } else {
            Logger.warning("LoginHelper.doPasswordLogin", "Login failed with error '" + (String)result.get("error") + "'");
            return "Bad Login!";
        }
        
        return "Success";
    }
    
    public static void doSystemLogout() {
        Username = null;
        AccountName = null;
        Password = null;

        UUID = null;
        UserProperties = null;
        UserType = null;

        AccessToken = null;
        ClientToken = null;
    }
    
    public static String Username = null;
    public static String AccountName = null;
    public static String Password = null;

    public static String UUID = null;
    public static String UserProperties = null;
    public static String UserType = null;

    public static String AccessToken = null;
    public static String ClientToken = null;
    
}
