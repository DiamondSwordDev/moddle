package com.dsdev.moddle;

import java.io.File;
import org.apache.commons.io.FileUtils;

/**
 * The mod pack compiler.
 *
 * @author Greenlock28
 */
public class PackBuilder {

    private String ModpackName = "";

    public PackBuilder(String name) {
        ModpackName = name;
    }

    public void buildPack() {
        try {

            System.out.println("[Build] Creating pack directory...");
            File modpackDir = new File("./packs/" + ModpackName);
            if (!modpackDir.exists()) {
                FileUtils.forceMkdir(modpackDir);
            }

            System.out.println("[Build] Creating .minecraft directory...");
            File dotMinecraft = new File("./packs/" + ModpackName + "/.minecraft");
            if (!dotMinecraft.exists()) {
                FileUtils.forceMkdir(dotMinecraft);
            }

        } catch (Exception ex) {
            System.out.println("[Build][ERROR] " + ex.getMessage());
        }
    }

}
