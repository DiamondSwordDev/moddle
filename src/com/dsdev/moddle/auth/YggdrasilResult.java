
package com.dsdev.moddle.auth;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author LukeSmalley
 */
public class YggdrasilResult {
    
    public YggdrasilResult(String serverResponse) {
        JSONObject resultObj = (JSONObject)JSONValue.parse(serverResponse);
        if (resultObj.get("error") == null) {
            wasSuccessful = true;
            AccessToken = resultObj.get("accessToken").toString();
            JSONObject profile = (JSONObject)resultObj.get("selectedProfile");
            UUID = profile.get("id").toString();
            Username = profile.get("name").toString();
        } else {
            wasSuccessful = false;
            Error = resultObj.get("error").toString();
            ErrorDescription = resultObj.get("errorMessage").toString();
        }
    }
    
    public boolean wasSuccessful = false;
    
    public String Error = null;
    public String ErrorDescription = null;
    
    public String AccessToken = null;
    public String UUID = null;
    public String Username = null;
    
}
