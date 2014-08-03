
package com.dsdev.moddle;

import com.dsdev.moddle.auth.Auth;
import com.dsdev.moddle.util.Logger;
import com.dsdev.moddle.util.Util;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Diamond Sword Development
 */
public class Instances {
    
    public static boolean isInstanceComplete(String account, String name) {
        return (new File("./users/" + account + "/" + name + "/completeinstall").isFile());
    }
    
    public static void setInstanceIncomplete(String account, String name) {
        new File("./users/" + account + "/" + name + "/completeinstall").delete();
    }
    
    public static void setInstanceComplete(String account, String name) {
        try {
            new File("./users/" + account + "/" + name + "/completeinstall").createNewFile();
        } catch (IOException ex) { }
    }
    
    
    public static boolean buildInstance(String account, String name) {
        
        //Clear progress (partially redundant)
        GlobalDialogs.setProgressCaption("Initializing...");
        GlobalDialogs.setProgressValue(0);
        
        //Mark instance as incomplete
        Logger.info("Instances.buildInstance", "Updating instance status as 'incomplete'...");
        setInstanceIncomplete(account, name);
        
        String instancePath = "./users/" + account + "/" + name;
        
        //Load instance config
        Logger.info("Instances.buildInstance", "Loading instance config...");
        JSONObject instanceConfig;
        try {
            instanceConfig = Util.readJSONFile(instancePath + "/instance.json");
        } catch (IOException ex) {
            Logger.error("Instances.buildInstance", "Failed to read instance.json!", true, ex.getMessage());
            GlobalDialogs.showNotification("Failed to load instance!");
            return false;
        }
        
        //Load pack config
        Logger.info("Instances.buildInstance", "Loading pack config...");
        JSONObject packConfig;
        try {
            packConfig = Util.readJSONFile("./packs/" + instanceConfig.get("pack").toString() + "/pack.json");
        } catch (IOException ex) {
            Logger.error("Instances.buildInstance", "Failed to read pack.json!", true, ex.getMessage());
            GlobalDialogs.showNotification("Failed to load modpack!");
            return false;
        }
        
        //Announce installation queue build
        GlobalDialogs.setProgressCaption("Building installation queue...");
        GlobalDialogs.setProgressValue(0);
        
        //Get entries queued for installation
        Logger.info("Instances.buildInstance", "Building installation queue...");
        List<String> installationQueue = getInstallationQueue(packConfig, instanceConfig.get("version").toString());
        
        //Announce settings being loaded
        GlobalDialogs.setProgressCaption("Loading settings...");
        GlobalDialogs.setProgressValue(0);
        
        //Set mod list settings
        Logger.info("Instances.buildInstance", "Adding mods to installation queue setting...");
        for (String mod : installationQueue) {
            Variables.setSetting("mods.Installed", mod);
        }
        
        //Set APPDATA setting
        Variables.setSetting("launch.AppDataDirectory", Util.getFullPath(instancePath));
        
        //Set all other settings
        Logger.info("Instances.buildInstance", "Loading settings...");
        loadPackSettings(packConfig, installationQueue);
        
        //Create .minecraft directory
        Util.createDirectoryIfNeeded(instancePath + "/.minecraft");
        
        //Install cache entries
        for (String entry : installationQueue) {
            //Announce entry
            GlobalDialogs.setProgressCaption("Installing entry '" + entry.split("-")[0] + "'...");
            
            //Install entry
            Logger.info("Instances.buildInstance", "Installing entry '" + entry.split("-")[0] + "'...");
            installCacheEntry(entry.split("-")[0], entry.split("-")[1], "./packs/" + instanceConfig.get("pack").toString() + "/cache", "./users/" + account + "/" + name + "/.minecraft");
            
            //Update progress
            GlobalDialogs.incrementProgressValue(100 / installationQueue.size());
        }
        
        //Announce Minecraft installation
        GlobalDialogs.setProgressCaption("Obtaining Minecraft jarfile...");
        GlobalDialogs.setProgressValue(10);
        
        //Install Minecraft jarfile
        Logger.info("Instances.buildInstance", "Obtaining Minecraft jarfile...");
        String mcVersion = packConfig.get("minecraftversion").toString();
        Util.createDirectoryIfNeeded(instancePath + "/.minecraft/versions/" + mcVersion);
        Util.createDirectoryIfNeeded("./data/versions");
        if (!(new File("./data/versions/" + mcVersion + ".jar").isFile())) {
            //Download file
            Logger.info("Instances.buildInstance", "Version not found!  Will download...");
            try {
                FileUtils.copyURLToFile(new URL("http://s3.amazonaws.com/Minecraft.Download/versions/" + mcVersion + "/" + mcVersion + ".jar"), new File("./data/versions/" + mcVersion + ".jar"));
            } catch (IOException ex) {
                Logger.error("Modpack.build", "Failed to download Minecraft jarfile!", true, ex.getMessage());
                GlobalDialogs.showNotification("Failed to get required Minecraft version!");
                return false;
            }
            
            //Update progress
            GlobalDialogs.setProgressValue(50);
        }
        Logger.info("Instances.buildInstance", "Copying Minecraft jarfile...");
        try {
            FileUtils.copyFile(new File("./data/versions/" + mcVersion + ".jar"), new File(instancePath + "/.minecraft/versions/" + mcVersion + "/" + mcVersion + ".jar"));
        } catch (IOException ex) {
            Logger.error("Modpack.build", "Failed to copy Minecraft jarfile!", true, ex.getMessage());
            GlobalDialogs.showNotification("Failed to install Minecraft jarfile!");
            return false;
        }
        
        //Update progress
        GlobalDialogs.setProgressCaption("Finishing up...");
        GlobalDialogs.setProgressValue(100);
        
        //Set instance complete
        Logger.info("Instances.buildInstance", "Updating instance status as 'complete'...");
        setInstanceComplete(account, name);
        
        return true;
    }
    
