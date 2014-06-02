
package com.dsdev.moddle;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;

/**
 *
 * @author LukeSmalley
 */
public class IDHelper {
    
    public static String getID(String mod) {
        if (!IDsLoaded) {
            loadIDs();
        }
        
        return null;
    }
    
    private static String IDMode = "default";
    private static Map<String, Integer> IDRanges = new HashMap<String, Integer>();
    private static boolean IDsLoaded = false;
    
    private static void loadIDs() {
        try {
            JSONObject idConfig = Util.readJSONFile("./ids.json");
            
            IDMode = (String)idConfig.get("mode");
            
            
        } catch (IOException ex) {
            Logger.error("IDHelper.loadIDs", "Failed to load 'ids.json'!", false, ex.getMessage());
        }
    }
}
