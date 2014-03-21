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

            Logger.info("Creating pack directory...");
            File modpackDir = new File("./packs/" + ModpackName);
            if (!modpackDir.exists()) {
                FileUtils.forceMkdir(modpackDir);
            }

            Logger.info("Creating .minecraft directory...");
            File dotMinecraft = new File("./packs/" + ModpackName + "/.minecraft");
            if (!dotMinecraft.exists()) {
                FileUtils.forceMkdir(dotMinecraft);
            }
            
            Logger.info("Loading pack config...");

        } catch (Exception ex) {
            Logger.error(ex.getMessage());
        }
    }

}
