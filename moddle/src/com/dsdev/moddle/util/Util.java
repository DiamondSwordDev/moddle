package com.dsdev.moddle.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.rauschig.jarchivelib.ArchiveFormat;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.rauschig.jarchivelib.CompressionType;

/**
 * Random utilities for stuff
 *
 * @author Diamond Sword Development
 */
public class Util {

    public static void createDirectoryIfNeeded(String path) {
        File dir = new File(path);
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
    }

    public static String getFullPath(String path) {
        try {
            File f = new File(path);
            return f.getCanonicalPath();
        } catch (IOException ex) {
            Logger.error("Util.getFullPath", ex.getMessage(), false, ex);
            return path;
        }
    }

    public static JSONObject readJSONFile(String path) throws IOException {
        return (JSONObject) JSONValue.parse(FileUtils.readFileToString(new File(path)));
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
    
    public static void decompressTarGzFile(String tarballFile, String outputDir) throws IOException {
        Archiver ark = ArchiverFactory.createArchiver(ArchiveFormat.TAR, CompressionType.GZIP);
        ark.extract(new File(tarballFile), new File(outputDir));
    }

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static void copyFileAndBackUpOldCopy(File source, File target) throws IOException {
        if (target.isFile()) {
            if (!target.getName().endsWith(".jar")) {
                String[] extensions = target.getName().split(".");
                String extension = "." + extensions[extensions.length-1];
                copyFileAndBackUpOldCopy(target, new File(target.getParentFile(), target.getName().replace(extension, ".old" + extension)));
            }
        }
        FileUtils.copyFile(source, target);
    }
    
    public static boolean versionIsEquivalentOrNewer(String oldVersion, String newVersion) {
        //Get the list of version numbers for the current version
        List<Integer> newVersionBreakout = new ArrayList();
        for (String versionNumber : newVersion.split("\\.")) {
            newVersionBreakout.add(Integer.parseInt(versionNumber));
        }
        
        //Get the list of version numbers for the latest version
        List<Integer> oldVersionBreakout = new ArrayList();
        for (String versionNumber : oldVersion.split("\\.")) {
            oldVersionBreakout.add(Integer.parseInt(versionNumber));
        }
        
        //Compare each version number to determine whether the user is up to date or not
        boolean isUpToDate = true;
        int length = Math.max(newVersionBreakout.size(), oldVersionBreakout.size());
        for (int i = 0; i < length; i++) {
            int newDigit = (i < newVersionBreakout.size()) ? newVersionBreakout.get(i) : 0;
            int oldDigit = (i < oldVersionBreakout.size()) ? oldVersionBreakout.get(i) : 0;
            if (newDigit > oldDigit) {
                isUpToDate = false;
                break;
            }
        }
        
        return isUpToDate;
    }
}
