
package com.dsdev.moddle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author LukeSmalley
 */
public class IDHelper {
    
    public static String getID(String associatedSetting) {
        
        if (IDConfig == null) {
            try {
                IDConfig = Util.readJSONFile("./data/ids.json");
            } catch (IOException ex) {
                Logger.error("IDHelper.loadIDs", "Failed to load 'ids.json'!", false, ex.getMessage());
                return null;
            }
        }
        
        String idmode = (String)IDConfig.get("mode");
        
        if (idmode.equalsIgnoreCase("default")) {
            return null;
        } else if (idmode.startsWith("file::")) {
            return getIDFromFile(idmode.split("::")[1], associatedSetting);
        } else {
            return getIDFromConfig(idmode, associatedSetting);
        }
    }
    
    public static String getIDFromConfig(String mode, String setting) {
        
        JSONArray modsArray = (JSONArray)IDConfig.get(mode);
        
        if (modsArray != null) {
            for (Object obj : modsArray.toArray()) {
                JSONObject mod = (JSONObject)obj;
                String modname = (String)mod.get("name");
                if (setting.contains(modname)) {
                    int rangeStart = Integer.parseInt((String)mod.get("range"));
                    int bump = 0;
                    if (IDRanges.containsKey(modname)) {
                        bump = IDRanges.get(modname);
                    }
                    IDRanges.put(modname, bump + 1);
                    return Integer.toString(rangeStart + bump);
                }
            }
        }
        
        return null;
    }
    
    public static String getIDFromFile(String filename, String setting) {
        
        JSONObject file = null;
        
        try {
            file = Util.readJSONFile("./" + filename);
        } catch (IOException ex) {
            Logger.error("IDHelper.getIDFromFile", "Failed to load external ID file!", false, ex.getMessage());
            return null;
        }
        
        JSONArray modsArray = (JSONArray)file.get("mode");
        
        if (modsArray != null) {
            for (Object obj : modsArray.toArray()) {
                JSONObject mod = (JSONObject)obj;
                String modname = (String)mod.get("name");
                if (setting.contains(modname)) {
                    int rangeStart = Integer.parseInt((String)mod.get("range"));
                    int bump = 0;
                    if (IDRanges.containsKey(modname)) {
                        bump = IDRanges.get(modname);
                    }
                    IDRanges.put(modname, bump + 1);
                    return Integer.toString(rangeStart + bump);
                }
            }
        }
        
        return null;
    }
    
    private static JSONObject IDConfig = null;
    private static Map<String, Integer> IDRanges = new HashMap<String, Integer>();
}
