package com.dsdev.assets;

import com.google.gson.*;
import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class contains all of the tools required to build and maintain a copy of
 * the Minecraft assets.
 *
 * @author Nathan2055
 */
public class AssetBuilder {

    /**
     * A File object representing the assets definition file from Mojang.
     */
    private File defFile;

    /**
     * This method builds a fresh copy of the Minecraft assets in the provided
     * directory. If the directory already exists, it is deleted.
     *
     * @param directory A String representing the directory where the assets
     * should be created.
     * @throws java.io.IOException Of course. :P
     */
    public void buildAssets(String directory) throws IOException {
        // Let's start by setting up the folders we are gonna need.
        File buffer = new File(directory + "/legacy");
        buffer.mkdirs();

        // Now let's fetch the version definition file from Mojang.
        defFile = File.createTempFile("assets", ".json");
        URL defWeb = new URL("https://s3.amazonaws.com/Minecraft.Download/indexes/legacy.json");
        ReadableByteChannel rbc = Channels.newChannel(defWeb.openStream());
        FileOutputStream fos = new FileOutputStream(defFile);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

        // And now we parse...
        String defPathString = defFile.getAbsolutePath();
        byte[] encoded = Files.readAllBytes(Paths.get(defPathString));
        String defString = Charset.defaultCharset().decode(ByteBuffer.wrap(encoded)).toString();
        BufferedReader br = new BufferedReader(new FileReader(defFile));
        Gson gson = new GsonBuilder().create();
        //DataObject obj = gson.fromJson(br, DataObject);
    }
}
