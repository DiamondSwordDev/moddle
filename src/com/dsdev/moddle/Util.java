package com.dsdev.moddle;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Random utilities for stuff.
 *
 * @author Greenlock28
 */
public class Util {

    public static File getFile(String path) {
        return new File(path);
    }

    public static URL getURL(String uri) {
        try {
            return new URL(uri);
        } catch (Exception ex) {
            Logger.error("Util.getURL", ex.getMessage(), false, ex.getMessage());
            return null;
        }
    }

    public static void assertDirectoryExistence(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    public static String getFullPath(String path) {
        try {
            File f = new File(path);
            return f.getCanonicalPath();
        } catch (Exception ex) {
            Logger.error("Util.getFullPath", ex.getMessage(), false, ex.getMessage());
            return path;
        }
    }
    
    public static JSONObject readJSONFile(String path) {
        try {
            return (JSONObject)JSONValue.parse(FileUtils.readFileToString(new File(path)));
        } catch (Exception ex) {
            Logger.error("Util.readJSONFile", ex.getMessage(), false, ex.getMessage());
            return null;
        }
    }
    
    public static void decompressZipfile(String file, String outputDir) throws Exception {
        try {
            if (!Util.getFile(outputDir).exists()) {
                Util.getFile(outputDir).mkdirs();
            }
            ZipFile zipFile = new ZipFile(file);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                File entryDestination = new File(outputDir, entry.getName());
                if (entry.isDirectory()) {
                    entryDestination.mkdirs();
                } else {
                    //entryDestination.getParentFile().mkdirs();
                    InputStream in = zipFile.getInputStream(entry);
                    OutputStream out = new FileOutputStream(entryDestination);
                    IOUtils.copy(in, out);
                    IOUtils.closeQuietly(in);
                    IOUtils.closeQuietly(out);
                }
            }
        } catch (IOException ex) {
            throw new Exception("Failed to extract zipfile!");
            //Logger.error("Util.decompressZipfile", ex.getMessage(), false, ex.getMessage());
            //return false;
        }
    }
    
    //Method from 'mclauncher-api'
    //Original code by tomsik68
    public static String doJSONPost(String url, JSONObject request) throws Exception {
        URL u = new URL(url);
        String json = request.toJSONString();
        byte[] bytes = json.getBytes();

        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        connection.setRequestProperty("Content-Length", "" + bytes.length);
        connection.setRequestProperty("Content-Language", "en-US");

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
        dos.write(bytes);
        dos.flush();
        dos.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = br.readLine()) != null) {
            result = result.append(line).append('\r');
        }
        br.close();
        connection.disconnect();
        return result.toString();
    }
    
    public static boolean isNumeric(String str)  
    {  
        try  
        {  
            double d = Double.parseDouble(str);  
        }  
        catch(NumberFormatException nfe)  
        {  
            return false;  
        }  
        return true;  
    }

}
