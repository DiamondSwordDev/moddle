
package com.dsdev.moddle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Diamond Sword Development
 */
public class Variables {
    
    private static Map<String, String> SettingsMap = new HashMap<String, String>();
    
    public static void setSetting(String name, String value) {
        if (name.startsWith("+")) {
            int index = 0;
            while (getSetting(name.substring(1) + "." + Integer.toString(index)) != null) index++;
            SettingsMap.put(name.substring(1) + "." + Integer.toString(index), value);
        } else {
            SettingsMap.put(name, value);
        }
    }
    
    public static String parseSettingsString(String s) {

        List<String> variableStrings = new ArrayList();

        for (int i = 0; i < s.length(); i++) {
            
            if (s.toCharArray()[i] == '$' && s.toCharArray()[i + 1] == '{') {
                int depth = 0;
                for (int ii = i + 2; ii < s.length(); ii++) {
                    
                    if (s.toCharArray()[ii] == '$' && s.toCharArray()[ii + 1] == '{') {
                        depth++;
                    } else if (s.toCharArray()[ii] == '}') {
                        if (depth == 0) {
                            String functionString = s.substring(i, ii + 1);
                            variableStrings.add(functionString);
                            i = ii;
                            break;
                        } else {
                            depth--;
                        }
                    }
                    
                }
            }
            
        }

        String ret = s;
        String settingName = "null";

        for (String clause : variableStrings) {
            
            String fullclause = clause.substring(2, clause.length() - 1);
            String name;
            String defaultval = "";
            boolean isID = false;
            
            if (fullclause.contains(":")) {
                name = fullclause.split(":")[0];
                defaultval = fullclause.split(":")[1];
            } else if (fullclause.contains("##") && fullclause.split("##")[0].equalsIgnoreCase("id")) {
                isID = true;
                name = fullclause.split("##")[1];
                defaultval = fullclause.split("##")[2];
            } else {
                name = fullclause;
            }
            
            if (!isID) {
                if (!getSetting(name).equals("null")) {
                    ret = ret.replace(clause, getSetting(name));
                } else {
                    ret = ret.replace(clause, defaultval);
                }
            } else {
                if (!getSetting(name).equals("null")) {
                    ret = ret.replace(clause, getSetting(name));
                } else {
                    String id = IDHelper.getID(name);
                    if (id != null) {
                        ret = ret.replace(clause, id);
                    } else {
                        ret = ret.replace(clause, defaultval);
                    }
                }
            }
            
            settingName = name;
        }

        if (ret.contains("${")) {
            ret = parseSettingsString(ret);
        }

        /*if (ret.contains("##")) {
            if (ret.split("##")[0].equalsIgnoreCase("id")) {
                String id = IDHelper.getID(settingName);
                if (id != null) {
                    ret = id;
                } else {
                    ret = ret.split("##")[1];
                }
            }
        }*/

        return ret;
    }

    public static String getSetting(String key) {
        String value = SettingsMap.get(key);
        if (value != null) {
            return parseSettingsString(value);
        } else {
            return "null";
        }
    }
    
    public static boolean getSettingBool(String key) {
        return "true".equalsIgnoreCase(getSetting(key));
    }
    
    public static List<String> getSettingList(String key) {
        List<String> settingList = new ArrayList();
        int index = 0;
        while (true) {
            String listEntry = getSetting(key + "." + Integer.toString(index));
            if (!listEntry.equals("null")) {
                settingList.add(listEntry);
                index++;
            } else {
                break;
            }
        }
        return settingList;
    }
    
    public static void clearSettings() {
        SettingsMap = new HashMap<String, String>();
    }
    
    public static void loadSettingsFromJSON(JSONObject json) {
        for (Object obj : (JSONArray)json.get("settings")) {
            JSONObject setting = (JSONObject)obj;
            SettingsMap.put(setting.get("name").toString(), setting.get("value").toString());
        }
    }
    
}
