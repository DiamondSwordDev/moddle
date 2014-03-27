package com.dsdev.moddle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
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

    
    
    public boolean buildAndRun() {
        if (!build())
            return false;
        if (!run())
            return false;
        return true;
    }
    
    public boolean build() {
        try {

            Logger.info("Creating pack directory...");
            if (!getFile("./packs/" + ModpackName).exists())
                getFile("./packs/" + ModpackName).mkdirs();

            Logger.info("Creating .minecraft directory...");
            if (!getFile("./packs/" + ModpackName + "/.minecraft").exists())
                getFile("./packs/" + ModpackName + "/.minecraft").mkdirs();
            
            Logger.info("Extracting pack archive...");
            if (!decompressZipfile("./packs/" + ModpackName + ".zip", "./tmp/pack/"))
                return false;
            
            Logger.info("Loading pack config...");
            JSONObject packConfig = (JSONObject)JSONValue.parse(FileUtils.readFileToString(getFile("./tmp/pack/pack.json")));
            
            Logger.info("Loading version config...");
            JSONObject versionConfig = (JSONObject)JSONValue.parse(FileUtils.readFileToString(getFile("./data/" + packConfig.get("version") + ".json")));
            
            Logger.info("Creating '.minecraft/versions/' ...");
            if (!getFile("./packs/" + ModpackName + "/.minecraft/versions").exists())
                getFile("./packs/" + ModpackName + "/.minecraft/versions").mkdirs();
            
            Logger.info("Creating '.minecraft/versions/<version>/' ...");
            if (!getFile("./packs/" + ModpackName + "/.minecraft/versions/" + packConfig.get("version")).exists())
                getFile("./packs/" + ModpackName + "/.minecraft/versions/" + packConfig.get("version")).mkdirs();
            
            Logger.info("Obtaining Minecraft jarfile...");
            if (!getFile("./data/versions").exists())
                getFile("./data/versions").mkdirs();
            if (!getFile("./data/versions/" + packConfig.get("version") + ".jar").exists()) {
                Logger.info("Version does not exist.  Downloading...");
                FileUtils.copyURLToFile(getURL("http://s3.amazonaws.com/Minecraft.Download/versions/" + packConfig.get("version") + "/" + packConfig.get("version") + ".jar"), getFile("./data/versions/" + packConfig.get("version") + ".jar"));
            }
            FileUtils.copyFile(getFile("./tmp/versions/" + packConfig.get("version") + ".jar"), getFile("./packs/" + ModpackName + "/.minecraft/versions/" + packConfig.get("version") + "/" + packConfig.get("version") + ".jar"));
            
            Logger.info("Creating '.minecraft/libraries/' ...");
            if (!getFile("./packs/" + ModpackName + "/.minecraft/libraries").exists())
                getFile("./packs/" + ModpackName + "/.minecraft/libraries").mkdirs();
            
            Logger.info("Installing libraries...");
            JSONArray libraryList = (JSONArray)packConfig.get("libraries");
            for (Object obj : libraryList) {
                JSONObject library = (JSONObject)obj;
                Logger.info("Installing library: " + library.get("name"));
                if (!decompressZipfile("./data/libraries/" + library.get("name") + "-" + library.get("version") + ".zip", "./packs/" + ModpackName + "/.minecraft/libraries"))
                    return false;
            }
            
            return true;
            
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            return false;
        }
    }
    
    public boolean run() {
        try {
            
            Logger.info("Preparing to launch...");

            Logger.info("Getting version...");
            JSONObject packConfig = (JSONObject)JSONValue.parse(FileUtils.readFileToString(getFile("./tmp/pack/pack.json")));
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
                    "-Djava.library.path=\"" + getFile("./packs/" + ModpackName + "/.minecraft/versions/" + mcVersion + "/" + mcVersion + "-natives").getCanonicalPath() + "\"",
                    "-cp", "\"" + getFile("/packs/" + ModpackName + "/.minecraft/versions/" + mcVersion + "/" + mcVersion + ".jar").getCanonicalPath() + "\"",//classPathVariable,
                    "net.minecraft.client.main.Main");
                    //"--username", "Player",
                    //"--session", "Null",
                    //"--version", "1.6.4",
                    //"--gameDir", "%APPDATA%/.minecraft",
                    //"--assetsDir", "%APPDATA%/.minecraft/assets");
            
            Logger.info("Setting environment variables...");
            Map<String, String> env = launcher.environment();
            env.put("APPDATA", getFile("./packs/" + ModpackName).getCanonicalPath());
            
            Logger.info("Launching process!");
            launcher.redirectOutput(getFile("./stdout.txt"));
            launcher.redirectError(getFile("./stderr.txt"));
            launcher.directory(getFile("./packs/" + ModpackName + "/.minecraft"));
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
            for (File item : getFile(dir).listFiles()) {
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
    
    
    
    private File getFile(String path) {
        return new File(path);
    }
    
    private URL getURL(String uri) {
        try {
            return new URL(uri);
        } catch (Exception ex) {
            Logger.error("GetURL", ex.getMessage());
            return null;
        }
    }
    
    private boolean decompressZipfile(String file, String outputDir) {
        try {
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(outputDir,  entry.getName());
                entryDestination.getParentFile().mkdirs();
                InputStream in = zipFile.getInputStream(entry);
                OutputStream out = new FileOutputStream(entryDestination);
                IOUtils.copy(in, out);
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(out);
            }
            return true;
        } catch (Exception ex) {
            Logger.error("UnZip", ex.getMessage());
            return false;
        }
    }
    
}
