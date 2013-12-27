import os
import shutil
import platform

import mcore
import vnlib
import cacheutil



def ppath(path):
    if platform.system() == "Windows":
        return path.replace("/", "\\")
    elif platform.system() == "Linux":
        return path.replace("\\", "/")
    else:
        return path.replace("\\", "/")



def build(name, packconfig):
    
    if not os.path.isdir(os.path.join(".", "packs", name)):
        print("[BUILD] Creating pack directory...")
        os.mkdir(os.path.join(".", "packs", name))

    mcore.status_setpartial(name)

    if not os.path.isdir(os.path.join(".", "packs", name, ".minecraft")):
        print("[BUILD] Creating '.minecraft'...")
        os.mkdir(os.path.join(".", "packs", name, ".minecraft"))

    print("[BUILD][MC] Installing Natives..")
    cacheutil.cache_get("__MinecraftNatives", packconfig.getvalue("MinecraftVersion"), os.path.join(".", "packs", name, ".minecraft"), packconfig)
    
    print("[BUILD][MC] Installing Libraries...")
    cacheutil.cache_get("__MinecraftLibraries", packconfig.getvalue("MinecraftVersion"), os.path.join(".", "packs", name, ".minecraft"), packconfig)
    
    print("[BUILD][MC] Installing Assets...")
    cacheutil.cache_get("__MinecraftAssets", packconfig.getvalue("MinecraftVersion"), os.path.join(".", "packs", name, ".minecraft"), packconfig)
    
    print("[BUILD][MC] Installing Moddle Asset Patches...")
    cacheutil.cache_get("__ModdleAssets", "0.0.1", os.path.join(".", "packs", name, ".minecraft"), packconfig)

    print("[BUILD] Installing Forge...")
    cacheutil.cache_get("Forge", packconfig.getvalue("ForgeVersion"), os.path.join(".", "packs", name, ".minecraft"), packconfig)
    
    print("[BUILD] Installing Forge Libraries...")
    cacheutil.cache_get("ForgeLibraries", packconfig.getvalue("ForgeVersion"), os.path.join(".", "packs", name, ".minecraft"), packconfig)
    
    print("[BUILD] Creating launch script...")
    cacheutil.cache_get("__MinecraftLauncher", platform.system() + "." + packconfig.getvalue("MinecraftVersion"), os.path.join(".", "packs", name), packconfig)

    print("[BUILD] Obtaining Minecraft jarfile...")
    if not os.path.isdir(os.path.join(".", "packs", name, ".minecraft", "versions", packconfig.getvalue("MinecraftVersion"))):
        os.makedirs(os.path.join(".", "packs", name, ".minecraft", "versions", packconfig.getvalue("MinecraftVersion")))
    cacheutil.cache_get("__Minecraft", packconfig.getvalue("MinecraftVersion"), os.path.join(".", "packs", name, ".minecraft", "versions", packconfig.getvalue("MinecraftVersion")), packconfig)
        
    '''if not packconfig.getvalue("JarMods") == "None":
        
        print("[BUILD] Extracting Minecraft...")
        if not os.path.isdir(ppath("./packs/" + name + "/.minecraft/mcext/")):
            os.mkdir(ppath("./packs/" + name + "/.minecraft/mcext/"))
        if packconfig.getvalue("MinecraftInstallType") == "Legacy":
            extractfile(ppath("./packs/" + name + "/.minecraft/bin/minecraft.jar"), ppath("./packs/" + name + "/.minecraft/mcext/"))
        elif packconfig.getvalue("MinecraftInstallType") == "MultiVersion":
            extractfile(ppath("./packs/" + name + "/.minecraft/versions/" + packconfig.getvalue("MinecraftVersion") + "/" + packconfig.getvalue("MinecraftVersion") + ".jar"), ppath("./packs/" + name + "/.minecraft/mcext/"))

        print("[BUILD] Removing META-INF...")
        if packconfig.getvalue("MinecraftInstallType") == "Legacy":
            shutil.rmtree(ppath("./packs/" + name + "/.minecraft/mcext/META-INF/"))

        print("[BUILD] Installing JarMods...")
        for jarmod in packconfig.getvalues("JarMods"):
            if not jarmod == "None":
                print("[BUILD][JARMOD] " + jarmod)
                if cache_assert(jarmod.split(",")[0], jarmod.split(",")[1]) == True:
                    cache_extract(jarmod.split(",")[0], jarmod.split(",")[1], ppath("./packs/" + name + "/.minecraft/mcext/"))
                    cache_uniconfig(jarmod.split(",")[0], jarmod.split(",")[1], name, packconfig)

        print("[BUILD] Recompressing Minecraft...")
        if packconfig.getvalue("MinecraftInstallType") == "Legacy":
            compressdir(ppath("./packs/" + name + "/.minecraft/mcext"), ppath("./packs/" + name + "/.minecraft/bin/minecraft.jar"))
        elif packconfig.getvalue("MinecraftInstallType") == "MultiVersion":
            compressdir(ppath("./packs/" + name + "/.minecraft/mcext"), ppath("./packs/" + name + "/.minecraft/versions/" + packconfig.getvalue("MinecraftVersion") + "/" + packconfig.getvalue("MinecraftVersion") + ".jar"))

        print("[BUILD] Cleaning up JarMod residue...")
        shutil.rmtree(ppath("./packs/" + name + "/.minecraft/mcext/"))

    print("[BUILD] Installing CoreMods...")
    if not os.path.isdir(ppath("./packs/" + name + "/.minecraft/coremods/")):
        os.mkdir(ppath("./packs/" + name + "/.minecraft/coremods/"))
    for coremod in packconfig.getvalues("CoreMods"):
        if not coremod == "None":
            print("[BUILD][COREMOD] " + coremod)
            if cache_assert(coremod.split(",")[0], coremod.split(",")[1]) == True:
                cache_copy(coremod.split(",")[0], coremod.split(",")[1], ppath("./packs/" + name + "/.minecraft/coremods/" + coremod.split(",")[0] + cache_getftype(coremod.split(",")[0], coremod.split(",")[1])))
                cache_uniconfig(coremod.split(",")[0], coremod.split(",")[1], name, packconfig)

    print("[BUILD] Installing Mods...")
    if not os.path.isdir(ppath("./packs/" + name + "/.minecraft/mods/")):
        os.mkdir(ppath("./packs/" + name + "/.minecraft/mods/"))
    for mod in packconfig.getvalues("Mods"):
        if not mod == "None":
            print("[BUILD][MOD] " + mod)
            if cache_assert(mod.split(",")[0], mod.split(",")[1]) == True:
                cache_copy(mod.split(",")[0], mod.split(",")[1], ppath("./packs/" + name + "/.minecraft/mods/" + mod.split(",")[0] + cache_getftype(mod.split(",")[0], mod.split(",")[1])))
                cache_uniconfig(mod.split(",")[0], mod.split(",")[1], name, packconfig)

    print("[BUILD] Installing Configs...")
    if not os.path.isdir(ppath("./packs/" + name + "/.minecraft/config/")):
        os.mkdir(ppath("./packs/" + name + "/.minecraft/config/"))
    for config in packconfig.getvalues("Configs"):
        if not config == "None":
            print("[BUILD][CONFIG] " + config)
            if cache_assert(config.split(",")[0], config.split(",")[1]) == True:
                cache_copy(config.split(",")[0], config.split(",")[1], ppath("./packs/" + name + "/.minecraft/config/" + config.split(",")[0] + cache_getftype(config.split(",")[0], config.split(",")[1]))'''
    
    mcore.status_setcomplete(name)


