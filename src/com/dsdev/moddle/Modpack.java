package com.dsdev.moddle;

import java.io.File;
import java.util.Iterator;
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
    
    public Modpack(String name) {
        ModpackName = name;
    }

    
    
    public boolean build(Settings packSettings) {
        try {

            Logger.info("Creating pack directory...");
            if (!Util.getFile("./packs/" + ModpackName).exists())
                Util.getFile("./packs/" + ModpackName).mkdirs();

            Logger.info("Creating .minecraft directory...");
            if (!Util.getFile("./packs/" + ModpackName + "/.minecraft").exists())
                Util.getFile("./packs/" + ModpackName + "/.minecraft").mkdirs();
            
            
            Logger.info("Extracting pack archive...");
            if (!Util.decompressZipfile("./packs/" + ModpackName + ".zip", "./tmp/pack/"))
                return false;
            
            Logger.info("Loading pack config...");
            JSONObject packConfig = (JSONObject)JSONValue.parse(FileUtils.readFileToString(Util.getFile("./tmp/pack/pack.json")));
            
            Logger.info("Building skeleton installation...");
            Cache.getCacheEntry("minecraft", (String)packConfig.get("minecraftversion"), "./packs/" + ModpackName + "/.minecraft");
            
            //Logger.info("Building Forge installation...");
            //Cache.getCacheEntry("minecraftforge", "9.11.1.965", "./packs/" + ModpackName + "/.minecraft");
            
            //Logger.info("Loading version config...");
            //JSONObject versionConfig = (JSONObject)JSONValue.parse(FileUtils.readFileToString(Util.getFile("./data/" + packConfig.get("version") + ".json")));
            
            Logger.info("Creating '.minecraft/versions/' ...");
            if (!Util.getFile("./packs/" + ModpackName + "/.minecraft/versions").exists())
               Util.getFile("./packs/" + ModpackName + "/.minecraft/versions").mkdirs();
            
            Logger.info("Creating '.minecraft/versions/<version>/' ...");
            if (!Util.getFile("./packs/" + ModpackName + "/.minecraft/versions/" + packConfig.get("minecraftversion")).exists())
                Util.getFile("./packs/" + ModpackName + "/.minecraft/versions/" + packConfig.get("minecraftversion")).mkdirs();
            
            Logger.info("Obtaining Minecraft jarfile...");
            if (!Util.getFile("./data/versions").exists())
                Util.getFile("./data/versions").mkdirs();
            if (!Util.getFile("./data/versions/" + packConfig.get("minecraftversion") + ".jar").exists()) {
                Logger.info("Version does not exist.  Downloading...");
                FileUtils.copyURLToFile(Util.getURL("http://s3.amazonaws.com/Minecraft.Download/versions/" + packConfig.get("minecraftversion") + "/" + packConfig.get("minecraftversion") + ".jar"), Util.getFile("./data/versions/" + packConfig.get("minecraftversion") + ".jar"));
            }
            FileUtils.copyFile(Util.getFile("./data/versions/" + packConfig.get("minecraftversion") + ".jar"), Util.getFile("./packs/" + ModpackName + "/.minecraft/versions/" + packConfig.get("minecraftversion") + "/" + packConfig.get("minecraftversion") + ".jar"));
            
            /*Logger.info("Creating '.minecraft/libraries/' ...");
            if (!Util.getFile("./packs/" + ModpackName + "/.minecraft/libraries").exists())
                Util.getFile("./packs/" + ModpackName + "/.minecraft/libraries").mkdirs();
            
            Logger.info("Installing libraries...");
            JSONArray libraryList = (JSONArray)versionConfig.get("libraries");
            for (Iterator it = libraryList.iterator(); it.hasNext();) {
                Object obj = it.next();
                JSONObject library = (JSONObject)obj;
                Logger.info("Installing library: " + library.get("name"));
                if (!Util.decompressZipfile("./data/libraries/" + library.get("name") + "-" + library.get("version") + ".zip", "./packs/" + ModpackName + "/.minecraft/libraries"))
                    return false;
            }*/
            
            return true;
            
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            return false;
        }
    }
    
    public boolean run(Settings packSettings) {
        try {
            
            Logger.info("Getting version...");
            JSONObject packConfig = (JSONObject)JSONValue.parse(FileUtils.readFileToString(Util.getFile("./tmp/pack/pack.json")));
            String mcVersion = (String)packConfig.get("minecraftversion");

            Logger.info("Getting classpath values...");
            String classPathVariable = "\"";
            classPathVariable += getLibraryJarfiles("./packs/" + ModpackName + "/.minecraft/libraries");
            classPathVariable += Util.getFile("./packs/" + ModpackName + "/.minecraft/versions/" + mcVersion + "/" + mcVersion + ".jar").getCanonicalPath() + "\"";
            
            Logger.info("Building process...");
            ProcessBuilder launcher = new ProcessBuilder(
                    "javaw.exe",
                    "-Xmx1024M",
                    "-Djava.library.path=\"" + Util.getFile("./packs/" + ModpackName + "/.minecraft/versions/" + mcVersion + "/" + mcVersion + "-natives").getCanonicalPath() + "\"",
                    "-cp", classPathVariable,
                    "net.minecraft.client.main.Main", //"net.minecraft.launchwrapper.Launch", //"net.minecraft.client.main.Main",
                    "--username", "Player",
                    "--session", "Null",
                    "--version", mcVersion,
                    "--gameDir", "\"" + Util.getFile("./packs/" + ModpackName + "/.minecraft").getCanonicalPath() + "\"",
                    "--assetsDir", "\"" + Util.getFile("./packs/" + ModpackName + "/.minecraft/assets").getCanonicalPath() + "\"");
                    //"--tweakClass", "cpw.mods.fml.common.launcher.FMLTweaker");
            
            Logger.info("Setting environment variables...");
            Map<String, String> env = launcher.environment();
            env.put("APPDATA", Util.getFile("./packs/" + ModpackName).getCanonicalPath());
            
            Logger.info("Launching process!");
            //launcher.redirectOutput(Util.getFile("./stdout.txt"));
            //launcher.redirectError(Util.getFile("./stderr.txt"));
            launcher.directory(Util.getFile("./packs/" + ModpackName + "/.minecraft"));
            launcher.start();
            
            return true;
        
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
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