    public static Process runInstance(String account, String name) {
        
        //Make progress indeterminate
        GlobalDialogs.setProgressIndeterminate(true);
        GlobalDialogs.setProgressCaption("Preparing to launch...");
        
        String instancePath = "./users/" + account + "/" + name;
        
        //Load instance config
        Logger.info("Instances.runInstance", "Loading instance config...");
        JSONObject instanceConfig;
        try {
            instanceConfig = Util.readJSONFile(instancePath + "/instance.json");
        } catch (IOException ex) {
            Logger.error("Instances.buildInstance", "Failed to read instance.json!", true, ex.getMessage());
            GlobalDialogs.showNotification("Failed to load instance! (Second Pass)");
            return null;
        }
        
        //Load pack config
        Logger.info("Instances.runInstance", "Loading pack config...");
        JSONObject packConfig;
        try {
            packConfig = Util.readJSONFile("./packs/" + instanceConfig.get("pack").toString() + "/pack.json");
        } catch (IOException ex) {
            Logger.error("Instances.buildInstance", "Failed to read pack.json!", true, ex.getMessage());
            GlobalDialogs.showNotification("Failed to load modpack! (Second Pass)");
            return null;
        }
        
        //Get entries queued for installation
        Logger.info("Instances.runInstance", "Building installation queue...");
        List<String> installationQueue = getInstallationQueue(packConfig, instanceConfig.get("version").toString());
        
        //Add installation queue variable
        Logger.info("Instances.runInstance", "Adding mods to installation queue setting...");
        for (String mod : installationQueue) {
            Variables.setSetting("mods.Installed", mod);
        }
        
        //Set APPDATA setting
        Variables.setSetting("launch.AppDataDirectory", Util.getFullPath(instancePath));
        
        //Set all other settings
        Logger.info("Instances.runInstance", "Loading settings...");
        loadPackSettings(packConfig, installationQueue);

        Logger.info("Instances.runInstance", "Preparing arguments...");
        List<String> args = new ArrayList();

        //There are so many logger statements in here, you be able to tell what's going on
        
        //<editor-fold defaultstate="collapsed" desc="Java Core Arguments">
        Logger.info("Instances.runInstance", "Parsing JavaExecutablePath...");
        if (!Variables.getSetting("launch.JavaExecutablePath").equals("null")) {
            args.add(Variables.getSetting("launch.JavaExecutablePath"));
        } else {
            args.add("javaw.exe");
        }

        Logger.info("Instances.runInstance", "Parsing XmxArgument...");
        if (Variables.getSettingBool("launch.UseXmxArgument")) {
            args.add("-Xmx" + Variables.getSetting("launch.XmxArgument") + "M");
        }

        Logger.info("Instances.runInstance", "Parsing XmsArgument...");
        if (Variables.getSettingBool("launch.UseXmsArgument")) {
            args.add("-Xms" + Variables.getSetting("launch.XmsArgument") + "M");
        }

        Logger.info("Instances.runInstance", "Parsing DJavaLibPathArgument...");
        if (Variables.getSettingBool("launch.UseDJavaLibPathArgument")) {
            args.add("-Djava.library.path=\"" + Util.getFullPath(Variables.getSetting("launch.DJavaLibPathArgument")) + "\"");
        }

        Logger.info("Instances.runInstance", "Parsing ClassPathArgument...");
        if (Variables.getSettingBool("launch.UseClassPathArgument")) {
            String cpArg = "";//"\"";
            if (Variables.getSettingBool("launch.GetLibraryClassPaths")) {
                String libCp = getLibraryJarfiles("./users/" + account + "/" + name + "/.minecraft/libraries");
                cpArg += libCp.substring(0, libCp.length() - 1);
            }
            if (Variables.getSettingBool("launch.GetMinecraftClassPath")) {
                if (cpArg.length() > 0) {
                    cpArg += ";";
                }
                cpArg += Util.getFullPath("./users/" + account + "/" + name + "/.minecraft/versions/" + Variables.getSetting("general.MinecraftVersion") + "/" + Variables.getSetting("general.MinecraftVersion") + ".jar");
            }
            List<String> additionalClassPathEntries = Variables.getSettingList("launch.AdditionalClassPathEntries");
            if (!additionalClassPathEntries.isEmpty()) {
                if (cpArg.length() > 0) {
                    cpArg += ";";
                }
                for (String cpArgEntry : additionalClassPathEntries) {
                    cpArg += cpArgEntry + ";";
                }
                cpArg = cpArg.substring(0, cpArg.length() - 2);
            }
            args.add("-cp");
            args.add("\"" + cpArg + "\"");
        }

        Logger.info("Instances.runInstance", "Parsing MainClassArgument...");
        if (Variables.getSettingBool("launch.UseMainClassArgument")) {
            args.add(Variables.getSetting("launch.MainClassArgument"));
        }

        Logger.info("Instances.runInstance", "Parsing AdditionalCoreArguments...");
        if (!Variables.getSettingList("launch.AdditionalCoreLaunchArguments").isEmpty()) {
            for (String additionalArg : Variables.getSettingList("launch.AdditionalCoreLaunchArguments")) {
                args.add(additionalArg);
            }
        }

        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Minecraft Arguments">
        Logger.info("Instances.runInstance", "Parsing UseLegacyUsernameAndSession...");
        if (Variables.getSettingBool("launch.UseLegacyUsernameAndSession")) {
            args.add(Auth.Username);
            args.add(Auth.AccessToken);
        }

        Logger.info("Instances.runInstance", "Parsing UseGameDirArgument...");
        if (Variables.getSettingBool("launch.UseGameDirArgument")) {
            args.add("--gameDir");
            args.add(Variables.getSetting("launch.GameDirArgument"));
        }

        Logger.info("Instances.runInstance", "Parsing UseAssetDirArgument...");
        if (Variables.getSettingBool("launch.UseAssetDirArgument")) {
            args.add("--assetDir");
            args.add(Variables.getSetting("launch.AssetDirArgument"));
        }

        Logger.info("Instances.runInstance", "Parsing UseVersionArgument...");
        if (Variables.getSettingBool("launch.UseVersionArgument")) {
            args.add("--version");
            args.add(Variables.getSetting("launch.VersionArgument"));
        }

        Logger.info("Instances.runInstance", "Parsing UseUsernameArgument...");
        if (Variables.getSettingBool("launch.UseUsernameArgument")) {
            args.add("--username");
            args.add(Auth.Username);
        }

        Logger.info("Instances.runInstance", "Parsing UseSessionArgument...");
        if (Variables.getSettingBool("launch.UseSessionArgument")) {
            args.add("--session");
            args.add(Auth.AccessToken);
        }

        Logger.info("Instances.runInstance", "Parsing UseUUIDArgument...");
        if (Variables.getSettingBool("launch.UseUUIDArgument")) {
            args.add("--uuid");
            args.add(Auth.UUID);
        }

        Logger.info("Instances.runInstance", "Parsing UseAccessTokenArgument...");
        if (Variables.getSettingBool("launch.UseAccessTokenArgument")) {
            args.add("--accessToken");
            args.add(Auth.AccessToken);
        }

        //Need to find out what these actually do!
        /*Logger.info("Modpack.run", "Parsing UseUserPropertiesArgument...");
        if (getSettingBool("launch.UseUserPropertiesArgument")) {
            args.add("--userProperties");
            args.add(LoginHelper.UserProperties);
        }

        Logger.info("Modpack.run", "Parsing UseUserTypeArgument...");
        if (getSettingBool("launch.UseUserTypeArgument")) {
            args.add("--userType");
            args.add(LoginHelper.UserType);
        }*/

        Logger.info("Instances.runInstance", "Parsing UseTweakClassArgument...");
        if (Variables.getSettingBool("launch.UseTweakClassArgument")) {
            args.add("--tweakClass");
            args.add(Variables.getSetting("launch.TweakClassArgument"));
        }
        
        Logger.info("Instances.runInstance", "Parsing UseWidthArgument...");
        if (Variables.getSettingBool("launch.UseWidthArgument")) {
            args.add("--width");
            args.add(Variables.getSetting("launch.WidthArgument"));
        }
        
        Logger.info("Instances.runInstance", "Parsing UseHeightArgument...");
        if (Variables.getSettingBool("launch.UseHeightArgument")) {
            args.add("--height");
            args.add(Variables.getSetting("launch.HeightArgument"));
        }

        Logger.info("Instances.runInstance", "Parsing AdditionalMinecraftArguments...");
        if (!Variables.getSettingList("launch.AdditionalMinecraftLaunchArguments").isEmpty()) {
            for (String additionalArg : Variables.getSettingList("launch.AdditionalMinecraftLaunchArguments")) {
                args.add(additionalArg);
            }
        }

        //</editor-fold>

        //Put args in an array (wha... what is this... this... boilerplate code?!?)
        String[] argArray = new String[args.toArray().length];
        for (int i = 0; i < argArray.length; i++) {
            argArray[i] = args.get(i);
        }

        //Make the ProcessBuilder
        ProcessBuilder launcher = new ProcessBuilder(argArray);

        //Set the actual APPDATA variable
        Logger.info("Instances.runInstance", "Setting environment variables...");
        Map<String, String> env = launcher.environment();
        env.put("APPDATA", Variables.getSetting("launch.AppDataDirectory"));

        //Set working directory
        launcher.directory(new File("./users/" + account + "/" + name + "/.minecraft"));

        //Launch!
        Logger.info("Instances.runInstance", "Launching process!");
        Process processHandle;
        try {
            processHandle = launcher.start();
        } catch (IOException ex) {
            Logger.error("Instances.runInstance", "Failed to launch process!", true, ex.getMessage());
            GlobalDialogs.showNotification("Failed to launch Minecraft!");
            return null;
        }

        //Redirect output to the console
        inheritIO(processHandle.getInputStream(), System.out);
        inheritIO(processHandle.getErrorStream(), System.err);
        
        return processHandle;
    }
    
    
    private static List<String> getInstallationQueue(JSONObject packConfig, String packVersion) {
        
        //Get version entries
        Logger.info("Instances.getInstallationQueue", "Getting entry list...");
        JSONArray entriesArray = null;
        if (packVersion.equalsIgnoreCase("recommended")) {
            for (Object obj : (JSONArray)packConfig.get("versions")) {
                JSONObject versionObj = (JSONObject)obj;
                if (versionObj.get("name").toString().equals(packConfig.get("recommendedversion").toString())) {
                    entriesArray = (JSONArray)versionObj.get("entries");
                    break;
                }
            }
        } else {
            for (Object obj : (JSONArray)packConfig.get("versions")) {
                JSONObject versionObj = (JSONObject)obj;
                if (versionObj.get("name").toString().equals(packVersion)) {
                    entriesArray = (JSONArray)versionObj.get("entries");
                    break;
                }
            }
        }
        if (entriesArray == null) {
            Logger.error("Instances.getInstallationQueue", "Could not find entries for specified version!");
            return null;
        }
        
        //Init installation queue
        List<String> installationQueue = new ArrayList();
        
        //Announce Minecraft enqueueing
        GlobalDialogs.setProgressCaption("Enqueueing Minecraft...");
        
        //Queue Minecraft
        Logger.info("Instances.getInstallationQueue", "Enqueueing Minecraft...");
        enqueueCacheEntry("minecraft", packConfig.get("minecraftversion").toString(), "./packs/" + packConfig.get("name").toString() + "/cache", packConfig.get("minecraftversion").toString(), installationQueue);
        
        //Add pack entries to installation queue
        for (Object obj : entriesArray) {
            JSONObject entryObj = (JSONObject) obj;
            
            //Announce entry
            GlobalDialogs.setProgressCaption("Enqueueing '" + entryObj.get("name").toString() + "'...");
            
            //Enqueue entry
            Logger.info("Instances.getInstallationQueue", "Enqueueing '" + entryObj.get("name").toString() + "'...");
            enqueueCacheEntry(entryObj.get("name").toString(), entryObj.get("version").toString(), "./packs/" + packConfig.get("name").toString() + "/cache", packConfig.get("minecraftversion").toString(), installationQueue);
            
            //Update progress
            GlobalDialogs.incrementProgressValue(100 / entriesArray.size());
        }
        
        return installationQueue;
    }
    
