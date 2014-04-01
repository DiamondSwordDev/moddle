
package com.dsdev.moddle;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author Greenlock28
 */
public class Cache {
    
    public static boolean getCacheEntry(String entryPath, Settings entrySettings) {
        try {
            
            if (!Util.decompressZipfile(entryPath, "./tmp/cache/" + Util.getFile(entryPath).getName().replace(".zip", "")))
                return false;
            
            JSONObject entryConfig = (JSONObject)JSONValue.parse(FileUtils.readFileToString(Util.getFile("./tmp/cache/" + Util.getFile(entryPath).getName().replace(".zip", "") + "/entry.json")));
            
            entrySettings.loadSettingsFromFile(entryPath);
            
            return true;
            
        } catch (Exception ex) {
            Logger.error("GetCacheEntry", ex.getMessage());
            return false;
        }
    }
    
}
