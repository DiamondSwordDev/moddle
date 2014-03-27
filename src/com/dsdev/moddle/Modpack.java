package com.dsdev.moddle;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import sun.net.ProgressListener;

/**
 * The mod pack compiler.
 *
 * @author Greenlock28
 */
public class PackBuilder {

    private String ModpackName = "";

    public PackBuilder(String name) {
        ModpackName = name;
    }

    
    
    public void buildPack() {
        try {

            Logger.info("Creating pack directory...");
            if (!getFile("./packs/" + ModpackName).exists())
                getFile("./packs/" + ModpackName).mkdirs();

            Logger.info("Creating .minecraft directory...");
            if (!getFile("./packs/" + ModpackName + "/.minecraft").exists())
                getFile("./packs/" + ModpackName + "/.minecraft").mkdirs();
            
            Logger.info("Extracting pack archive...");
            if (!decompressZipfile("./packs/" + ModpackName + ".zip", "./tmp/pack/"))
                return;
            
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
            if (!getFile("./tmp/versions").exists())
                getFile("./tmp/versions").mkdirs();
            if (!getFile("./tmp/versions/" + packConfig.get("version") + ".jar").exists()) {
                Logger.info("Version does not exist.  Downloading...");
                FileUtils.copyURLToFile(getURL("http://s3.amazonaws.com/Minecraft.Download/versions/" + packConfig.get("version") + "/" + packConfig.get("version") + ".jar"), getFile("./tmp/versions/" + packConfig.get("version") + ".jar"));
            }
            FileUtils.copyFile(getFile("./tmp/versions/" + packConfig.get("version") + ".jar"), getFile("./packs/" + ModpackName + "/.minecraft/versions/" + packConfig.get("version") + "/" + packConfig.get("version") + ".jar"));
            
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
        }
    }
    
    
    
    public File getFile(String path) {
        return new File(path);
    }
    
    public URL getURL(String uri) {
        try {
            return new URL(uri);
        } catch (Exception ex) {
            Logger.error("GetURL", ex.getMessage());
            return null;
        }
    }
    
    public boolean decompressZipfile(String file, String outputDir) {
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