    private static void enqueueCacheEntry(String name, String version, String packCacheLocation, String minecraftVersion, List<String> installationQueue) {
        //Get entry location
        String entryPath = getCacheEntryPath(name, version, packCacheLocation);

        if (entryPath != null) {
            
            //Load entry config
            JSONObject entryConfig;
            try {
                entryConfig = Util.readJSONFile(entryPath + "/entry.json");
            } catch (IOException ex) {
                Logger.warning("Instances.enqueueCacheEntry", "Could not load config for cache entry!");
                return;
            }
            
            //Check Minecraft version
            if (!entryConfig.get("minecraftversion").equals(minecraftVersion)) {
                Logger.warning("Instances.enqueueCacheEntry", "Entry is not compatible with this version of Minecraft!");
                return;
            }
            
            //Add to queue
            installationQueue.add(name + "-" + version);

            //Enqueue dependencies
            if (entryConfig.get("dependencies") != null) {
                for (Object obj : (JSONArray)entryConfig.get("dependencies")) {
                    JSONObject dependencyObj = (JSONObject)obj;
                    if (!installationQueue.contains(dependencyObj.get("name").toString() + "-" + dependencyObj.get("version").toString())) {
                        Logger.info("Instances.getInstallationQueue", "Enqueueing '" + dependencyObj.get("name").toString() + "'...");
                        enqueueCacheEntry(dependencyObj.get("name").toString(), dependencyObj.get("version").toString(), packCacheLocation, minecraftVersion, installationQueue);
                    }
                }
            }
        } else {
            Logger.warning("Instances.enqueueCacheEntry", "Cache entry not found!");
        }
    }

    
    private static void loadPackSettings(JSONObject packConfig, List<String> installationQueue) {
        
        //Set Minecraft version setting
        Variables.setSetting("general.MinecraftVersion", (String) packConfig.get("minecraftversion"));
        
        //Load pack settings
        if (packConfig.get("settings") != null) {
            Logger.info("Instances.loadPackSettings", "Loading pack settings...");
            Variables.loadSettingsFromJSON(packConfig, "pack");
        }
        
        //Load entry settings
        for (String entry : installationQueue) {
            JSONObject entryConfig;
            try {
                entryConfig = Util.readJSONFile(getCacheEntryPath(entry.split("-")[0], entry.split("-")[1], "./packs/" + packConfig.get("name").toString() + "/cache") + "/entry.json");
            } catch (IOException ex) {
                Logger.warning("Instances.buildInstance", "Could not load entry config!");
                continue;
            }
            Logger.info("Instances.loadPackSettings", "Loading settings for entry '" + entryConfig.get("name").toString() + "'...");
            Variables.loadSettingsFromJSON(entryConfig, "entry");
        }
    }
    
    
    private static String getCacheEntryPath(String name, String version, String packCacheLocation) {
        
        //Check data folder
        String dataLocation = "./data/" + name + "-" + version;
        if (new File(dataLocation).isDirectory()) {
            return dataLocation;
        } else if (new File(dataLocation + ".zip").isFile()) {
            try {
                Util.decompressZipfile(dataLocation + ".zip", dataLocation);
            } catch (ZipException ex) {
                Logger.warning("Instances.getCacheEntryPath", "Could not unzip cache entry!");
                return null;
            }
            return dataLocation;
        }
        
        //Check pack cache folder
        String packLocation = packCacheLocation + "/" + name + "-" + version;
        if (new File(packLocation).isDirectory()) {
            return packLocation;
        } else if (new File(packLocation + ".zip").isFile()) {
            try {
                Util.decompressZipfile(packLocation + ".zip", packLocation);
            } catch (ZipException ex) {
                Logger.warning("Instances.getCacheEntryPath", "Could not unzip cache entry!");
                return null;
            }
            return packLocation;
        }
        
        //Check cache folder
        String cacheLocation = "./cache/" + name + "-" + version;
        if (new File(cacheLocation).isDirectory()) {
            return cacheLocation;
        } else if (new File(cacheLocation + ".zip").isFile()) {
            try {
                Util.decompressZipfile(cacheLocation + ".zip", cacheLocation);
            } catch (ZipException ex) {
                Logger.warning("Instances.getCacheEntryPath", "Could not unzip cache entry!");
                return null;
            }
            return cacheLocation;
        }
        
        return null;
    }
    
