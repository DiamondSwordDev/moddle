package com.dsdev.moddle;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * This class contains fields required for modifying launch arguments
 *
 * @author Diamond Sword Development
 */
public class LaunchArgs {

    //public JSONArray jsonStruct = new JSONArray();
    //General Arguments
    public String MinecraftVersion = null;
    public String AppDataDirectory = null;
    public String None = null;

    //Java Core Arguments
    public String JavaExecutablePath = "javaw.exe";

    public boolean UseXmxArgument = true;
    public int XmxArgument = 1024;

    public boolean UseXmsArgument = false;
    public int XmsArgument = 256;

    public boolean UseDJavaLibPathArgument = false;
    public String DJavaLibPathArgument = null;

    public boolean UseClassPathArgument = false;
    public boolean GetLibraryClassPaths = false;
    public boolean GetMinecraftClassPath = false;
    public List<String> AdditionalClassPathEntries = new ArrayList();

    public boolean UseMainClassArgument = true;
    public String MainClassArgument = "net.minecraft.client.main.Main";

    public List<String> AdditionalCoreArguments = new ArrayList();

    //Minecraft Arguments
    public boolean UseGameDirArgument = false;
    public String GameDirArgument = null;

    public boolean UseAssetDirArgument = false;
    public String AssetDirArgument = null;

    public boolean UseAssetIndexArgument = false;
    public String AssetIndexArgument = null;

    public boolean UseVersionArgument = false;
    public String VersionArgument = null;

    public boolean UseTweakClassArgument = false;
    public String TweakClassArgument = null;

    public boolean UseUsernameArgument = false;
    public boolean UseSessionArgument = false;
    public boolean UseUUIDArgument = false;
    public boolean UseAccessTokenArgument = false;
    public boolean UseUserPropertiesArgument = false;
    public boolean UseUserTypeArgument = false;

    public boolean UseLegacyUsernameAndSession = false;

    public List<String> AdditionalMinecraftArguments = new ArrayList();

    public void setVariable(String name, String value) {
        try {

            for (Field f : this.getClass().getDeclaredFields()) {
                if (name.equalsIgnoreCase(f.getName().toLowerCase())) {
                    if (f.getType().getSimpleName().equals("String")) {
                        f.set(this, value);
                    } else if (f.getType().getSimpleName().equals("int")) {
                        f.set(this, Integer.parseInt(value));
                    } else if (f.getType().getSimpleName().equals("boolean")) {
                        f.set(this, value.equalsIgnoreCase("true"));
                    }
                }
            }

        } catch (Exception ex) {
            Logger.error("LaunchArgs.setVariable", ex.getMessage(), false, ex.getMessage());
        }
    }

    public Object getVariable(String name) {
        try {

            for (Field f : this.getClass().getDeclaredFields()) {
                if (name.equalsIgnoreCase(f.getName().toLowerCase())) {
                    return f.get(this);
                }
            }

            return null;

        } catch (Exception ex) {
            Logger.error("MainForm.getVariable", ex.getMessage(), false, ex.getMessage());
            return null;
        }
    }

    public String parseString(String s) {
        try {

            String ret = s;

            for (Field f : this.getClass().getDeclaredFields()) {
                if (f.get(this) != null) {
                    ret = ret.replace("{" + f.getName() + "}", f.get(this).toString());
                }
            }

            return ret;

        } catch (Exception ex) {
            Logger.error("LaunchArgs.parseString", ex.getMessage(), false, ex.getMessage());
            return null;
        }
    }

    public void loadSettings(JSONArray settingsArray) {
        for (Object obj : settingsArray) {
            JSONObject setting = (JSONObject) obj;
            setVariable((String) setting.get("name"), (String) setting.get("value"));

            /*for (Object o : jsonStruct) {
             JSONObject oo = (JSONObject) o;
             if (((String)oo.get("name")).equalsIgnoreCase(((String)setting.get("name")).toLowerCase())) {
             oo.values().clear();
             oo.values().add((String)setting.get("value"));
             }
             }*/
        }
    }
}
