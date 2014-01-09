import os
import shutil
import platform

import mcore
import vnlib
import unicache



def ppath(path):
    if platform.system() == "Windows":
        return path.replace("/", "\\")
    elif platform.system() == "Linux":
        return path.replace("\\", "/")
    else:
        return path.replace("\\", "/")



def build(name, packconfig, clean):

    if not os.path.isdir(os.path.join(".", "packs", name)):
        print("[BUILD] Creating pack directory...")
        os.mkdir(os.path.join(".", "packs", name))
    else:
        if clean == True:
            print("[BUILD] Cleaning pack directory...")
            try:
                shutil.rmtree(os.path.join(".", "packs", name))
            except:
                print("[BUILD][WARNING] Failed to clean pack directory!")
            print("[BUILD] Creating pack directory...")
            os.mkdir(os.path.join(".", "packs", name))

    mcore.status_setpartial(name)

    if not os.path.isdir(os.path.join(".", "packs", name, ".minecraft")):
        print("[BUILD] Creating '.minecraft'...")
        os.mkdir(os.path.join(".", "packs", name, ".minecraft"))

    print("[BUILD][MC] Installing Natives..")
    unicache.cache_get("__MinecraftNatives", packconfig.getvalue("MinecraftVersion"), os.path.join(".", "packs", name, ".minecraft"), packconfig, os.path.join(".", "packs", name, ".minecraft"))
    
    print("[BUILD][MC] Installing Libraries...")
    unicache.cache_get("__MinecraftLibraries", packconfig.getvalue("MinecraftVersion"), os.path.join(".", "packs", name, ".minecraft"), packconfig, os.path.join(".", "packs", name, ".minecraft"))
    
    print("[BUILD][MC] Installing Assets...")
    unicache.cache_get("__MinecraftAssets", packconfig.getvalue("MinecraftVersion"), os.path.join(".", "packs", name, ".minecraft"), packconfig, os.path.join(".", "packs", name, ".minecraft"))
    
    print("[BUILD][MC] Installing Moddle Asset Patches...")
    unicache.cache_get("__ModdleAssets", "0.0.1", os.path.join(".", "packs", name, ".minecraft"), packconfig, os.path.join(".", "packs", name, ".minecraft"))

    print("[BUILD] Installing Forge...")
    unicache.cache_get("Forge", packconfig.getvalue("ForgeVersion"), os.path.join(".", "packs", name, ".minecraft"), packconfig, os.path.join(".", "packs", name, ".minecraft"))
    
    print("[BUILD] Installing Forge Libraries...")
    unicache.cache_get("ForgeLibraries", packconfig.getvalue("ForgeVersion"), os.path.join(".", "packs", name, ".minecraft"), packconfig, os.path.join(".", "packs", name, ".minecraft"))
    
    print("[BUILD] Creating launch script...")
    unicache.cache_get("__MinecraftLauncher", platform.system() + "." + packconfig.getvalue("MinecraftVersion"), os.path.join(".", "packs", name), packconfig, os.path.join(".", "packs", name, ".minecraft"))

    print("[BUILD] Obtaining Minecraft jarfile...")
    if not os.path.isdir(os.path.join(".", "packs", name, ".minecraft", "versions", packconfig.getvalue("MinecraftVersion"))):
        os.makedirs(os.path.join(".", "packs", name, ".minecraft", "versions", packconfig.getvalue("MinecraftVersion")))
    unicache.cache_get("__Minecraft", packconfig.getvalue("MinecraftVersion"), os.path.join(".", "packs", name, ".minecraft", "versions", packconfig.getvalue("MinecraftVersion")), packconfig, os.path.join(".", "packs", name, ".minecraft"))
        
    if not packconfig.getvalue("JarMods") == "None":
        
        print("[BUILD] Extracting Minecraft...")
        if not os.path.isdir(os.path.join(".", "packs", name, ".minecraft", "mcext")):
            os.mkdir(os.path.join(".", "packs", name, ".minecraft", "mcext"))
        mcore.extractzipfile(os.path.join(".", "packs", name, ".minecraft", "versions", packconfig.getvalue("MinecraftVersion"), packconfig.getvalue("MinecraftVersion") + ".jar"), os.path.join(".", "packs", name, ".minecraft", "mcext"))

        print("[BUILD] Installing JarMods...")
        for jarmod in packconfig.getvalues("JarMods"):
            if not jarmod == "None":
                print("[BUILD][JARMOD] " + jarmod)
                unicache.cache_get(jarmod.split(",")[0], jarmod.split(",")[1], os.path.join(".", "packs", name, ".minecraft", "mcext"), packconfig, os.path.join(".", "packs", name, ".minecraft"))

        print("[BUILD] Recompressing Minecraft...")
        mcore.compresszipfile(os.path.join(".", "packs", name, ".minecraft", "mcext"), os.path.join(".", "packs", name, ".minecraft", "versions", packconfig.getvalue("MinecraftVersion"), packconfig.getvalue("MinecraftVersion") + ".jar"))

        print("[BUILD] Cleaning up JarMod residue...")
        shutil.rmtree(os.path.join(".", "packs", name, ".minecraft", "mcext"))

    '''print("[BUILD] Installing CoreMods...")
    if not os.path.isdir(ppath("./packs/" + name + "/.minecraft/coremods/")):
        os.mkdir(ppath("./packs/" + name + "/.minecraft/coremods/"))
    for coremod in packconfig.getvalues("CoreMods"):
        if not coremod == "None":
            print("[BUILD][COREMOD] " + coremod)
            if cache_assert(coremod.split(",")[0], coremod.split(",")[1]) == True:
                cache_copy(coremod.split(",")[0], coremod.split(",")[1], ppath("./packs/" + name + "/.minecraft/coremods/" + coremod.split(",")[0] + cache_getftype(coremod.split(",")[0], coremod.split(",")[1])))
                cache_uniconfig(coremod.split(",")[0], coremod.split(",")[1], name, packconfig)'''

    print("[BUILD] Installing Mods...")
    if not os.path.isdir(os.path.join(".", "packs", name, ".minecraft", "mods")):
        os.mkdir(os.path.join(".", "packs", name, ".minecraft", "mods"))
    for mod in packconfig.getvalues("Mods"):
        if not mod == "None":
            print("[BUILD][MOD] " + mod)
            unicache.cache_get(mod.split(",")[0], mod.split(",")[1], os.path.join(".", "packs", name, ".minecraft", "mods"), packconfig, os.path.join(".", "packs", name, ".minecraft"))

    print("[BUILD] Installing Additional Objects...")
    for adtl in packconfig.getvalues("Additional"):
        if not adtl == "None":
            print("[BUILD][ADTL] " + adtl)
            unicache.cache_get(adtl.split(",")[0], adtl.split(",")[1], os.path.join(".", "packs", name), packconfig, os.path.join(".", "packs", name, ".minecraft"))
    
    mcore.status_setcomplete(name)


