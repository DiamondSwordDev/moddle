package com.dsdev.moddle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
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
            
            
            Logger.info("Loading version config...");
            JSONObject versionConfig = (JSONObject)JSONValue.parse(FileUtils.readFileToString(Util.getFile("./data/" + packConfig.get("version") + ".json")));
            
            Logger.info("Creating '.minecraft/versions/' ...");
            if (!Util.getFile("./packs/" + ModpackName + "/.minecraft/versions").exists())
                Util.getFile("./packs/" + ModpackName + "/.minecraft/versions").mkdirs();
            
            Logger.info("Creating '.minecraft/versions/<version>/' ...");
            if (!Util.getFile("./packs/" + ModpackName + "/.minecraft/versions/" + packConfig.get("version")).exists())
                Util.getFile("./packs/" + ModpackName + "/.minecraft/versions/" + packConfig.get("version")).mkdirs();
            
            Logger.info("Obtaining Minecraft jarfile...");
            if (!Util.getFile("./data/versions").exists())
                Util.getFile("./data/versions").mkdirs();
            if (!Util.getFile("./data/versions/" + packConfig.get("version") + ".jar").exists()) {
                Logger.info("Version does not exist.  Downloading...");
                FileUtils.copyURLToFile(Util.getURL("http://s3.amazonaws.com/Minecraft.Download/versions/" + packConfig.get("version") + "/" + packConfig.get("version") + ".jar"), Util.getFile("./data/versions/" + packConfig.get("version") + ".jar"));
            }
            FileUtils.copyFile(Util.getFile("./data/versions/" + packConfig.get("version") + ".jar"), Util.getFile("./packs/" + ModpackName + "/.minecraft/versions/" + packConfig.get("version") + "/" + packConfig.get("version") + ".jar"));
            
            Logger.info("Creating '.minecraft/libraries/' ...");
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
            }
            
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
            String mcVersion = (String)packConfig.get("version");

            Logger.info("Getting classpath values...");
            ////String classPathVariable = "\"";
            ////classPathVariable += getLibraryJarfiles("./packs/" + ModpackName + "/.minecraft/libraries");
            ////classPathVariable = classPathVariable.substring(0, classPathVariable.length()-2);
            ////classPathVariable += ";%APPDATA%/.minecraft/versions/" + mcVersion + "/" + mcVersion + ".jar\"";
            //classPathVariable += "\"";
            String classPathVariable = "\"./packs/Testcraft/.minecraft/versions/" + mcVersion + "/" + mcVersion + ".jar\"";
            
            Logger.info("Building process...");
            ProcessBuilder launcher = new ProcessBuilder(
                    "java.exe",
                    "-Xmx1024M",
                    "-Djava.library.path=\"" + Util.getFile("./packs/" + ModpackName + "/.minecraft/versions/" + mcVersion + "/" + mcVersion + "-natives").getCanonicalPath() + "\"",
                    "-cp", "\"" + Util.getFile("/packs/" + ModpackName + "/.minecraft/versions/" + mcVersion + "/" + mcVersion + ".jar").getCanonicalPath() + "\"",//classPathVariable,
                    "net.minecraft.client.main.Main",
                    "--username", "Player",
                    "--session", "Null",
                    "--version", "1.6.4",
                    "--gameDir", "%APPDATA%/.minecraft",
                    "--assetsDir", "%APPDATA%/.minecraft/assets");
            
            Logger.info("Setting environment variables...");
            Map<String, String> env = launcher.environment();
            env.put("APPDATA", Util.getFile("./packs/" + ModpackName).getCanonicalPath());
            
            Logger.info("Launching process!");
            //launcher.redirectOutput(getFile("./stdout.txt"));
            //launcher.redirectError(getFile("./stderr.txt"));
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