def buildserver(name, packconfig):
    
    if os.path.isdir(ppath("./packs/" + name + "_server/")):
        print("[BUILD] Cleaning pack directory...")
        shutil.rmtree(ppath("./packs/" + name + "_server/"))
        os.mkdir(ppath("./packs/" + name + "_server/"))
    else:
        print("[BUILD] Creating pack directory...")
        os.mkdir(ppath("./packs/" + name + "_server/"))

    status_setpartial(name)

    print("[BUILD] Building server installation...")
    if packconfig.getvalue("MinecraftInstallType") == "MultiVersion":
        print("[BUILD][MC] Installing Forge...")
        cache_extract("ForgeServer", packconfig.getvalue("ForgeVersion"), "./packs/" + name + "_server/")
        cache_uniconfig("Forge", packconfig.getvalue("ForgeVersion"), name + "_server", packconfig)
    elif packconfig.getvalue("MinecraftInstallType") == "Legacy":
        pass
        #print("[BUILD][MC] Installing Natives...")
        #cache_extract("__MinecraftNatives", packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/.minecraft/", verbose)

    print("[BUILD] Creating launch script...")
    #INCLUDES VOLATILE cache_getftype() CODE!!!
    #if cache_assert("__MinecraftLauncher", platform.system() + "." + packconfig.getvalue("MinecraftVersion")) == True:
    cache_copy("__MinecraftLauncher", platform.system() + "." + packconfig.getvalue("MinecraftVersion") + ".server", "./packs/" + name + "_server/start_template" + cache_getftype("__MinecraftLauncher", platform.system() + "." + packconfig.getvalue("MinecraftVersion") + ".server"))

    print("[BUILD] Obtaining Minecraft jarfile...")
    if packconfig.getvalue("MinecraftInstallType") == "Legacy":
        pass
        #cache_copy("__Minecraft", packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/.minecraft/bin/minecraft.jar")
    elif packconfig.getvalue("MinecraftInstallType") == "MultiVersion":
        cache_copy("__MinecraftServer", packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "_server/minecraft_server." + packconfig.getvalue("MinecraftVersion") + ".jar")
        
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

    print("[BUILD] Installing CoreMods...")
    if not os.path.isdir(ppath("./packs/" + name + "_server/coremods/")):
        os.mkdir(ppath("./packs/" + name + "_server/coremods/"))
    for coremod in packconfig.getvalues("CoreMods"):
        if not coremod == "None":
            print("[BUILD][COREMOD] " + coremod)
            if cache_assert(coremod.split(",")[0], coremod.split(",")[1]) == True:
                cache_copy(coremod.split(",")[0], coremod.split(",")[1], ppath("./packs/" + name + "_server/coremods/" + coremod.split(",")[0] + cache_getftype(coremod.split(",")[0], coremod.split(",")[1])))
                cache_uniconfig(coremod.split(",")[0], coremod.split(",")[1], name + "_server", packconfig)
                #if packconfig.getvalue("UniversalConfigType") == "Default":
                #    uniconfig_default(coremod.split(",")[0], coremod.split(",")[1], "./packs/" + name + "_server/config/", verbose)

    print("[BUILD] Installing Mods...")
    if not os.path.isdir(ppath("./packs/" + name + "_server/mods/")):
        os.mkdir(ppath("./packs/" + name + "_server/mods/"))
    for mod in packconfig.getvalues("Mods"):
        if not mod == "None":
            print("[BUILD][MOD] " + mod)
            if cache_assert(mod.split(",")[0], mod.split(",")[1]) == True:
                cache_copy(mod.split(",")[0], mod.split(",")[1], ppath("./packs/" + name + "_server/mods/" + mod.split(",")[0] + cache_getftype(mod.split(",")[0], mod.split(",")[1])))
                cache_uniconfig(mod.split(",")[0], mod.split(",")[1], name + "_server", packconfig)
                #if packconfig.getvalue("UniversalConfigType") == "Default":
                #    uniconfig_default(mod.split(",")[0], mod.split(",")[1], "./packs/" + name + "_server/config/", verbose)

    print("[BUILD] Installing Configs...")
    if not os.path.isdir(ppath("./packs/" + name + "_server/config/")):
        os.mkdir(ppath("./packs/" + name + "_server/config/"))
    for config in packconfig.getvalues("Configs"):
        if not config == "None":
            print("[BUILD][CONFIG] " + config)
            if cache_assert(config.split(",")[0], config.split(",")[1]) == True:
                cache_copy(config.split(",")[0], config.split(",")[1], ppath("./packs/" + name + "_server/config/" + config.split(",")[0] + cache_getftype(config.split(",")[0], config.split(",")[1])))
            
    status_setcomplete(name)