def buildserver(name, packconfig, clean):
    
    if not os.path.isdir(os.path.join(".", "packs", name)):
        print("[BUILD] Creating pack directory...")
        os.makedirs(os.path.join(".", "packs", name + "_server"))
    else:
        if clean == True:
            print("[BUILD] Cleaning pack directory...")
            try:
                shutil.rmtree(os.path.join(".", "packs", name + "_server"))
            except:
                print("[BUILD][WARNING] Failed to clean pack directory!")
            print("[BUILD] Creating pack directory...")
            os.makedirs(os.path.join(".", "packs", name + "_server"))

    mcore.status_setpartial(name)

    print("[BUILD] Building server installation...")
    print("[BUILD] Installing Forge...")
    unicache.cache_get("ForgeServer", packconfig.getvalue("ForgeVersion"), os.path.join(".", "packs", name + "_server"), packconfig, os.path.join(".", "packs", name + "_server"))

    print("[BUILD] Creating launch script...")
    unicache.cache_get("__MinecraftLauncher", platform.system() + "." + packconfig.getvalue("MinecraftVersion") + ".server", os.path.join(".", "packs", name + "_server"), packconfig, os.path.join(".", "packs", name + "_server"))

    print("[BUILD] Obtaining Minecraft jarfile...")
    unicache.cache_get("__MinecraftServer", packconfig.getvalue("MinecraftVersion"), os.path.join(".", "packs", name + "_server"), packconfig, os.path.join(".", "packs", name + "_server"))
        
    '''if not packconfig.getvalue("JarMods") == "None":
        
        print("[BUILD] Extracting Minecraft...")
        if not os.path.isdir(ppath("./packs/" + name + "/.minecraft/mcext/")):
            os.mkdir(ppath("./packs/" + name + "/.minecraft/mcext/"))
        if packconfig.getvalue("MinecraftInstallType") == "Legacy":
            extractfile(ppath("./packs/" + name + "/.minecraft/bin/minecraft.jar"), ppath("./packs/" + name + "/.minecraft/mcext/"), verbose)
        elif packconfig.getvalue("MinecraftInstallType") == "MultiVersion":
            extractfile(ppath("./packs/" + name + "/.minecraft/versions/" + packconfig.getvalue("MinecraftVersion") + "/" + packconfig.getvalue("MinecraftVersion") + ".jar"), ppath("./packs/" + name + "/.minecraft/mcext/"), verbose)

        print("[BUILD] Removing META-INF...")
        if packconfig.getvalue("MinecraftInstallType") == "Legacy":
            shutil.rmtree(ppath("./packs/" + name + "/.minecraft/mcext/META-INF/"))

        print("[BUILD] Installing JarMods...")
        for jarmod in packconfig.getvalues("JarMods"):
            if not jarmod == "None":
                print("[BUILD][JARMOD] " + jarmod)
                if cache_assert(jarmod.split(",")[0], jarmod.split(",")[1]) == True:
                    cache_extract(jarmod.split(",")[0], jarmod.split(",")[1], ppath("./packs/" + name + "/.minecraft/mcext/"), verbose)
                    if packconfig.getvalue("UniversalConfigType") == "Default":
                        uniconfig_default(jarmod.split(",")[0], jarmod.split(",")[1], "./packs/" + name + "/.minecraft/config/", verbose)

        print("[BUILD] Recompressing Minecraft...")
        if packconfig.getvalue("MinecraftInstallType") == "Legacy":
            compressdir(ppath("./packs/" + name + "/.minecraft/mcext"), ppath("./packs/" + name + "/.minecraft/bin/minecraft.jar"), verbose)
        elif packconfig.getvalue("MinecraftInstallType") == "MultiVersion":
            compressdir(ppath("./packs/" + name + "/.minecraft/mcext"), ppath("./packs/" + name + "/.minecraft/versions/" + packconfig.getvalue("MinecraftVersion") + "/" + packconfig.getvalue("MinecraftVersion") + ".jar"), verbose)

        print("[BUILD] Cleaning up JarMod residue...")
        shutil.rmtree(ppath("./packs/" + name + "/.minecraft/mcext/"))'''

    '''print("[BUILD] Installing CoreMods...")
    if not os.path.isdir(ppath("./packs/" + name + "_server/coremods/")):
        os.mkdir(ppath("./packs/" + name + "_server/coremods/"))
    for coremod in packconfig.getvalues("CoreMods"):
        if not coremod == "None":
            print("[BUILD][COREMOD] " + coremod)
            if cache_assert(coremod.split(",")[0], coremod.split(",")[1]) == True:
                cache_copy(coremod.split(",")[0], coremod.split(",")[1], ppath("./packs/" + name + "_server/coremods/" + coremod.split(",")[0] + cache_getftype(coremod.split(",")[0], coremod.split(",")[1])))
                cache_uniconfig(coremod.split(",")[0], coremod.split(",")[1], name + "_server", packconfig)
                #if packconfig.getvalue("UniversalConfigType") == "Default":
                #    uniconfig_default(coremod.split(",")[0], coremod.split(",")[1], "./packs/" + name + "_server/config/", verbose)'''

    print("[BUILD] Installing Mods...")
    if not os.path.isdir(os.path.join(".", "packs", name + "_server", "mods")):
        os.makedirs(os.path.join(".", "packs", name + "_server", "mods"))
    for mod in packconfig.getvalues("Mods"):
        if not mod == "None":
            print("[BUILD][MOD] " + mod)
            unicache.cache_get(mod.split(",")[0], mod.split(",")[1], os.path.join(".", "packs", name + "_server", "mods"), packconfig, os.path.join(".", "packs", name + "_server"))

    '''print("[BUILD] Installing Configs...")
    if not os.path.isdir(ppath("./packs/" + name + "_server/config/")):
        os.mkdir(ppath("./packs/" + name + "_server/config/"))
    for config in packconfig.getvalues("Configs"):
        if not config == "None":
            print("[BUILD][CONFIG] " + config)
            if cache_assert(config.split(",")[0], config.split(",")[1]) == True:
                cache_copy(config.split(",")[0], config.split(",")[1], ppath("./packs/" + name + "_server/config/" + config.split(",")[0] + cache_getftype(config.split(",")[0], config.split(",")[1])))'''
            
    mcore.status_setcomplete(name)
