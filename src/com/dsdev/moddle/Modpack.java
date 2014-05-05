package com.dsdev.moddle;

import com.dsdev.assets.AssetBuilder;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * The mod pack compiler.
 *
 * @author Greenlock28
 */
public class Modpack {

    private String ModpackName = "";
    private String PlayerOwner = "";
    
    public boolean IsInstallComplete = false;
    
    private LaunchArgs LaunchArguments = null;
    
    public Map<String, String> Settings = new HashMap<String, String>();
    public List<String> AdditionalClassPathEntries = new ArrayList();
    public List<String> AdditionalCoreLaunchArguments = new ArrayList();
    public List<String> AdditionalMinecraftLaunchArguments = new ArrayList();
    
    public List<String> InstalledEntries = new ArrayList();
    public List<String> ExcludedEntries = new ArrayList();

    
    
    public String parseSettingsString(String s) {
        String ret = s;
        for (Entry<String, String> entry : Settings.entrySet()) {
            ret = ret.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        if (ret.contains("${")) {
            ret = parseSettingsString(ret);
        }
        return ret;
    }
    
    public String getSetting(String key) {
        String value = Settings.get(key);
        if (value != null) {
            return parseSettingsString(value);
        } else {
            return "null";
        }
    }
    
    public boolean getSettingBool(String key) {
        return "true".equalsIgnoreCase(Settings.get(key));
    }
    
    public void setSetting(String key, String value) {
        if (key.equalsIgnoreCase("addclasspathentry")) {
            AdditionalClassPathEntries.add(value);
        } else if (key.equalsIgnoreCase("addcorelaunchargument")) {
            AdditionalCoreLaunchArguments.add(value);
        } else {
            Settings.put(key, value);
        }
    }
    
    
    
    public Modpack() { }
    
    public Modpack(String name, String player, boolean forceUpdate) {
        
        this.ModpackName = name;
        this.PlayerOwner = player;
        
        Logger.info("Modpack (const)", "Asserting pack directory...");
        Util.assertDirectoryExistence("./users/" + PlayerOwner + "/" + ModpackName);
        
        Logger.info("Modpack (const)", "Asserting .minecraft directory...");
        Util.assertDirectoryExistence("./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft");
        
        Logger.info("Modpack (const)", "Asserting entries directory...");
        Util.assertDirectoryExistence("./users/" + PlayerOwner + "/" + ModpackName + "/entries");
        
        if (!(new File("./users/" + PlayerOwner + "/" + ModpackName + "/completeinstall").exists())) {
            IsInstallComplete = false;
            if (!(new File("./users/" + PlayerOwner + "/" + ModpackName + "/partialinstall").exists())) {
                try {
                    FileUtils.writeStringToFile(new File("./users/" + PlayerOwner + "/" + ModpackName + "/partialinstall"), "");
                } catch (IOException ex) {
                    Logger.error("Modpack (const)", "Failed to create partialinstall file!", false, ex.getMessage());
                }
            }
        } else {
            IsInstallComplete = true;
        }
        
        if (!IsInstallComplete || forceUpdate) {
            JSONObject instanceConfig = null;
            try {
                instanceConfig = Util.readJSONFile("./users/" + PlayerOwner + "/" + ModpackName + "/instance.json");
            } catch (IOException ex) {
                Logger.error("Modpack (const)", "Instance config not found!", true, ex.getMessage());
                return;
            }
            if (instanceConfig != null) {
                try {
                    if (new File("./users/" + PlayerOwner + "/" + ModpackName + "/ark").isDirectory()) {
                        FileUtils.deleteDirectory(new File("./users/" + PlayerOwner + "/" + ModpackName + "/ark"));
                    }
                    FileUtils.copyDirectory(new File("./packs/" + (String)instanceConfig.get("pack")), new File("./users/" + PlayerOwner + "/" + ModpackName + "/ark"));
                } catch (IOException ex) {
                    Logger.error("Modpack (const)", "Failed to copy modpack template!", true, ex.getMessage());
                    return;
                }
            }
        }
    }
    
    public static void createInstance(String name, String player, String base) {
        try {
            if (!new File("./users/" + player + "/" + name).exists()) {
                new File("./users/" + player + "/" + name).mkdirs();
            }
            JSONObject instanceConfig = new JSONObject();
            instanceConfig.put("name", name);
            instanceConfig.put("pack", base);
            FileUtils.writeStringToFile(new File("./users/" + player + "/" + name + "/instance.json"), instanceConfig.toJSONString());
            //FileUtils.copyDirectory(new File("./packs/" + BaseModpackComboBox.getSelectedItem().toString()), new File("./users/" + UsernameField.getText().replace("@", "_") + "/" + InstanceNameField.getText() + "/ark"));
        } catch (IOException ex) {
            Logger.error("Modpack.createInstance", "Failed to create instance!", false, ex.getMessage());
        }
    }
    
    
    
    public void build() {
        
        //LaunchArguments = new LaunchArgs();
        
        //LaunchArguments.AppDataDirectory = Util.getFullPath("./users/" + PlayerOwner + "/" + ModpackName);
        setSetting("launch.AppDataDirectory", Util.getFullPath("./users/" + PlayerOwner + "/" + ModpackName));

        //<editor-fold defaultstate="collapsed" desc="Installion status file manipulation">
        
        if (new File("./users/" + PlayerOwner + "/" + ModpackName + "/completeinstall").exists()) {
            if (!(new File("./users/" + PlayerOwner + "/" + ModpackName + "/completeinstall").delete())) {
                Logger.error("Modpack.build", "Failed to delete completeinstall file!", false, "None");
            }
        }
        
        if (!(new File("./users/" + PlayerOwner + "/" + ModpackName + "/partialinstall").exists())) {
            try {
                FileUtils.writeStringToFile(new File("./users/" + PlayerOwner + "/" + ModpackName + "/partialinstall"), "");
            } catch (IOException ex) {
                Logger.error("Modpack.build", "Failed to create partialinstall file!", false, ex.getMessage());
            }
        }

        //</editor-fold>
        
        JSONObject packConfig = null;
        try {
            packConfig = Util.readJSONFile("./users/" + PlayerOwner + "/" + ModpackName + "/ark/pack.json");
        } catch (IOException ex) {
            Logger.error("Modpack.build", "Failed to read pack.json!", true, ex.getMessage());
            return;
        }
        
        setSetting("general.MinecraftVersion", (String) packConfig.get("minecraftversion"));
        
        Logger.info("Modpack.build", "Building skeleton installation...");
        evaluateCacheEntry("minecraft", Settings.get("general.MinecraftVersion"), null, null);
        parseCacheEntry("minecraft", Settings.get("general.MinecraftVersion"));
        getCacheEntry("minecraft", Settings.get("general.MinecraftVersion"), "./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft", true);

        //<editor-fold defaultstate="collapsed" desc="Install Minecraft Jarfile">

        Logger.info("Modpack.build", "Creating '.minecraft/versions/' ...");
        Util.assertDirectoryExistence("./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft/versions");

        Logger.info("Modpack.build", "Creating '.minecraft/versions/<version>/' ...");
        Util.assertDirectoryExistence("./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft/versions/" + Settings.get("general.MinecraftVersion"));

        Logger.info("Modpack.build", "Obtaining Minecraft jarfile...");
        Util.assertDirectoryExistence("./data/versions");
        if (!(new File("./data/versions/" + Settings.get("general.MinecraftVersion") + ".jar").exists())) {
            Logger.info("Modpack.build", "Version does not exist.  Downloading...");
            try {
                FileUtils.copyURLToFile(new URL("http://s3.amazonaws.com/Minecraft.Download/versions/" + Settings.get("general.MinecraftVersion") + "/" + Settings.get("general.MinecraftVersion") + ".jar"), new File("./data/versions/" + Settings.get("general.MinecraftVersion") + ".jar"));
            } catch (IOException ex) {
                Logger.error("Modpack.build", "Failed to download Minecraft jarfile!", true, ex.getMessage());
                return;
            }
        }
        try {
        FileUtils.copyFile(new File("./data/versions/" + Settings.get("general.MinecraftVersion") + ".jar"), new File("./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft/versions/" + Settings.get("general.MinecraftVersion") + "/" + Settings.get("general.MinecraftVersion") + ".jar"));
        } catch (IOException ex) {
            Logger.error("Modpack.build", "Failed to copy Minecraft jarfile!", true, ex.getMessage());
            return;
        }
        
        //</editor-fold>
        
        Logger.info("Modpack.build", "Installing assets...");
        if (!new File("./data/assets").isDirectory()) {
            try {
                AssetBuilder.buildAssets("./data/assets/");
            } catch (IOException ex) {
                Logger.error("Modpack.build", "Failed to download assets!", false, ex.getMessage());
            }
        }
        if (new File("./data/assets").isDirectory()) {
            try {
                FileUtils.copyDirectory(new File("./data/assets/legacy"), new File("./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft/assets"));
            } catch (IOException ex) {
                Logger.error("Modpack.build", "Failed to install assets!", false, ex.getMessage());
            }
        }
        
        List<String> installQueue = new ArrayList();
        List<String> excludeQueue = new ArrayList();
        
        JSONArray entriesArray = (JSONArray)packConfig.get("entries");
        for (Object obj : entriesArray) {
            JSONObject entryObj = (JSONObject)obj;
            Logger.info("Modpack.build", "Evaluating entry '" + (String) entryObj.get("name") + "'...");
            evaluateCacheEntry((String)entryObj.get("name"), (String)entryObj.get("version"), installQueue, excludeQueue);
        }
        
        Logger.info("Modpack.build", "Evaluating entry 'moddleassets'...");
        evaluateCacheEntry("moddleassets", "0.2", installQueue, excludeQueue);
        
        List<String> finalQueue = new ArrayList();
        
        Logger.info("Modpack.build", "Building installation queue...");
        for (String entry : installQueue) {
            boolean install = true;
            for (String exclusion : excludeQueue) {
                if (entry.startsWith(exclusion)) {
                    install = false;
                    break;
                }
            }
            if (install) {
                finalQueue.add(entry);
            }
        }
        
        for (String entry : finalQueue) {
            String[] entryData = entry.split(",");
            Logger.info("Installing entry " + entryData[0] + "...");
            parseCacheEntry(entryData[0], entryData[1]);
            getCacheEntry(entryData[0], entryData[1], "./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft", false);
        }
        
        //<editor-fold defaultstate="collapsed" desc="Installion status file manipulation (complete)">
        
        if (new File("./users/" + PlayerOwner + "/" + ModpackName + "/partialinstall").exists()) {
            if (!(new File("./users/" + PlayerOwner + "/" + ModpackName + "/partialinstall").delete())) {
                Logger.error("Modpack.build", "Failed to delete partialinstall file!", false, "None");
            }
        }
        
        if (!(new File("./users/" + PlayerOwner + "/" + ModpackName + "/completeinstall").exists())) {
            try {
                FileUtils.writeStringToFile(new File("./users/" + PlayerOwner + "/" + ModpackName + "/completeinstall"), "");
            } catch (IOException ex) {
                Logger.error("Modpack.build", "Failed to create completeinstall file!", false, ex.getMessage());
            }
        }

        //</editor-fold>
    }
    
    public void pseudoBuild() {
        
        LaunchArguments = new LaunchArgs();
        
        LaunchArguments.AppDataDirectory = Util.getFullPath("./users/" + PlayerOwner + "/" + ModpackName);

        JSONObject packConfig = null;
        try {
            packConfig = Util.readJSONFile("./users/" + PlayerOwner + "/" + ModpackName + "/ark/pack.json");
        } catch (IOException ex) {
            Logger.error("Modpack.pseudoBuild", "Failed to read pack.json!", true, ex.getMessage());
            return;
        }
        
        Logger.info("Modpack.pseudoBuild", "Building skeleton pseudoinstallation...");
        getPseudoEntry("minecraft", (String) packConfig.get("minecraftversion"), true);

        getPseudoEntry("moddlejarmod", "0.2", false);
        
        JSONArray entriesArray = (JSONArray)packConfig.get("entries");
        for (Object obj : entriesArray) {
            JSONObject entryObj = (JSONObject)obj;
            Logger.info("Modpack.pseudoBuild", "Installing pseudoentry " + (String)entryObj.get("name") + "...");
            getPseudoEntry((String)entryObj.get("name"), (String)entryObj.get("version"), false);
        }
    }

    public void evaluateCacheEntry(String entryName, String entryVersion, List<String> installQueue, List<String> excludeQueue) {
        String entryLocation = "";
        if (new File("./data/" + entryName + "-" + entryVersion + ".zip").exists()) {
            entryLocation = "./data/";
        } else if (new File("./users/" + PlayerOwner + "/" + ModpackName + "/ark/cache/" + entryName + "-" + entryVersion + ".zip").exists()) {
            entryLocation = "./users/" + PlayerOwner + "/" + ModpackName + "/ark/cache/";
        } else if (new File("./cache/" + entryName + "-" + entryVersion + ".zip").exists()) {
            entryLocation = "./cache/";
        } else {
            Logger.error("Modpack.evaluateCacheEntry", "Cache entry was not found!", false, "None");
            return;
        }
        
        if (!new File(entryLocation + entryName + "-" + entryVersion).isDirectory()) {
            try {
                Util.decompressZipfile(entryLocation + entryName + "-" + entryVersion + ".zip", entryLocation + entryName + "-" + entryVersion);
            } catch (ZipException ex) {
                Logger.error("Modpack.evaluateCacheEntry", "Failed to extract entry archive!", false, ex.getMessage());
                return;
            }
        }

        JSONObject entryConfig = null;
        try {
            entryConfig = Util.readJSONFile(entryLocation + entryName + "-" + entryVersion + "/entry.json");
        } catch (IOException ex) {
            Logger.error("Modpack.evaluateCacheEntry", "Cache entry was not found!", false, "None");
            return;
        }

        if (installQueue != null) {
            installQueue.add(entryName + "," + entryVersion);
        }
        
        if (entryConfig.get("settings") != null) {
            JSONArray settingsArray = (JSONArray) entryConfig.get("settings");
            for (Object obj : settingsArray) {
                JSONObject setting = (JSONObject) obj;
                setSetting((String) setting.get("name"), (String) setting.get("value"));
            }
        }
        
        if (installQueue != null) {
            if (entryConfig.get("dependencies") != null) {
                JSONArray dependenciesArray = (JSONArray) entryConfig.get("dependencies");
                for (Object obj : dependenciesArray) {
                    JSONObject dep = (JSONObject) obj;
                    if (!installQueue.contains((String)dep.get("name"))) {
                        evaluateCacheEntry((String) dep.get("name"), (String) dep.get("version"), installQueue, excludeQueue);
                    }
                }
            }

            if (entryConfig.get("exclusions") != null) {
                JSONArray exclusionsArray = (JSONArray) entryConfig.get("exclusions");
                for (Object obj : exclusionsArray) {
                    JSONObject exclude = (JSONObject) obj;
                    if (!excludeQueue.contains((String)exclude.get("name"))) {
                        excludeQueue.add((String) exclude.get("name"));
                    }
                }
            }
        }
    }
    
    public void parseCacheEntry(String entryName, String entryVersion) {
        String entryLocation = "";
        if (new File("./data/" + entryName + "-" + entryVersion + ".zip").exists()) {
            entryLocation = "./data/";
        } else if (new File("./users/" + PlayerOwner + "/" + ModpackName + "/ark/cache/" + entryName + "-" + entryVersion + ".zip").exists()) {
            entryLocation = "./users/" + PlayerOwner + "/" + ModpackName + "/ark/cache/";
        } else if (new File("./cache/" + entryName + "-" + entryVersion + ".zip").exists()) {
            entryLocation = "./cache/";
        } else {
            Logger.error("Modpack.evaluateCacheEntry", "Cache entry was not found!", false, "None");
            return;
        }
        
        JSONObject entryConfig = null;
        try {
            entryConfig = Util.readJSONFile(entryLocation + entryName + "-" + entryVersion + "/entry.json");
        } catch (IOException ex) {
            Logger.error("Modpack.evaluateCacheEntry", "Cache entry was not found!", false, "None");
            return;
        }

        if (entryConfig.get("settings") != null) {
            JSONArray settingsArray = (JSONArray) entryConfig.get("settings");
            for (Object obj : settingsArray) {
                JSONObject setting = (JSONObject) obj;
                setSetting((String) setting.get("name"), (String) setting.get("value"));
            }
        }
    }
    
    public void getCacheEntry(String entryName, String entryVersion, String targetDir, boolean fatality) {
        String entryLocation = "";
        if (new File("./data/" + entryName + "-" + entryVersion + ".zip").exists()) {
            entryLocation = "./data/";
        } else if (new File("./users/" + PlayerOwner + "/" + ModpackName + "/ark/cache/" + entryName + "-" + entryVersion + ".zip").exists()) {
            entryLocation = "./users/" + PlayerOwner + "/" + ModpackName + "/ark/cache/";
        } else if (new File("./cache/" + entryName + "-" + entryVersion + ".zip").exists()) {
            entryLocation = "./cache/";
        } else {
            Logger.error("Modpack.getCacheEntry", "Cache entry was not found!", fatality, "None");
            return;
        }
        
        JSONObject entryConfig = null;
        try {
            entryConfig = Util.readJSONFile(entryLocation + entryName + "-" + entryVersion + "/entry.json");
        } catch (IOException ex) {
            Logger.error("Modpack.getCacheEntry", "Cache entry was not found!", fatality, "None");
            return;
        }

        JSONArray filesArray = (JSONArray) entryConfig.get("files");
        for (Object obj : filesArray) {
            JSONObject file = (JSONObject) obj;
            try {
                if (!new File(entryLocation + entryName + "-" + entryVersion + "/" + (String) file.get("name")).exists()) {
                    FileUtils.copyURLToFile(new URL((String)file.get("url")), new File(entryLocation + entryName + "-" + entryVersion + "/" + (String) file.get("name")));
                }
            } catch (IOException ex) {
                Logger.error("Modpack.getCacheEntry", "Failed to obtain file '" + (String) file.get("name") + "'!", false, ex.getMessage());
            }
            try {
                if (((String) file.get("action")).equalsIgnoreCase("extract-zip")) {
                    Util.decompressZipfile(entryLocation + entryName + "-" + entryVersion + "/" + (String) file.get("name"), targetDir + (String) file.get("target"));
                } else if (((String) file.get("action")).equalsIgnoreCase("copy-file")) {
                    FileUtils.copyFile(new File(entryLocation + entryName + "-" + entryVersion + "/" + (String) file.get("name")), new File(targetDir + (String) file.get("target")));
                }
            } catch (Exception ex) {
                Logger.error("Modpack.getCacheEntry", "Failed to process file '" + (String) file.get("name") + "'!", false, ex.getMessage());
            }
        }
        
    }
    
    public void getPseudoEntry(String entryName, String entryVersion, boolean fatality) {
        /*if (Util.getFile("./data/" + entryName + "-" + entryVersion + ".zip").exists()) {
            try {
                Util.decompressZipfile("./data/" + entryName + "-" + entryVersion + ".zip", "./users/" + PlayerOwner + "/" + ModpackName + "/entries/" + entryName + "-" + entryVersion);
            } catch (IOException ex) {
                Logger.error("Modpack.getCacheEntry", "Failed to extract entry archive!", fatality, ex.getMessage());
                return;
            }
        } else if (Util.getFile("./users/" + PlayerOwner + "/" + ModpackName + "/ark/cache/" + entryName + "-" + entryVersion + ".zip").exists()) {
            try {
                Util.decompressZipfile("./users/" + PlayerOwner + "/" + ModpackName + "/ark/cache/" + entryName + "-" + entryVersion + ".zip", "./users/" + PlayerOwner + "/" + ModpackName + "/entries/" + entryName + "-" + entryVersion);
            } catch (IOException ex) {
                Logger.error("Modpack.getCacheEntry", "Failed to extract entry archive!", fatality, ex.getMessage());
                return;
            }
        } else if (Util.getFile("./cache/" + entryName + "-" + entryVersion + ".zip").exists()) {
            try {
                Util.decompressZipfile("./cache/" + entryName + "-" + entryVersion + ".zip", "./users/" + PlayerOwner + "/" + ModpackName + "/entries/" + entryName + "-" + entryVersion);
            } catch (IOException ex) {
                Logger.error("Modpack.getCacheEntry", "Failed to extract entry archive!", fatality, ex.getMessage());
                return;
            }
        } else {
            Logger.error("Modpack.getCacheEntry", "Cache entry was not found!", fatality, "None");
            return;
        }*/

        JSONObject entryConfig = null;
        try {
            entryConfig = Util.readJSONFile("./users/" + PlayerOwner + "/" + ModpackName + "/entries/" + entryName + "-" + entryVersion + "/entry.json");
        } catch (IOException ex) {
            Logger.error("Modpack.getCacheEntry", "Cache entry was not found!", fatality, "None");
            return;
        }

        JSONArray settingsArray = (JSONArray) entryConfig.get("settings");
        LaunchArguments.loadSettings(settingsArray);
        
    }
    
    
    
    public boolean run() {
        try {

            if (LaunchArguments == null) {
                setSetting("launch.AppDataDirectory", Util.getFullPath("./users/" + PlayerOwner + "/" + ModpackName));
                
                JSONObject packConfig = null;
                try {
                    packConfig = Util.readJSONFile("./users/" + PlayerOwner + "/" + ModpackName + "/ark/pack.json");
                } catch (IOException ex) {
                    Logger.error("Modpack.run", "Failed to read pack.json!", true, ex.getMessage());
                    return false;
                }
                
                setSetting("general.MinecraftVersion", (String) packConfig.get("minecraftversion"));
                
                List<String> installQueue = new ArrayList();
                List<String> excludeQueue = new ArrayList();

                JSONArray entriesArray = (JSONArray)packConfig.get("entries");
                for (Object obj : entriesArray) {
                    JSONObject entryObj = (JSONObject)obj;
                    Logger.info("Modpack.build", "Evaluating entry '" + (String) entryObj.get("name") + "'...");
                    evaluateCacheEntry((String)entryObj.get("name"), (String)entryObj.get("version"), installQueue, excludeQueue);
                }

                Logger.info("Modpack.build", "Evaluating entry 'moddleassets'...");
                evaluateCacheEntry("moddleassets", "0.2", installQueue, excludeQueue);

                List<String> finalQueue = new ArrayList();

                Logger.info("Modpack.build", "Building installation queue...");
                for (String entry : installQueue) {
                    boolean install = true;
                    for (String exclusion : excludeQueue) {
                        if (entry.startsWith(exclusion)) {
                            install = false;
                            break;
                        }
                    }
                    if (install) {
                        finalQueue.add(entry);
                    }
                }

                parseCacheEntry("minecraft", (String)packConfig.get("minecraftversion"));
                
                for (String entry : finalQueue) {
                    String[] entryData = entry.split(",");
                    Logger.info("Installing entry " + entryData[0] + "...");
                    parseCacheEntry(entryData[0], entryData[1]);
                }
            }
            
            Logger.info("Building process arguments...");
            List<String> args = new ArrayList();

            //<editor-fold defaultstate="collapsed" desc="Java Core Arguments">
            
            Logger.info("    JavaExecutablePath");
            if (!getSetting("launch.JavaExecutablePath").equals("null")) {
                args.add(getSetting("launch.JavaExecutablePath"));
            } else {
                args.add("javaw.exe");
            }

            Logger.info("    XmxArgument");
            if (getSettingBool("launch.UseXmxArgument")) {
                args.add("-Xmx" + getSetting("launch.XmxArgument") + "M");
            }

            Logger.info("    XmsArgument");
            if (getSettingBool("launch.UseXmsArgument")) {
                args.add("-Xms" + getSetting("launch.XmsArgument") + "M");
            }

            Logger.info("    DJavaLibPathArgument");
            if (getSettingBool("launch.UseDJavaLibPathArgument")) {
                args.add("-Djava.library.path=\"" + Util.getFullPath(getSetting("launch.DJavaLibPathArgument")) + "\"");
            }

            Logger.info("    ClassPathArgument");
            if (getSettingBool("launch.UseClassPathArgument")) {
                String cpArg = "";//"\"";
                if (getSettingBool("launch.GetLibraryClassPaths")) {
                    String libCp = getLibraryJarfiles("./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft/libraries");
                    cpArg += libCp.substring(0, libCp.length() - 1);
                }
                if (getSettingBool("launch.GetMinecraftClassPath")) {
                    if (cpArg.length() > 0) {
                        cpArg += ";";
                    }
                    cpArg += Util.getFullPath("./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft/versions/" + getSetting("general.MinecraftVersion") + "/" + getSetting("general.MinecraftVersion") + ".jar");
                }
                if (!AdditionalClassPathEntries.isEmpty()) {
                    if (cpArg.length() > 0) {
                        cpArg += ";";
                    }
                    for (String cpArgEntry : AdditionalClassPathEntries) {
                        cpArg += parseSettingsString(cpArgEntry) + ";";
                    }
                    cpArg = cpArg.substring(0, cpArg.length() - 2);
                }
                args.add("-cp");
                args.add("\"" + cpArg + "\"");
            }

            Logger.info("    MainClassArgument");
            if (getSettingBool("launch.UseMainClassArgument")) {
                args.add(getSetting("launch.MainClassArgument"));
            }

            Logger.info("    AdditionalCoreArguments");
            if (!AdditionalCoreLaunchArguments.isEmpty()) {
                for (String additionalArg : AdditionalCoreLaunchArguments) {
                    args.add(parseSettingsString(additionalArg));
                }
            }

            //</editor-fold>
            
            //<editor-fold defaultstate="collapsed" desc="Minecraft Arguments">
            
            Logger.info("    UseLegacyUsernameAndSession");
            if (getSettingBool("launch.UseLegacyUsernameAndSession")) {
                args.add(LoginHelper.Username);
                args.add(LoginHelper.AccessToken);
            }

            Logger.info("    UseGameDirArgument");
            if (getSettingBool("launch.UseGameDirArgument")) {
                args.add("--gameDir");
                args.add(getSetting("launch.GameDirArgument"));
            }

            Logger.info("    UseAssetDirArgument");
            if (getSettingBool("launch.UseAssetDirArgument")) {
                args.add("--assetDir");
                args.add(getSetting("launch.AssetDirArgument"));
            }

            Logger.info("    UseVersionArgument");
            if (getSettingBool("launch.UseVersionArgument")) {
                args.add("--version");
                args.add(getSetting("launch.VersionArgument"));
            }

            Logger.info("    UseUsernameArgument");
            if (getSettingBool("launch.UseUsernameArgument")) {
                args.add("--username");
                args.add(LoginHelper.Username);
            }

            Logger.info("    UseSessionArgument");
            if (getSettingBool("launch.UseSessionArgument")) {
                args.add("--session");
                args.add(LoginHelper.AccessToken);
            }

            Logger.info("    UseUUIDArgument");
            if (getSettingBool("launch.UseUUIDArgument")) {
                args.add("--uuid");
                args.add(LoginHelper.UUID);
            }

            Logger.info("    UseAccessTokenArgument");
            if (getSettingBool("launch.UseAccessTokenArgument")) {
                args.add("--accessToken");
                args.add(LoginHelper.AccessToken);
            }

            Logger.info("    UseUserPropertiesArgument");
            if (getSettingBool("launch.UseUserPropertiesArgument")) {
                args.add("--userProperties");
                args.add(LoginHelper.UserProperties);
            }

            Logger.info("    UseUserTypeArgument");
            if (getSettingBool("launch.UseUserTypeArgument")) {
                args.add("--userType");
                args.add(LoginHelper.UserType);
            }

            Logger.info("    UseTweakClassArgument");
            if (getSettingBool("launch.UseTweakClassArgument")) {
                args.add("--tweakClass");
                args.add(getSetting("launch.TweakClassArgument"));
            }
            
            Logger.info("    AdditionalMinecraftArguments");
            if (!AdditionalMinecraftLaunchArguments.isEmpty()) {
                for (String additionalArg : AdditionalMinecraftLaunchArguments) {
                    args.add(parseSettingsString(additionalArg));
                }
            }

            //</editor-fold>
            
            String[] argArray = new String[args.toArray().length];
            for (int i = 0; i < argArray.length; i++) {
                argArray[i] = args.get(i);
            }

            ProcessBuilder launcher = new ProcessBuilder(argArray);

            Logger.info("Setting environment variables...");
            Map<String, String> env = launcher.environment();
            //env.put("APPDATA", Util.getFile("./packs/" + ModpackName).getCanonicalPath());
            env.put("APPDATA", getSetting("launch.AppDataDirectory"));

            Logger.info("Launching process!");
            launcher.directory(new File("./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft"));
            launcher.start();

            return true;

        } catch (Exception ex) {
            Logger.error("PackBuilder.run", ex.getMessage(), true, ex.getMessage());
            return false;
        }
    }

    private String getLibraryJarfiles(String dir) {
        try {

            String ret = "";
            for (File item : new File(dir).listFiles()) {
                if (item.isDirectory()) {
                    ret += getLibraryJarfiles(item.getCanonicalPath());
                } else {
                    if (item.getName().endsWith(".jar")) {
                        ret += item.getCanonicalPath() + ";";
                    }
                }
            }
            return ret;

        } catch (Exception ex) {
            Logger.error("PackBuilder.getLibraryJarfiles", ex.getMessage(), false, ex.getMessage());
            return null;
        }
    }

}
