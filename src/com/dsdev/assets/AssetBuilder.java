package com.dsdev.assets;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.*;
import java.net.URL;
import org.apache.commons.io.FileUtils;

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
     * @throws java.io.IOException Of course. :P
     */
    public static void buildAssets(String directory) throws IOException {
        // Let's start by setting up the stuff we're gonna need
        File assetFolder = new File(directory);
        File assetFolderLegacy = new File(directory + "/legacy");
        assetFolderLegacy.mkdirs();

        // Now let's download the version definition file from Mojang and store it in a temporary file
        File defFile = File.createTempFile("assets", ".json");
        URL defWeb = new URL("https://s3.amazonaws.com/Minecraft.Download/indexes/legacy.json");
        FileUtils.copyURLToFile(defWeb, defFile);

        // And now we parse...
        String defString = FileUtils.readFileToString(defFile);
        StringReader defReader = new StringReader(defString);
        JsonReader reader = new JsonReader(defReader);
        try {
            reader.nextBoolean();
            reader.beginObject();
            boolean newFile = true;
            while (newFile) {
                String assetName = reader.nextName();
                reader.beginObject();
                String hash = reader.nextString();
                String subhash = hash.substring(0, 2);
                URL asset = new URL("http://resources.download.minecraft.net/" + subhash + "/" + hash);
                File assetDest = new File(directory + "modern/" + subhash + "/" + hash);
                FileUtils.copyURLToFile(asset, assetDest);
                assetDest = new File(directory + "legacy/" + assetName);
                FileUtils.copyURLToFile(asset, assetDest);
                reader.skipValue();
                JsonToken next = reader.peek();
                newFile = next == JsonToken.NAME;
            }
        } finally {
            reader.close();
        }

    }

    /**
     * This method rechecks the asset files that have been downloaded and
     * updates them if necessary.
     *
     * @param directory
     */
    public void recheckAssets(String directory) {
        //TODO: Write code to recheck the asset files that have already been downloaded
    }

    /**
     * This method will copy a set of downloaded assets to a Minecraft
     * directory.
     *
     * @param downloadedAssets The folder that contains an assets definition
     * downloaded using the buildAssets() method.
     * @param assetsType Either legacy or modern based on what version of
     * Minecraft you are using.
     * @param destFolder The destination folder for the assets.
     */
    public void copyAssets(String downloadedAssets, String assetsType, String destFolder) {
        //TODO: Create method to export the dwonloaded assets
    }
}
