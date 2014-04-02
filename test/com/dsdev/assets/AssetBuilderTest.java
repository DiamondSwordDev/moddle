package com.dsdev.assets;

import java.io.IOException;

/**
 * This class provides the test methods for AssetBuilder.
 *
 * @author Nathan2055
 */
public class AssetBuilderTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            AssetBuilder.buildAssets("assets");
        } catch (IOException ex) {
            System.err.println("An error was encountered. Printing stack trace...");
            ex.printStackTrace(System.err);
        }
    }

}
