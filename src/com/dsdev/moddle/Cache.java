package com.dsdev.moddle;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * This class provides all of the methods for working with a class.
 *
 * @author Greenlock28
 */
public class Cache {

    public static boolean getCacheEntry(String entryName, String entryVersion, String targetDir, LaunchArgs launchArgs, Modpack pack) {
        try {

            if (Util.getFile("./data/" + entryName + "-" + entryVersion + ".zip").exists()) {
                if (!Util.decompressZipfile("./data/" + entryName + "-" + entryVersion + ".zip", "./tmp/cache/" + entryName + "-" + entryVersion)) {
                    return false;
                }
            } else if (Util.getFile("./tmp/pack/cache/" + entryName + "-" + entryVersion + ".zip").exists()) {
                if (!Util.decompressZipfile("./tmp/pack/cache/" + entryName + "-" + entryVersion + ".zip", "./tmp/cache/" + entryName + "-" + entryVersion)) {
                    return false;
                }
            } else if (Util.getFile("./cache/" + entryName + "-" + entryVersion + ".zip").exists()) {
                if (!Util.decompressZipfile("./cache/" + entryName + "-" + entryVersion + ".zip", "./tmp/cache/" + entryName + "-" + entryVersion)) {
                    return false;
                }
            } else {
                Logger.error("Cache.getCacheEntry", "Cache entry was not found!", false, "None");
                return false;
            }

            JSONObject entryConfig = (JSONObject) JSONValue.parse(FileUtils.readFileToString(Util.getFile("./tmp/cache/" + entryName + "-" + entryVersion + "/entry.json")));

            JSONArray filesArray = (JSONArray) entryConfig.get("files");
            for (Object obj : filesArray) {
                JSONObject file = (JSONObject) obj;
                if (((String) file.get("action")).equalsIgnoreCase("extract-zip")) {
                    if (!Util.decompressZipfile("./tmp/cache/" + entryName + "-" + entryVersion + "/" + (String) file.get("name"), targetDir + (String) file.get("target"))) {
                        return false;
                    }
                }
            }
            
            launchArgs.loadSettings((JSONArray)entryConfig.get("settings"));
            
            return true;

        } catch (Exception ex) {
            Logger.error("Cache.getCacheEntry", ex.getMessage(), false, ex.getMessage());
            return false;
        }
    }

}
