package com.dsdev.moddle;

import java.io.IOException;

/**
 * This class provides the test methods for AssetBuilder.
 *
 * @author Nathan2055
 */
public class AssetBuilderTest {

    /**
     * Builds a copy of the Minecraft assets to a folder called "assets" on the
     * user's desktop.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            String home = System.getProperty("user.home");
            String directory = home + "\\Desktop\\assets\\";
            AssetBuilder.updateAssets(directory);
        } catch (IOException ex) {
            System.err.println("An error was encountered. Printing stack trace...");
            ex.printStackTrace(System.err);
        }
    }

}
