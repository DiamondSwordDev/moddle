package com.dsdev.assets;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.*;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import com.dsdev.moddle.*;

/**
 * This class contains all of the tools required to build and maintain a copy of
 * the Minecraft assets.
 *
 * @author Nathan2055
 */
public class AssetBuilder {

    /**
     * This method builds a fresh copy of the Minecraft assets in the provided
     * directory. If the directory already exists, it is deleted.
     *
     * @param directory A String representing the directory where the assets
     * should be created. MAKE SURE THAT IT ENDS WITH A / CHARCTER OR THIS CODE
     * WILL GO BOOM!
     * @throws java.io.IOException
     */
    public static void buildAssets(String directory) throws IOException {
        Logger.info("Assets", "Building Minecraft assets at " + directory);

        // Let's start by setting up the stuff we're gonna need
        Logger.info("Assets", "Setting up folders...");
        File assetFolder = new File(directory);
        try {
            FileUtils.deleteDirectory(assetFolder);
            Logger.info("Assets", "Deleting existing folder...");
        } catch (IOException e) {
            Logger.info("Assets", "Folder exists. Skipping deletion...");
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
            Logger.info("Assets", "Asset download completed.");
        }

    }

    /**
     * Checks the assetDef.json's SHA-512 against a fresh copy from Mojang's
     * servers to determine if it's up-to-date.
     *
     * @param directory A String representing the directory where the assets
     * should be created. MAKE SURE THAT IT ENDS WITH A / CHARCTER OR THIS CODE
     * WILL GO BOOM!
     * @return A boolean representing whether the assets are up-to-date or not.
     * @throws java.io.IOException
     */
    public static boolean assetsUpdateRequired(String directory) throws IOException {
        Logger.info("Assets Checker", "Fetching asset definition file...");
        File defFileCurrent = File.createTempFile("assets", ".json");
        URL defWeb = new URL("https://s3.amazonaws.com/Minecraft.Download/indexes/legacy.json");
        FileUtils.copyURLToFile(defWeb, defFileCurrent);
        
        File defFileExisting = new File(directory + "assetDef.json");
        if (defFileExisting.exists() && defFileExisting.isFile()) {
            
        }
        
        return false;
    }
}
