
package com.dsdev.moddle;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LukeSmalley
 */
public class LaunchArgs {
    
    //Launch command format:
    //    "{JavaExecutablePath}" -Xmx{MinimumMemory}M -Xms{StartingMemory}M -Djava.library.path="{DJavaLibPath}"
    //    -cp "{ClassPathEntries}" {MainClass} 
    
    public int MinimumMemory = 1024;
    public int StartingMemory = 256;
    
    public String JavaExecutablePath = null;
    
    public String MainClass = null;
    public String TweakClass = null;
    
    public String DJavaLibPath = null;
    public boolean AddCPEntriesFromLibraries = true;
    public List<String> ClassPathEntries = new ArrayList();
    
    public String GameDirectory = null;
    public String AssetDirectory = null;
    public String MinecraftVersion = null;
    
}
