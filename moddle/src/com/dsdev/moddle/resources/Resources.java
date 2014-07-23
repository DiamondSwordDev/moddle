
package com.dsdev.moddle.resources;

import java.io.IOException;
import javax.swing.ImageIcon;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author Diamond Sword Development
 */
public class Resources {
    
    ////////////////////////////////////////////////////////////////////////////
    // This class is mainly a placeholder to allow access to the resource     //
    // images in this package.                                                //
    ////////////////////////////////////////////////////////////////////////////
    
    public static ImageIcon getImageResource(String name) {
        return new ImageIcon(Resources.class.getResource(name));
    }
    
    public static String getTextResource(String name) {
        try {
            return IOUtils.toString(Resources.class.getResourceAsStream(name), "UTF-8");
        } catch (IOException ex) {
            return null;
        }
    }
    
}
