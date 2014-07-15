
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
    
    public static String parseString(String s) {
        if (s.contains("\n")) {
            return parseMultilineString(s);
        } else {
            return parseLineString(s);
        }
    }
    
    private static String parseLineString(String s) {

        //List of variable strings to be parsed
        List<String> variableStrings = new ArrayList();

        //Iterate over each character
        for (int i = 0; i < s.length(); i++) {
            
            //Check for a variable string
            if (s.toCharArray()[i] == '$' && s.toCharArray()[i + 1] == '{') {
                
                //Find the end of the variable
                int depth = 0;
                for (int ii = i + 2; ii < s.length(); ii++) {
                    
                    //Check for nested variables
                    if (s.toCharArray()[ii] == '$' && s.toCharArray()[ii + 1] == '{') {
                        depth++;
                    } else if (s.toCharArray()[ii] == '}') {
                        if (depth == 0) {
                            //Add variable string to list
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

        //Create a non-argument variable to return
        String ret = s;
        
        //Iterate through variables
        for (String clause : variableStrings) {
            
            //Get the variable clause text without '${' and '}'
            String fullclause = clause.substring(2, clause.length() - 1);
            String name;
            String defaultval = "";
            boolean isID = false;
            
            //Check for default values or IDs
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
                //Replace variable with its respective or provided default value
                if (!getSetting(name).equals("null")) {
                    ret = ret.replace(clause, getSetting(name));
                } else {
                    ret = ret.replace(clause, defaultval);
                }
            } else {
                //If a value is found, use it
                //Otherwise, get the default ID for the speficied variable
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
        }

        //Recursively parse leftover variables (Is this unnecessary?)
        if (ret.contains("${")) {
            ret = parseString(ret);
        }

        return ret;
    }

    private static String parseMultilineString(String s) {
        
        //Get lines
        String[] lines = s.replace("\r", "").split("\n");
        
        //String to return
        String ret = "";
        
        //Is skipping lines
        boolean isSkipping = false;
        
        //Iterate through lines
        for (String line : lines) {

            //Get trimmed line
            String trimmedline = line.trim();
            
            if (trimmedline.equalsIgnoreCase("$[[end]]")) {
                //Stop skipping lines
                isSkipping = false;
            } else if (trimmedline.startsWith("$[[") && trimmedline.endsWith("]]") && trimmedline.contains(":")) {
                //Get the line without '$[[' and ']]'
                String functionString = trimmedline.substring(3, trimmedline.length() - 2);
                if (functionString.split(":")[0].equalsIgnoreCase("if")) {
                    //'if' statement
                    if (!Variables.getSetting(functionString.split(":")[1]).equals(functionString.split(":")[2])) {
                        isSkipping = true;
                    }
                } else if (functionString.split(":")[0].equalsIgnoreCase("ifnot")) {
                    //'ifnot' statement
                    if (Variables.getSetting(functionString.split(":")[1]).equals(functionString.split(":")[2])) {
                        isSkipping = true;
                    }
                } else if (functionString.split(":")[0].equalsIgnoreCase("ifbool")) {
                    //'ifbool' statement
                    if (!Variables.getSettingBool(functionString.split(":")[1]) == Boolean.parseBoolean(functionString.split(":")[2])) {
                        isSkipping = true;
                    }
                } else if (functionString.split(":")[0].equalsIgnoreCase("ifnotbool")) {
                    //'ifnotbool' statement
                    if (Variables.getSettingBool(functionString.split(":")[1]) == Boolean.parseBoolean(functionString.split(":")[2])) {
                        isSkipping = true;
                    }
                } else if (functionString.split(":")[0].equalsIgnoreCase("ifcontains")) {
                    //'ifcontains' statement
                    boolean contains = false;
                    for (String item : Variables.getSettingList(functionString.split(":")[1])) {
                        if (item.equals(functionString.split(":")[2])) {
                            contains = true;
                            break;
                        } else if (item.contains(functionString.split(":")[2])) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        isSkipping = true;
                    }
                } else if (functionString.split(":")[0].equalsIgnoreCase("ifnotcontains")) {
                    //'ifnotcontains' statement
                    for (String item : Variables.getSettingList(functionString.split(":")[1])) {
                        if (item.equals(functionString.split(":")[2])) {
                            isSkipping = true;
                            break;
                        } else if (item.contains(functionString.split(":")[2])) {
                            isSkipping = true;
                            break;
                        }
                    }
                }
            } else {
                //Append the line if not skipping
                if (!isSkipping) {
                    ret += Variables.parseString(line) + "\n\r";
                }
            }

        }
        
        //Remove trailing '\n\r'
        ret = ret.substring(0, ret.length() - 2);
        
        return ret;
    }
    
    public static String getSetting(String key) {
        String value = SettingsMap.get(key);
        if (value != null) {
            return parseString(value);
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
