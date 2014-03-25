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
            
            JSONArray libraryList
            
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
        }
    }
    
    
    
    public File getFile(String path) {
        return new File(path);
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
    
    public boolean downloadFile(String webAddress, String targetFile) {
        String x = null;
        OutputStream os = null;
        InputStream is = null;
        ProgressListener progressListener = new ActionListener();
        try {
            os = new FileOutputStream(getFile(targetFile));
            is = (new URL(webAddress)).openStream();

            DownloadCountingOutputStream dcount = new DownloadCountingOutputStream(os);
            dcount.setListener(progressListener);

            // this line give you the total length of source stream as a String.
            // you may want to convert to integer and store this value to
            // calculate percentage of the progression.
            dl.openConnection().getHeaderField("Content-Length");

            // begin transfer by writing to dcount, not os.
            IOUtils.copy(is, dcount);

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (os != null) { 
                os.close(); 
            }
            if (is != null) { 
                is.close(); 
            }
        }
    }
    
}
