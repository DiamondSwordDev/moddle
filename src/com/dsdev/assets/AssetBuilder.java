package com.dsdev.assets;

import com.google.gson.stream.JsonReader;
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
     * should be created.
     * @throws java.io.IOException Of course. :P
     */
    public void buildAssets(String directory) throws IOException {
        // Let's start by setting up the folders we are gonna need
        File buffer = new File(directory + "/legacy");
        buffer.mkdirs();

        // Now let's download the version definition file from Mojang and store it in a temporary file
        File defFile = File.createTempFile("assets", ".json");
        URL defWeb = new URL("https://s3.amazonaws.com/Minecraft.Download/indexes/legacy.json");
        FileUtils.copyURLToFile(defWeb, defFile);

        // And now we parse...
        String defString = FileUtils.readFileToString(defFile);
        //JSON parsing code goes here
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
