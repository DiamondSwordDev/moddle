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
}
