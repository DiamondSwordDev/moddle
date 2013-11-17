import mlaunch

class META:
    NAME = "Greenlock's Mod"
    DESCRIPTION = "A collection of tech mods, mostly Tekkit flavoured."
    AUTHOR = "Greenlock28"

def build():
    print("[BUILD] Creating modpack.")
    pack = mlaunch.ForgeModpack("greenmod")
    print("[BUILD] Initializing install.")
    pack.install_init("1_5_2")
    print("[BUILD] Installing forge libraries.")
    pack.get_forge_lib()
    print("[BUILD][JARMOD] Installing Forge.")
    pack.get_jarmod("minecraftforge-universal-1.5.2-7.8.1.738.zip", "http://files.minecraftforge.net/minecraftforge/minecraftforge-universal-1.5.2-7.8.1.738.zip")
    
    
    print("[BUILD][MOD] Installing Buildcraft")
    pack.get_mod("buildcraft-A-3.7.1.jar", "http://bit.ly/16o6GtL")
    
    print("[BUILD][COREMOD] Installing COFH Core")
    pack.get_coremod("CoFHCore-1.5.2.5.jar", "https://dl.dropboxusercontent.com/u/57416963/Minecraft/Mods/CoFHCore/release/CoFHCore-1.5.2.5.jar")
    
    print("[BUILD][MOD] Installing Thermal Expansion")
    pack.get_mod("ThermalExpansion-2.4.6.0.jar", "https://dl.dropboxusercontent.com/u/57416963/Minecraft/Mods/ThermalExpansion/release/ThermalExpansion-2.4.6.0.jar")
    
    
    print("[BUILD] Finishing installation.")
    pack.install_finish()
