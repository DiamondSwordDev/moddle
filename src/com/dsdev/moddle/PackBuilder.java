package com.dsdev.moddle;

import java.io.File;
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
public class PackBuilder {

    public String ModpackName = "";
    
    public List<String> InstalledEntries = new ArrayList();
    public List<String> ExcludedEntries = new ArrayList();

    public PackBuilder(String name) {
        ModpackName = name;
    }

    
    
    public boolean build(LaunchArgs launchArgs, MinecraftLogin login) {
        try {

            //<editor-fold defaultstate="collapsed" desc="Pack Setup">
            
            Logger.info("Creating pack directory...");
            Util.assertDirectoryExistence("./packs/" + ModpackName);
            
            launchArgs.AppDataDirectory = Util.getFullPath("./packs/" + ModpackName);

            Logger.info("Creating .minecraft directory...");
            Util.assertDirectoryExistence("./packs/" + ModpackName + "/.minecraft");

            //</editor-fold>
            
            Logger.info("Loading pack...");
            if (!Util.decompressZipfile("./packs/" + ModpackName + ".zip", "./tmp/pack/")) {
                return false;
            }
            JSONObject packConfig = Util.readJSONFile("./tmp/pack/pack.json");

            Logger.info("Building skeleton installation...");
            Cache.getCacheEntry("minecraft", (String) packConfig.get("minecraftversion"), "./packs/" + ModpackName + "/.minecraft", launchArgs, this);

            //<editor-fold defaultstate="collapsed" desc="Install Minecraft Jarfile">
            
            Logger.info("Creating '.minecraft/versions/' ...");
            Util.assertDirectoryExistence("./packs/" + ModpackName + "/.minecraft/versions");

            Logger.info("Creating '.minecraft/versions/<version>/' ...");
            Util.assertDirectoryExistence("./packs/" + ModpackName + "/.minecraft/versions/" + packConfig.get("minecraftversion"));

            Logger.info("Obtaining Minecraft jarfile...");
            Util.assertDirectoryExistence("./data/versions");
            if (!Util.getFile("./data/versions/" + packConfig.get("minecraftversion") + ".jar").exists()) {
                Logger.info("Version does not exist.  Downloading...");
                FileUtils.copyURLToFile(Util.getURL("http://s3.amazonaws.com/Minecraft.Download/versions/" + packConfig.get("minecraftversion") + "/" + packConfig.get("minecraftversion") + ".jar"), Util.getFile("./data/versions/" + packConfig.get("minecraftversion") + ".jar"));
            }
            FileUtils.copyFile(Util.getFile("./data/versions/" + packConfig.get("minecraftversion") + ".jar"), Util.getFile("./packs/" + ModpackName + "/.minecraft/versions/" + packConfig.get("minecraftversion") + "/" + packConfig.get("minecraftversion") + ".jar"));

            //</editor-fold>
            
            JSONArray entriesArray = (JSONArray)packConfig.get("entries");
            for (Object obj : entriesArray) {
                JSONObject entryObj = (JSONObject)obj;
                Logger.info("Installing entry " + (String)entryObj.get("name") + "...");
                Cache.getCacheEntry((String)entryObj.get("name"), (String)entryObj.get("version"), "./packs/" + ModpackName + "/.minecraft", launchArgs, this);
            }
            
            return true;

        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            return false;
        }
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
            launcher.redirectOutput(Util.getFile("./stdout.txt"));
            launcher.redirectError(Util.getFile("./stderr.txt"));
            launcher.directory(Util.getFile("./packs/" + ModpackName + "/.minecraft"));
            launcher.start();

            return true;

        } catch (Exception ex) {
            Logger.error("Run", ex.getMessage());
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
            Logger.error("GetLibPaths", ex.getMessage());
            return null;
        }
    }

}
