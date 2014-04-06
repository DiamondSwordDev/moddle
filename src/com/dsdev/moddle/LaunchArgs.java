
package com.dsdev.moddle;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author LukeSmalley
 */
public class LaunchArgs {
    
    //General Arguments
    
    public String MinecraftVersion = null;
    
    
    
    //Java Core Arguments
    
    public String JavaExecutablePath = "javaw.exe";
    
    public boolean UseXmxArgument = true;
    public int XmxArgument = 1024;
    
    public boolean UseXmsArgument = false;
    public int XmsArgument = 256;
    
    public boolean UseDJavaLibPathArgument = false;
    public String DJavaLibPathArgument = null;
    
    public boolean UseClassPathArgument = false;
    public boolean GetLibraryClassPaths = false;
    public boolean GetMinecraftClassPath = false;
    public List<String> AdditionalClassPathEntries = new ArrayList();
    
    public boolean UseMainClassArgument = true;
    public String MainClassArgument = "net.minecraft.client.main.Main";
    
    public List<String> AdditionalCoreArguments = new ArrayList();
    
    
    
    //Minecraft Arguments
    
    public boolean UseGameDirArgument = false;
    public String GameDirArgument = null;
    
    public boolean UseAssetDirArgument = false;
    public String AssetDirArgument = null;
    
    public boolean UseAssetIndexArgument = false;
    public String AssetIndexArgument = null;
    
    public boolean UseVersionArgument = false;
    public String VersionArgument = null;
    
    public boolean UseUsernameArgument = false;
    public boolean UseSessionArgument = false;
    public boolean UseUUIDArgument = false;
    public boolean UseAccessTokenArgument = false;
    public boolean UseUserPropertiesArgument = false;
    public boolean UseUserTypeArgument = false;
    
    public boolean UseLegacyUsernameAndSession = false;
    
    public List<String> AdditionalMinecraftArguments = new ArrayList();
    
}
