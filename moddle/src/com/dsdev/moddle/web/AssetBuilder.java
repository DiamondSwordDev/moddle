package com.dsdev.moddle.web;

import com.dsdev.moddle.util.Logger;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.*;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * This class contains all of the tools required to build and maintain a copy of
 * the Minecraft assets
 *
 * @author Diamond Sword Development
 */
public class AssetBuilder {

    /**
     * Checks to see if the assets needs updating, and if so, updates them.
     * 
     * @param directory A String representing the directory where the assets
     * should be created.
     * @throws java.io.IOException
     */
    public static void updateAssets(String directory) throws IOException {
        Logger.info("Assets", "Checking for updated assets...");
        if (assetsUpdateRequired(directory)) {
            Logger.info("Assets", "Assets not up-to-date. Redownloading...");
            buildAssets(directory);
        }
        else {
            Logger.info("Assets", "Assets already up-to-date.");
        }
    }
    
    /**
     * This method builds a fresh copy of the Minecraft assets in the provided
     * directory. If the directory already exists, it is deleted.
     *
     * @param directory A String representing the directory where the assets
     * should be created.
     * @throws java.io.IOException
     */
    public static void buildAssets(String directory) throws IOException {
        if (!(directory.endsWith("/")||directory.endsWith("\\"))) {
            Logger.warning("Assets", "A slash was not added at the end of the asset output directory. Attempting to fix...");
            directory = directory + "/";
        }
        
        Logger.info("Assets", "Building Minecraft assets at " + directory);

        // Let's start by setting up the stuff we're gonna need
        Logger.info("Assets", "Setting up folders...");
        File assetFolder = new File(directory);
        try {
            FileUtils.deleteDirectory(assetFolder);
            Logger.info("Assets", "Deleting existing assets...");
        } catch (IOException e) {
            // Folder already exists
        }
        assetFolder.mkdir();
        File assetFolderLegacy = new File(directory + "/legacy");
        assetFolderLegacy.mkdir();

        // Now let's download the version definition file from Mojang and store it in a temporary file
        Logger.info("Assets", "Fetching asset definition file...");
        File defFile = File.createTempFile("assets", ".json");
        URL defWeb = new URL("https://s3.amazonaws.com/Minecraft.Download/indexes/legacy.json");
        FileUtils.copyURLToFile(defWeb, defFile);
        Logger.info("Assets", "Saving asset definition file...");
        File defFileSaved = new File(directory + "assetDef.json");
        FileUtils.copyFile(defFile, defFileSaved);

        // And now we parse...
        Logger.info("Assets", "String asset download...");
        String defString = FileUtils.readFileToString(defFile);
        StringReader defReader = new StringReader(defString);
        JsonReader reader = new JsonReader(defReader);
        try {
            reader.beginObject();
            reader.nextName();
            reader.nextBoolean();
            reader.nextName();
            reader.beginObject();
            boolean newFile = true;
            while (newFile) {
                String assetName = reader.nextName();
                reader.beginObject();
                reader.nextName();
                String hash = reader.nextString();
                String subhash = hash.substring(0, 2);
                Logger.info("Assets", "Downloading " + assetName + "...");
                URL asset = new URL("http://resources.download.minecraft.net/" + subhash + "/" + hash);
                File assetDest = new File(directory + "modern/" + subhash + "/" + hash);
                FileUtils.copyURLToFile(asset, assetDest);
                assetDest = new File(directory + "legacy/" + assetName);
                FileUtils.copyURLToFile(asset, assetDest);
                reader.nextName();
                reader.nextInt();
                reader.endObject();
                JsonToken next = reader.peek();
                newFile = next == JsonToken.NAME;
            }
            reader.endObject();
        } finally {
            reader.close();
            Logger.info("Assets", "Asset download completed");
        }
        
        Logger.info("Assets", "Deleting tempfiles...");
        boolean deleted = defFile.delete();
        if (!deleted) {
            Logger.warning("Assets", "Tempfile " + defFile.getAbsolutePath() + " failed to delete. Scheduling deletion upon termination of Moddle...");
            defFile.deleteOnExit();
        }
    }

    /**
     * Checks the assetDef.json's SHA-512 against a fresh copy from Mojang's
     * servers to determine if it's up-to-date.
     *
     * @param directory A String representing the directory where the assets
     * should be checked.
     * @return A boolean representing whether the assets are up-to-date or not.
     * @throws java.io.IOException
     */
    public static boolean assetsUpdateRequired(String directory) throws IOException {
        if (!(directory.endsWith("/")||directory.endsWith("\\"))) {
            Logger.warning("Assets", "A slash was not added at the end of the asset directory to check for updates. Attempting to fix...");
            directory = directory + "/";
        }
        
        File defFileCurrent = File.createTempFile("assets", ".json");
        URL defWeb = new URL("https://s3.amazonaws.com/Minecraft.Download/indexes/legacy.json");
        FileUtils.copyURLToFile(defWeb, defFileCurrent);
        
        File defFileExisting = new File(directory + "assetDef.json");
        if (defFileExisting.exists() && defFileExisting.isFile()) {
            String defFileExistingString = FileUtils.readFileToString(defFileExisting);
            String defFileCurrentString = FileUtils.readFileToString(defFileCurrent);
            String defFileExistingHash = DigestUtils.md5Hex(defFileExistingString);
            String defFileCurrentHash = DigestUtils.md5Hex(defFileCurrentString);
            if (defFileExistingHash.equals(defFileCurrentHash)) {
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return true;
        }
    }
}