    private static void getCacheEntrySettings(String name, String version, String packCacheLocation) {
        //Get entry location
        String entryPath = getCacheEntryPath(name, version, packCacheLocation);

        if (entryPath != null) {
            
            //Load entry config
            JSONObject entryConfig;
            try {
                entryConfig = Util.readJSONFile(entryPath + "/entry.json");
            } catch (IOException ex) {
                Logger.warning("Instances.enqueueCacheEntry", "Could not load config for cache entry!");
                return;
            }
            
            //Load settings
            Variables.loadSettingsFromJSON(entryConfig, "entry");
        }
    }
    
    
    private static void installCacheEntry(String name, String version, String packCacheLocation, String targetDir) {
        //Get entry location
        String entryPath = getCacheEntryPath(name, version, packCacheLocation);

        if (entryPath != null) {
            
            //Load entry config
            JSONObject entryConfig;
            try {
                entryConfig = Util.readJSONFile(entryPath + "/entry.json");
            } catch (IOException ex) {
                Logger.warning("Instances.installCacheEntry", "Could not load config for cache entry!");
                return;
            }
            
            //Process files
            for (Object obj : (JSONArray)entryConfig.get("files")) {
                JSONObject file = (JSONObject) obj;
                
                //Attempt to download file if it does not exist
                try {
                    if (!new File(entryPath + "/" + (String) file.get("name")).exists()) {
                        FileUtils.copyURLToFile(new URL((String) file.get("url")), new File(entryPath + "/" + (String) file.get("name")));
                    }
                } catch (IOException ex) {
                    Logger.error("Instances.installCacheEntry", "Failed to obtain file '" + (String) file.get("name") + "'!", false, ex.getMessage());
                }
                
                //Perform specified action for file
                try {
                    if (((String) file.get("action")).equalsIgnoreCase("extract-zip")) {
                        //'extract-zip':  decompress the file into a folder
                        Util.decompressZipfile(entryPath + "/" + (String) file.get("name"), targetDir + (String) file.get("target"));
                    } else if (((String) file.get("action")).equalsIgnoreCase("copy-file")) {
                        //'copy-file':  Copy the file and delete existing copy if necessary
                        if (new File(targetDir + (String) file.get("target")).isFile()) {
                            new File(targetDir + (String) file.get("target")).delete();
                        }
                        FileUtils.copyFile(new File(entryPath + "/" + (String) file.get("name")), new File(targetDir + (String) file.get("target")));
                    } else if (((String) file.get("action")).equalsIgnoreCase("copy-config")) {
                        //'copy-config':  Copy the file via the config parser
                        if (new File(targetDir + (String) file.get("target")).isFile()) {
                            new File(targetDir + (String) file.get("target")).delete();
                        }
                        copyTextFileWithVariables(entryPath + "/" + (String) file.get("name"), targetDir + (String) file.get("target"));
                    }
                } catch (Exception ex) {
                    Logger.error("Instances.installCacheEntry", "Failed to process file '" + (String) file.get("name") + "'!", false, ex.getClass().getSimpleName() + ": " + ex.getMessage());
                }
            }
        }
    }
    
    public static void copyTextFileWithVariables(String sourceFile, String destFile) throws IOException {
        //This be reeeal simple:  Read, parse, make directory, and write
        String sourceText = FileUtils.readFileToString(new File(sourceFile));
        String destText = Variables.parseString(sourceText);
        new File(destFile).getParentFile().mkdirs();
        FileUtils.writeStringToFile(new File(destFile), destText);
    }
    
    
    private static String getLibraryJarfiles(String dir) {
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
    
    private static void inheritIO(final InputStream src, final PrintStream dest) {
        new Thread(new Runnable() {
            public void run() {
                Scanner sc = new Scanner(src);
                while (sc.hasNextLine()) {
                    dest.println(sc.nextLine());
                }
            }
        }).start();
    }
}
