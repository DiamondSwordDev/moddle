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
        
        Logger.info("Writing JSON launch data...");
        try {
            JSONObject container = new JSONObject();
            container.put("settings", LaunchArguments.jsonStruct);
            FileUtils.writeStringToFile(new File("./users/" + PlayerOwner + "/" + ModpackName + "launchargs.json"), container.toJSONString());
        } catch (IOException ex) {
            Logger.error("Modpack.build", "Failed to write launchargs.json!", true, ex.getMessage());
            return;
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
    
    
    
    public boolean run() {
        try {

            Logger.info("Loading launch data...");
            if (LaunchArguments == null) {
                LaunchArguments = new LaunchArgs();
                JSONObject launchConfig = null;
                try {
                    launchConfig = Util.readJSONFile("./users/" + PlayerOwner + "/" + ModpackName + "/launchargs.json");
                } catch (IOException ex) {
                    Logger.error("Modpack.run", "Failed to read launchargs.json!", true, ex.getMessage());
                    return false;
                }
                if (launchConfig != null) {
                    LaunchArguments.loadSettings((JSONArray)launchConfig.get("settings"));
                }
            }
            
            Logger.info("Building process arguments...");
            List<String> args = new ArrayList();

            //<editor-fold defaultstate="collapsed" desc="Java Core Arguments">
            
            Logger.info("    JavaExecutablePath");
            if (LaunchArguments.JavaExecutablePath != null) {
                args.add(LaunchArguments.parseString(LaunchArguments.JavaExecutablePath));
            } else {
                args.add("javaw.exe");
            }

            Logger.info("    XmxArgument");
            if (LaunchArguments.UseXmxArgument) {
                args.add("-Xmx" + Integer.toString(LaunchArguments.XmxArgument) + "M");
            }

            Logger.info("    XmsArgument");
            if (LaunchArguments.UseXmsArgument) {
                args.add("-Xms" + Integer.toString(LaunchArguments.XmsArgument) + "M");
            }

            Logger.info("    DJavaLibPathArgument");
            if (LaunchArguments.UseDJavaLibPathArgument) {
                args.add("-Djava.library.path=\"" + Util.getFile(LaunchArguments.parseString(LaunchArguments.DJavaLibPathArgument)).getCanonicalPath() + "\"");
            }

            Logger.info("    ClassPathArgument");
            if (LaunchArguments.UseClassPathArgument) {
                String cpArg = "";//"\"";
                if (LaunchArguments.GetLibraryClassPaths) {
                    String libCp = getLibraryJarfiles("./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft/libraries");
                    cpArg += libCp.substring(0, libCp.length() - 1);
                }
                if (LaunchArguments.GetMinecraftClassPath) {
                    if (cpArg.length() > 0) {
                        cpArg += ";";
                    }
                    cpArg += new File("./users/" + PlayerOwner + "/" + ModpackName + "/.minecraft/versions/" + LaunchArguments.MinecraftVersion + "/" + LaunchArguments.MinecraftVersion + ".jar").getCanonicalPath();
                }
                if (!LaunchArguments.AdditionalClassPathEntries.isEmpty()) {
                    if (cpArg.length() > 0) {
                        cpArg += ";";
                    }
                    for (String cpArgEntry : LaunchArguments.AdditionalClassPathEntries) {
                        cpArg += LaunchArguments.parseString(cpArgEntry) + ";";
                    }
                    cpArg = cpArg.substring(0, cpArg.length() - 2);
                }
                args.add("-cp");
                args.add("\"" + cpArg + "\"");
            }

            Logger.info("    MainClassArgument");
            if (LaunchArguments.UseMainClassArgument) {
                args.add(LaunchArguments.parseString(LaunchArguments.MainClassArgument));
            }

            Logger.info("    AdditionalCoreArguments");
            if (!LaunchArguments.AdditionalCoreArguments.isEmpty()) {
                for (String additionalArg : LaunchArguments.AdditionalCoreArguments) {
                    args.add(LaunchArguments.parseString(additionalArg));
                }
            }

            //</editor-fold>
            
            //<editor-fold defaultstate="collapsed" desc="Minecraft Arguments">
            
            Logger.info("    UseLegacyUsernameAndSession");
            if (LaunchArguments.UseLegacyUsernameAndSession) {
                args.add(LoginHelper.Username);
                args.add(LoginHelper.AccessToken);
            }

            Logger.info("    UseGameDirArgument");
            if (LaunchArguments.UseGameDirArgument) {
                args.add("--gameDir");
                args.add(LaunchArguments.parseString(LaunchArguments.GameDirArgument));
            }

            Logger.info("    UseAssetDirArgument");
            if (LaunchArguments.UseAssetDirArgument) {
                args.add("--assetDir");
                args.add(LaunchArguments.parseString(LaunchArguments.AssetDirArgument));
            }

            Logger.info("    UseVersionArgument");
            if (LaunchArguments.UseVersionArgument) {
                args.add("--version");
                args.add(LaunchArguments.parseString(LaunchArguments.VersionArgument));
            }

            Logger.info("    UseUsernameArgument");
            if (LaunchArguments.UseUsernameArgument) {
                args.add("--username");
                args.add(LoginHelper.Username);
            }

            Logger.info("    UseSessionArgument");
            if (LaunchArguments.UseSessionArgument) {
                args.add("--session");
                args.add(LoginHelper.AccessToken);
            }

            Logger.info("    UseUUIDArgument");
            if (LaunchArguments.UseUUIDArgument) {
                args.add("--uuid");
                args.add(LoginHelper.UUID);
            }

            Logger.info("    UseAccessTokenArgument");
            if (LaunchArguments.UseAccessTokenArgument) {
                args.add("--accessToken");
                args.add(LoginHelper.AccessToken);
            }

            Logger.info("    UseUserPropertiesArgument");
            if (LaunchArguments.UseUserPropertiesArgument) {
                args.add("--userProperties");
                args.add(LoginHelper.UserProperties);
            }

            Logger.info("    UseUserTypeArgument");
            if (LaunchArguments.UseUserTypeArgument) {
                args.add("--userType");
                args.add(LoginHelper.UserType);
            }

            Logger.info("    UseTweakClassArgument");
            if (LaunchArguments.UseTweakClassArgument) {
                args.add("--tweakClass");
                args.add(LaunchArguments.parseString(LaunchArguments.TweakClassArgument));
            }
            
            Logger.info("    AdditionalMinecraftArguments");
            if (!LaunchArguments.AdditionalMinecraftArguments.isEmpty()) {
                for (String additionalArg : LaunchArguments.AdditionalMinecraftArguments) {
                    args.add(LaunchArguments.parseString(additionalArg));
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
            env.put("APPDATA", LaunchArguments.AppDataDirectory);

            Logger.info("Launching process!");
            //launcher.redirectOutput(Util.getFile("./stdout.txt"));
            //launcher.redirectError(Util.getFile("./stderr.txt"));
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
