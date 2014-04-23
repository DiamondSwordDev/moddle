package com.dsdev.moddle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

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
    
    public List<String> InstalledEntries = new ArrayList();
    public List<String> ExcludedEntries = new ArrayList();

    public Modpack() { }
    
    public Modpack(String name, String player) {
        
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
    }

    
    
    public void build() {
        
        LaunchArguments = new LaunchArgs();
        
        LaunchArguments.AppDataDirectory = Util.getFullPath("./users/" + PlayerOwner + "/" + ModpackName);

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
        
        /*Logger.info("Extracting pack archive...");
        try {
            Util.decompressZipfile("./packs/" + ModpackName + ".zip", "./tmp/pack/");
        } catch (IOException ex) {
            Logger.error("Modpack.build", "Failed to extract pack archive!", true, ex.getMessage());
            return;
        }*/
        
        JSONObject packConfig = null;
        /*if (!(new File("./users/" + PlayerOwner + "/" + ModpackName + "/pack.json").exists())) {
            try {
                FileUtils.copyFile(new File("./tmp/pack/pack.json"), new File("./users/" + PlayerOwner + "/" + ModpackName + "/pack.json"));
            } catch (IOException ex) {
                Logger.error("Modpack.build", "Failed to copy pack config file!", true, ex.getMessage());
                return;
            }
        }*/
        try {
            packConfig = Util.readJSONFile("./users/" + PlayerOwner + "/" + ModpackName + "/ark/pack.json");
        } catch (IOException ex) {
            Logger.error("Modpack.build", "Failed to read pack.json!", true, ex.getMessage());
            return;
        }
        
        JSONObject launchArgTemplate = new JSONObject();
        
        Logger.info("Building skeleton installation...");
        getCacheEntry("minecraft", (String) packConfig.get("minecraftversion"), "./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft", true);

        //<editor-fold defaultstate="collapsed" desc="Install Minecraft Jarfile">

        Logger.info("Creating '.minecraft/versions/' ...");
        Util.assertDirectoryExistence("./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft/versions");

        Logger.info("Creating '.minecraft/versions/<version>/' ...");
        Util.assertDirectoryExistence("./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft/versions/" + packConfig.get("minecraftversion"));

        Logger.info("Obtaining Minecraft jarfile...");
        Util.assertDirectoryExistence("./data/versions");
        if (!Util.getFile("./data/versions/" + packConfig.get("minecraftversion") + ".jar").exists()) {
            Logger.info("Version does not exist.  Downloading...");
            try {
                FileUtils.copyURLToFile(new URL("http://s3.amazonaws.com/Minecraft.Download/versions/" + packConfig.get("minecraftversion") + "/" + packConfig.get("minecraftversion") + ".jar"), new File("./data/versions/" + packConfig.get("minecraftversion") + ".jar"));
            } catch (IOException ex) {
                Logger.error("Modpack.build", "Failed to download Minecraft jarfile!", true, ex.getMessage());
                return;
            }
        }
        try {
        FileUtils.copyFile(new File("./data/versions/" + packConfig.get("minecraftversion") + ".jar"), new File("./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft/versions/" + packConfig.get("minecraftversion") + "/" + packConfig.get("minecraftversion") + ".jar"));
        } catch (IOException ex) {
            Logger.error("Modpack.build", "Failed to copy Minecraft jarfile!", true, ex.getMessage());
            return;
        }
        
        //</editor-fold>

        JSONArray entriesArray = (JSONArray)packConfig.get("entries");
        for (Object obj : entriesArray) {
            JSONObject entryObj = (JSONObject)obj;
            Logger.info("Installing entry " + (String)entryObj.get("name") + "...");
            getCacheEntry((String)entryObj.get("name"), (String)entryObj.get("version"), "./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft", false);
        }
    }

    public void getCacheEntry(String entryName, String entryVersion, String targetDir, boolean fatality) {
        if (Util.getFile("./data/" + entryName + "-" + entryVersion + ".zip").exists()) {
            try {
                Util.decompressZipfile("./data/" + entryName + "-" + entryVersion + ".zip", "./tmp/cache/" + entryName + "-" + entryVersion);
            } catch (IOException ex) {
                Logger.error("Modpack.getCacheEntry", "Failed to extract entry archive!", fatality, ex.getMessage());
                return;
            }
        } else if (Util.getFile("./tmp/pack/cache/" + entryName + "-" + entryVersion + ".zip").exists()) {
            try {
                Util.decompressZipfile("./tmp/pack/cache/" + entryName + "-" + entryVersion + ".zip", "./tmp/cache/" + entryName + "-" + entryVersion);
            } catch (IOException ex) {
                Logger.error("Modpack.getCacheEntry", "Failed to extract entry archive!", fatality, ex.getMessage());
                return;
            }
        } else if (Util.getFile("./cache/" + entryName + "-" + entryVersion + ".zip").exists()) {
            try {
                Util.decompressZipfile("./cache/" + entryName + "-" + entryVersion + ".zip", "./tmp/cache/" + entryName + "-" + entryVersion);
            } catch (IOException ex) {
                Logger.error("Modpack.getCacheEntry", "Failed to extract entry archive!", fatality, ex.getMessage());
                return;
            }
        } else {
            Logger.error("Modpack.getCacheEntry", "Cache entry was not found!", fatality, "None");
            return;
        }

        JSONObject entryConfig = null;
        try {
            entryConfig = Util.readJSONFile("./tmp/cache/" + entryName + "-" + entryVersion + "/entry.json");
        } catch (IOException ex) {
            Logger.error("Modpack.getCacheEntry", "Cache entry was not found!", fatality, "None");
            return;
        }

        JSONArray filesArray = (JSONArray) entryConfig.get("files");
        for (Object obj : filesArray) {
            JSONObject file = (JSONObject) obj;
            try {
                if (((String) file.get("action")).equalsIgnoreCase("extract-zip")) {
                    Util.decompressZipfile("./tmp/cache/" + entryName + "-" + entryVersion + "/" + (String) file.get("name"), targetDir + (String) file.get("target"));
                }
            } catch (IOException ex) {
                Logger.error("Modpack.getCacheEntry", "Failed to process file '" + (String) file.get("name") + "'!", false, ex.getMessage());
            }
        }
        
        JSONArray settingsArray = (JSONArray) entryConfig.get("settings");
        LaunchArguments.loadSettings(settingsArray);
        
    }
    
    
    
    public boolean run(LaunchArgs launchArgs, MinecraftLogin login) {
        try {

            Logger.info("Building process arguments...");
            List<String> args = new ArrayList();

            //<editor-fold defaultstate="collapsed" desc="Java Core Arguments">
            
            Logger.info("    JavaExecutablePath");
            if (launchArgs.JavaExecutablePath != null) {
                args.add(launchArgs.parseString(launchArgs.JavaExecutablePath));
            } else {
                args.add("javaw.exe");
            }

            Logger.info("    XmxArgument");
            if (launchArgs.UseXmxArgument) {
                args.add("-Xmx" + Integer.toString(launchArgs.XmxArgument) + "M");
            }

            Logger.info("    XmsArgument");
            if (launchArgs.UseXmsArgument) {
                args.add("-Xms" + Integer.toString(launchArgs.XmsArgument) + "M");
            }

            Logger.info("    DJavaLibPathArgument");
            if (launchArgs.UseDJavaLibPathArgument) {
                args.add("-Djava.library.path=\"" + Util.getFile(launchArgs.parseString(launchArgs.DJavaLibPathArgument)).getCanonicalPath() + "\"");
            }

            Logger.info("    ClassPathArgument");
            if (launchArgs.UseClassPathArgument) {
                String cpArg = "";//"\"";
                if (launchArgs.GetLibraryClassPaths) {
                    String libCp = getLibraryJarfiles("./packs/" + ModpackName + "/.minecraft/libraries");
                    cpArg += libCp.substring(0, libCp.length() - 1);
                }
                if (launchArgs.GetMinecraftClassPath) {
                    if (cpArg.length() > 0) {
                        cpArg += ";";
                    }
                    cpArg += Util.getFile("./packs/" + ModpackName + "/.minecraft/versions/" + launchArgs.MinecraftVersion + "/" + launchArgs.MinecraftVersion + ".jar").getCanonicalPath();
                }
                if (!launchArgs.AdditionalClassPathEntries.isEmpty()) {
                    if (cpArg.length() > 0) {
                        cpArg += ";";
                    }
                    for (String cpArgEntry : launchArgs.AdditionalClassPathEntries) {
                        cpArg += launchArgs.parseString(cpArgEntry) + ";";
                    }
                    cpArg = cpArg.substring(0, cpArg.length() - 2);
                }
                args.add("-cp");
                args.add("\"" + cpArg + "\"");
            }

            Logger.info("    MainClassArgument");
            if (launchArgs.UseMainClassArgument) {
                args.add(launchArgs.parseString(launchArgs.MainClassArgument));
            }

            Logger.info("    AdditionalCoreArguments");
            if (!launchArgs.AdditionalCoreArguments.isEmpty()) {
                for (String additionalArg : launchArgs.AdditionalCoreArguments) {
                    args.add(launchArgs.parseString(additionalArg));
                }
            }

            //</editor-fold>
            
            //<editor-fold defaultstate="collapsed" desc="Minecraft Arguments">
            
            Logger.info("    UseLegacyUsernameAndSession");
            if (launchArgs.UseLegacyUsernameAndSession) {
                args.add(login.Username);
                args.add(login.AccessToken);
            }

            Logger.info("    UseGameDirArgument");
            if (launchArgs.UseGameDirArgument) {
                args.add("--gameDir");
                args.add(launchArgs.parseString(launchArgs.GameDirArgument));
            }

            Logger.info("    UseAssetDirArgument");
            if (launchArgs.UseAssetDirArgument) {
                args.add("--assetDir");
                args.add(launchArgs.parseString(launchArgs.AssetDirArgument));
            }

            Logger.info("    UseVersionArgument");
            if (launchArgs.UseVersionArgument) {
                args.add("--version");
                args.add(launchArgs.parseString(launchArgs.VersionArgument));
            }

            Logger.info("    UseUsernameArgument");
            if (launchArgs.UseUsernameArgument) {
                args.add("--username");
                args.add(login.Username);
            }

            Logger.info("    UseSessionArgument");
            if (launchArgs.UseSessionArgument) {
                args.add("--session");
                args.add(login.AccessToken);
            }

            Logger.info("    UseUUIDArgument");
            if (launchArgs.UseUUIDArgument) {
                args.add("--uuid");
                args.add(login.UUID);
            }

            Logger.info("    UseAccessTokenArgument");
            if (launchArgs.UseAccessTokenArgument) {
                args.add("--accessToken");
                args.add(login.AccessToken);
            }

            Logger.info("    UseUserPropertiesArgument");
            if (launchArgs.UseUserPropertiesArgument) {
                args.add("--userProperties");
                args.add(login.UserProperties);
            }

            Logger.info("    UseUserTypeArgument");
            if (launchArgs.UseUserTypeArgument) {
                args.add("--userType");
                args.add(login.UserType);
            }

            Logger.info("    UseTweakClassArgument");
            if (launchArgs.UseTweakClassArgument) {
                args.add("--tweakClass");
                args.add(launchArgs.parseString(launchArgs.TweakClassArgument));
            }
            
            Logger.info("    AdditionalMinecraftArguments");
            if (!launchArgs.AdditionalMinecraftArguments.isEmpty()) {
                for (String additionalArg : launchArgs.AdditionalMinecraftArguments) {
                    args.add(launchArgs.parseString(additionalArg));
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
            env.put("APPDATA", launchArgs.AppDataDirectory);

            Logger.info("Launching process!");
            //launcher.redirectOutput(Util.getFile("./stdout.txt"));
            //launcher.redirectError(Util.getFile("./stderr.txt"));
            launcher.directory(Util.getFile("./packs/" + ModpackName + "/.minecraft"));
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
            for (File item : Util.getFile(dir).listFiles()) {
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
