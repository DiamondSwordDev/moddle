package com.dsdev.moddle;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Random utilities for stuff.
 *
 * @author Greenlock28
 */
public class Util {

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
    
    public static JSONObject readJSONFile(String path) throws IOException {
        return (JSONObject)JSONValue.parse(FileUtils.readFileToString(new File(path)));
    }
    
    /*public static void decompressZipfile(String file, String outputDir) throws IOException {
        if (!new File(outputDir).exists()) {
            new File(outputDir).mkdirs();
        }
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            File entryDestination = new File(outputDir, entry.getName().replace("aux.class", "_aux.class"));
            if (entry.isDirectory()) {
                entryDestination.mkdirs();
            } else {
                entryDestination.getParentFile().mkdirs();
                try {
                    if (entryDestination.exists()) {
                        entryDestination.delete();
                    }
                } catch (Exception ex) { }
                InputStream in = zipFile.getInputStream(entry);
                OutputStream out = new FileOutputStream(entryDestination);
                IOUtils.copy(in, out);
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(out);
            }
        }
    }
    
    public static void compressZipfile(String sourceDir, String outputFile) throws IOException, FileNotFoundException {
        if (new File(outputFile).exists()) {
            new File(outputFile).delete();
        }
        ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(outputFile));
        compressDirectoryToZipfile(sourceDir, sourceDir, zipFile);
        zipFile.close();
    }
    
    private static void compressDirectoryToZipfile(String rootDir, String sourceDir, ZipOutputStream out) throws IOException, FileNotFoundException {
        for (File file : new File(sourceDir).listFiles()) {
            if (file.isDirectory()) {
                compressDirectoryToZipfile(rootDir, sourceDir + file.getName() + File.separator, out);
            } else {
                ZipEntry entry = new ZipEntry(sourceDir.replace(rootDir, "") + file.getName().replace("_aux.class", "aux.class"));
                out.putNextEntry(entry);

                FileInputStream in = new FileInputStream(sourceDir + file.getName());
                IOUtils.copy(in, out);
                out.closeEntry();
                in.close();
            }
        }
    }*/
    
    public static void decompressZipfile(String zipFile, String outputDir) throws ZipException {
        ZipFile zip = new ZipFile(zipFile);
        zip.extractAll(outputDir);
    }
    
    public static void compressZipfile(String sourceDir, String outputFile) throws ZipException {
        ZipFile zip = new ZipFile(outputFile);
        ZipParameters params = new ZipParameters();
        params.setCompressionMethod(Zip4jConstants.COMP_STORE);
		//params.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
        for (File f : new File(sourceDir).listFiles()) {
            if (f.isDirectory()) {
                zip.addFolder(f, params);
            } else {
                zip.addFile(f, params);
            }
        }
    }
    
    public static void removeFileFromZipfile(String zipFile, String fileName) throws ZipException {
        ZipFile zip = new ZipFile(zipFile);
        zip.removeFile(fileName);
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
    
    public static boolean isNumeric(String str) {  
        try {  
            double d = Double.parseDouble(str);  
        } catch(NumberFormatException nfe) {  
            return false;  
        }  
        return true;  
    }

}
