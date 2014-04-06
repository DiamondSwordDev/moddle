package com.dsdev.moddle;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.apache.commons.io.IOUtils;

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
            Logger.error("GetURL", ex.getMessage());
            return null;
        }
    }

    public static boolean decompressZipfile(String file, String outputDir) {
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
            return true;
        } catch (Exception ex) {
            Logger.error("UnZip", ex.getMessage());
            return false;
        }
    }

}
