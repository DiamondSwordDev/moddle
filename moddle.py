import urllib.request
import subprocess
import platform
import zipfile
import tarfile
import shutil
import string
import time
import imp
import sys
import re
import os



class VictoryNotationValue:
    
    name = ""
    values = []

    def __init__(self):
        self.values = []
        self.name = ""



class VictoryNotationFile:

    values = []

    def __init__(self, filename):
        self.values = []
        with open(filename, "r") as f:
            currentVal = None
            for line in f.readlines():
                l = line.replace("\n", "")
                if l[:1] == "*":
                    pass
                elif l[:1] == "-":
                    if not currentVal == None:
                        currentVal.values.append(l[1:])
                elif l.find("=") > -1:
                    if not currentVal == None:
                        self.values.append(currentVal)
                    v = VictoryNotationValue()
                    v.name = l.split("=")[0]
                    v.values.append(l.split("=")[1])
                    self.values.append(v)
                    #v.values = l[len(l.split("=")[0]):]
                elif l[-1:] == ":":
                    if not currentVal == None:
                        self.values.append(currentVal)
                    v = VictoryNotationValue()
                    v.name = l[:-1]
                    currentVal = v
                elif l == "":
                    pass
                else:
                    pass
                
            if not currentVal == None:
                self.values.append(currentVal)

    def getvalue(self, name):
        for m in self.values:
            if m.name == name:
                if not m.values == []:
                    return m.values[0]
                else:
                    return None

    def getvalues(self, name):
        for m in self.values:
            if m.name == name:
                if not m.values == []:
                    return m.values
                else:
                    return None

    def getnodes(self):
        nodelist = []
        for m in self.values:
            nodelist.append(m.name)
        return nodelist



def ppath(path):
    if platform.system() == "Windows":
        return path.replace("/", "\\")
    elif platform.system() == "Linux":
        return path.replace("\\", "/")
    else:
        return path.replace("\\", "/")

def printlog(msg):
    with open(ppath("./log-last-run.txt"), "a") as f:
        f.write("\n" + msg)
    print(msg)

def getshellformat():
    if platform.system() == "Windows":
        return ".bat"
    elif platform.system() == "Linux":
        return ".sh"
    else:
        return ".sh"

def extractfile(filename, target, verbose):
    with zipfile.ZipFile(filename, 'r') as z:
        for n in z.namelist():
            if verbose == True:
                print("[EXTRACT] " + n)
            if n.endswith("/"):
                try:
                    os.makedirs(target + ppath(n))
                except:
                    pass
                continue
            if not os.path.isdir(os.path.dirname(target + ppath(n))):
                os.makedirs(os.path.dirname(target + ppath(n)))
            #z.extract(n, os.path.dirname(target + ppath(n)))
            with open(target + ppath(n).replace("aux.class", "_aux.class"), "wb") as f:
                f.write(z.read(n))

def compress__directory(startingdir, directory, z, verbose):
    for root, directories, files in os.walk(directory):
        for file in files:
            if verbose == True:
                print("[COMPRESS][FILE] " + file)
            fpath = root + ppath("/") + file
            z.write(fpath, fpath.replace(startingdir, "").replace("_aux.class", "aux.class"))
        for sub in directories:
            if verbose == True:
                print("[COMPRESS][DIR] " + sub)
            compress__directory(startingdir, os.path.join(root + sub), z, verbose)

def compress__directory__old(rootdir, directory, z, verbose):
    for root, directorys, files in os.walk(directory):
        for file in files:
            if verbose == True:
                print("[COMPRESS][FILE] " + file)
            z.write(os.path.join(directory + file), directory.replace(rootdir, "") + file.replace("_aux.class", "aux.class"))
        for direx in dirs:
            if verbose == True:
                print("[COMPRESS][DIR] " + direx)
            compress__directory(rootdir, direx, z, verbose)

def compressdir(directory, target, verbose):
    z = zipfile.ZipFile(target, 'w')
    compress__directory(directory, directory, z, verbose)
    z.close()
    
def downloadfile(url, target):
    d = urllib.request.urlopen(url)
    with open(target, 'b+w') as f:
        f.write(d.read())

def cache_getftype(name, version):
    if os.path.isfile(ppath("./ext/cache/" + name + "/" + version + "/filetype.vn")):
        filetype = VictoryNotationFile(ppath("./ext/cache/" + name + "/" + version + "/filetype.vn"))
        return filetype.getvalue("FileType")
    elif os.path.isfile(ppath("./cache/" + name + "/" + version + "/filetype.vn")):
        filetype = VictoryNotationFile(ppath("./cache/" + name + "/" + version + "/filetype.vn"))
        return filetype.getvalue("FileType")
    else:
        return ".zip"

def cache__getpath(name, version):
    if name == "Minecraft":
        if os.path.isdir(ppath("./cache/Minecraft/" + version + "/")):
            return "./cache/" + name + "/" + version + "/"
        else:
            print("[BUILD] Minecraft version not found.  Downloading to cache...")
            os.mkdir(ppath("./cache/Minecraft/" + version + "/"))
            downloadfile("http://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".jar", ppath("./cache/Minecraft/" + version + "/Minecraft.jar"))
            with open(ppath("./data/minecraft/Minecraft/" + version + "/filetype.vn"), "w") as f:
                f.write("FileType=.jar")
            return "./cache/" + name + "/" + version + "/"
    else:
        if os.path.isdir(ppath("./data/minecraft/" + name + "/" + version + "/")):
            return "./data/minecraft/" + name + "/" + version + "/"
        elif os.path.isdir(ppath("./ext/cache/" + name + "/" + version + "/")):
            return ppath("./ext/cache/" + name + "/" + version + "/")
        elif os.path.isdir(ppath("./cache/" + name + "/" + version + "/")):
            return ppath("./cache/" + name + "/" + version + "/")
        else:
            for root, dirs, files in os.walk(ppath("./cache/")):
                for file in files:
                    if os.path.isfile(ppath("./cache/" + file)):
                        modlistfile = VictoryNotationFile(ppath("./cache/" + file))
                        for node in modlistfile.getnodes():
                            if node == name:
                                for v in modlistfile.getvalues(node):
                                    if v.split(",")[0] == version:
                                        os.makedirs(ppath("./cache/" + name + "/" + version + "/"))
                                        downloadfile(v.split(",")[2], ppath("./cache/" + name + "/" + version + "/" + name + v.split(",")[1]))
                                        with open(ppath("./cache/" + name + "/" + version + "/filetype.vn"), "w") as f:
                                            f.write("FileType=" + v.split(",")[1])
                                        return ppath("./cache/" + name + "/" + version + "/")
            return None

def cache_assert(name, version):
    if os.path.isfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version))):
        return True
    else:
        installed = False
        for root, dirs, files in os.walk(ppath("./cache/")):
            for file in files:
                if os.path.isfile(ppath("./cache/" + file)): # and file.endswith(".vn"): WHY THIS NO WORK??
                    modlistfile = VictoryNotationFile(ppath("./cache/" + file))
                    for node in modlistfile.getnodes():
                        if node == name:
                            for v in modlistfile.getvalues(node):
                                if v.split(",")[0] == version:
                                    os.makedirs(ppath("./cache/" + name + "/" + version + "/"))
                                    downloadfile(v.split(",")[2], ppath("./cache/" + name + "/" + version + "/" + name + v.split(",")[1]))
                                    with open(ppath("./cache/" + name + "/" + version + "/filetype.vn"), "w") as f:
                                        f.write("FileType=" + v.split(",")[1])
                                    installed = True
        return installed


def cache_copy(name, version, target):
    if name == "__Minecraft":
        if os.path.isdir(ppath("./cache/__Minecraft/" + version + "/")):
            shutil.copyfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target))
        else:
            print("[BUILD] Minecraft version not found.  Downloading to cache...")
            os.makedirs(ppath("./cache/__Minecraft/" + version + "/"))
            downloadfile("http://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".jar", ppath("./cache/__Minecraft/" + version + "/__Minecraft.jar"))
            with open(ppath("./cache/__Minecraft/" + version + "/filetype.vn"), "w") as f:
                f.write("FileType=.jar")
            shutil.copyfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target))
    else:
        if os.path.isdir(ppath("./ext/cache/" + name + "/" + version + "/")):
            shutil.copyfile(ppath("./ext/cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target))
        elif os.path.isdir(ppath("./cache/" + name + "/" + version + "/")):
            shutil.copyfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target))
        else:
            if cache_assert(name, version) == True:
                shutil.copyfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target))
            else:
                print("[BUILD][WARNING] " + name + " " + version + " was not found and was unable to be installed.")


def cache_extract(name, version, target, verbose):
    if name == "__Minecraft":
        if os.path.isdir(ppath("./cache/__Minecraft/" + version + "/")):
            extractfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target), verbose)
        else:
            print("[BUILD] Minecraft version not found.  Downloading to cache...")
            os.makedirs(ppath("./cache/__Minecraft/" + version + "/"))
            downloadfile("http://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".jar", ppath("./cache/__Minecraft/" + version + "/__Minecraft.jar"))
            with open(ppath("./cache/__Minecraft/" + version + "/filetype.vn"), "w") as f:
                f.write("FileType=.jar")
            extractfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target), verbose)
    else:
        if os.path.isdir(ppath("./ext/cache/" + name + "/" + version + "/")):
            extractfile(ppath("./ext/cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target), verbose)
        elif os.path.isdir(ppath("./cache/" + name + "/" + version + "/")):
            extractfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target), verbose)
        else:
            if cache_assert(name, version) == True:
                extractfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target), verbose)
            else:
                print("[BUILD][WARNING] " + name + " " + version + " was not found and was unable to be installed.")


def uniconfig_default(name, version, target, verbose):
    if not os.path.isdir(ppath(target)):
        os.makedirs(ppath(target))
    if os.path.isdir(ppath("./cache/__UniversalConfigDefault/" + name + "/" + version + "/")):
        extractfile(ppath("./cache/__UniversalConfigDefault/" + name + "/" + version + "/" + name + ".zip"), ppath(target), verbose)
    

def status_clean(name):
    if os.path.exists(ppath("./packs/" + name + "/inst_partial")):
        os.remove(ppath("./packs/" + name + "/inst_partial"))
    if os.path.exists(ppath("./packs/" + name + "/inst_complete")):
        os.remove(ppath("./packs/" + name + "/inst_complete"))

def status_setpartial(name):
    status_clean(name)
    with open(ppath("./packs/" + name + "/inst_partial"), "w") as f:
        f.write("0")
        f.close()

def status_setcomplete(name):
    status_clean(name)
    with open(ppath("./packs/" + name + "/inst_complete"), "w") as f:
        f.write("0")
        f.close()

def build_forge(name, packconfig, verbose):
    
    if os.path.isdir(ppath("./packs/" + name + "/")):
        print("[BUILD] Cleaning pack directory...")
        shutil.rmtree(ppath("./packs/" + name + "/"))
        os.mkdir(ppath("./packs/" + name + "/"))
    else:
        print("[BUILD] Creating pack directory...")
        os.mkdir(ppath("./packs/" + name + "/"))

    status_setpartial(name)

    if os.path.isdir(ppath("./packs/" + name + "/.minecraft/")):
        print("[BUILD] Cleaning '.minecraft'...")
        shutil.rmtree(ppath("./packs/" + name + "/.minecraft/"))
        os.mkdir(ppath("./packs/" + name + "/.minecraft/"))
    else:
        print("[BUILD] Creating '.minecraft'...")
        os.mkdir(ppath("./packs/" + name + "/.minecraft/"))
        
    print("[BUILD] Building Minecraft installation...")
    if packconfig.getvalue("MinecraftInstallType") == "MultiVersion":
        print("[BUILD][MC] Installing Natives...")
        cache_extract("__MinecraftNatives", packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/.minecraft/", verbose) #extractfile(getcachepath("MinecraftNatives", packconfig.getvalue("MinecraftVersion")) + "MinecraftNatives.zip", ppath("./packs/" + name + "/.minecraft/"), verbose)
        print("[BUILD][MC] Installing Libraries...")
        cache_extract("__MinecraftLibraries", packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/.minecraft/", verbose) #extractfile(getcachepath("MinecraftLibraries", packconfig.getvalue("MinecraftVersion")) + "MinecraftLibraries.zip", ppath("./packs/" + name + "/.minecraft/"), verbose)
        print("[BUILD][MC] Installing Assets...")
        cache_extract("__MinecraftAssets", packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/.minecraft/", verbose) #extractfile(getcachepath("MinecraftAssets", packconfig.getvalue("MinecraftVersion")) + "MinecraftAssets.zip", ppath("./packs/" + name + "/.minecraft/"), verbose)
    elif packconfig.getvalue("MinecraftInstallType") == "Legacy":
        print("[BUILD][MC] Installing Natives...")
        cache_extract("__MinecraftNatives", packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/.minecraft/", verbose) #extractfile(getcachepath("MinecraftNatives", packconfig.getvalue("MinecraftVersion")) + "MinecraftNatives.zip", ppath("./packs/" + name + "/.minecraft/"), verbose)

    '''for mccomp in packconfig.getvalues("MinecraftComponents"):
        if not mccomp == "None":
            print("[BUILD][MC] " + mccomp)
            if not getcachepath(mccomp.split(",")[0], mccomp.split(",")[1]) == None:
                extractfile(getcachepath(mccomp.split(",")[0], mccomp.split(",")[1]) + mccomp.split(",")[0] + ".zip", ppath("./packs/" + name + "/.minecraft/"), verbose)'''

    if packconfig.getvalue("MinecraftInstallType") == "MultiVersion":
        print("[BUILD] Installing MultiVersion Forge...")
        cache_extract("Forge", packconfig.getvalue("ForgeVersion"), "./packs/" + name + "/.minecraft/", verbose)
        if packconfig.getvalue("UniversalConfigType") == "Default":
            uniconfig_default("Forge", packconfig.getvalue("ForgeVersion"), "./packs/" + name + "/.minecraft/config/", verbose)
        
    print("[BUILD] Installing ForgeLibraries...")
    cache_extract("ForgeLibraries", packconfig.getvalue("ForgeVersion"), "./packs/" + name + "/.minecraft/", verbose)
    
    print("[BUILD] Creating launch script...")
    cache_copy("__MinecraftLauncher", platform.system() + "." + packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/start" + cache_getftype("__MinecraftLauncher", platform.system() + "." + packconfig.getvalue("MinecraftVersion"))) #shutil.copyfile(ppath("./data/launch/" + platform.system() + "." + packconfig.getvalue("LaunchVersion") + getshellformat()), ppath("./packs/" + name + "/start" + getshellformat()))

    print("[BUILD] Obtaining Minecraft jarfile...")
    if packconfig.getvalue("MinecraftInstallType") == "Legacy":
        cache_copy("__Minecraft", packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/.minecraft/bin/minecraft.jar")
        #shutil.copyfile(ppath(getcachepath("Minecraft", packconfig.getvalue("MinecraftVersion")) + "Minecraft.jar"), ppath("./packs/" + name + "/.minecraft/bin/minecraft.jar"))
        #downloadfile("http://s3.amazonaws.com/Minecraft.Download/versions/" + packconfig.getvalue("MinecraftVersion") + "/" + packconfig.getvalue("MinecraftVersion") + ".jar", ppath("./packs/" + name + "/.minecraft/bin/minecraft.jar"))
    elif packconfig.getvalue("MinecraftInstallType") == "MultiVersion":
        cache_copy("__Minecraft", packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/.minecraft/versions/" + packconfig.getvalue("MinecraftVersion") + "/" + packconfig.getvalue("MinecraftVersion") + ".jar")
        #shutil.copyfile(ppath(getcachepath("Minecraft", packconfig.getvalue("MinecraftVersion")) + "Minecraft.jar"), ppath("./packs/" + name + "/.minecraft/versions/" + packconfig.getvalue("MinecraftVersion") + "/" + packconfig.getvalue("MinecraftVersion") + ".jar"))
        #downloadfile("http://s3.amazonaws.com/Minecraft.Download/versions/" + packconfig.getvalue("MinecraftVersion") + "/" + packconfig.getvalue("MinecraftVersion") + ".jar", ppath("./packs/" + name + "/.minecraft/versions/" + packconfig.getvalue("MinecraftVersion") + "/" + packconfig.getvalue("MinecraftVersion") + ".jar"))

    if not packconfig.getvalue("JarMods") == "None":
        
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
                cache_extract(jarmod.split(",")[0], jarmod.split(",")[1], ppath("./packs/" + name + "/.minecraft/mcext/"), verbose)
                if packconfig.getvalue("UniversalConfigType") == "Default":
                    uniconfig_default(jarmod.split(",")[0], jarmod.split(",")[1], "./packs/" + name + "/.minecraft/config/", verbose)
                #if not getcachepath(jarmod.split(",")[0], jarmod.split(",")[1]) == None:
                #    extractfile(ppath(getcachepath(jarmod.split(",")[0], jarmod.split(",")[1]) + jarmod.split(",")[0] + ".zip"), ppath("./packs/" + name + "/.minecraft/mcext/"), verbose)

        print("[BUILD] Recompressing Minecraft...")
        if packconfig.getvalue("MinecraftInstallType") == "Legacy":
            compressdir(ppath("./packs/" + name + "/.minecraft/mcext"), ppath("./packs/" + name + "/.minecraft/bin/minecraft.jar"), verbose)
        elif packconfig.getvalue("MinecraftInstallType") == "MultiVersion":
            compressdir(ppath("./packs/" + name + "/.minecraft/mcext"), ppath("./packs/" + name + "/.minecraft/versions/" + packconfig.getvalue("MinecraftVersion") + "/" + packconfig.getvalue("MinecraftVersion") + ".jar"), verbose)

        print("[BUILD] Cleaning up JarMod residue...")
        shutil.rmtree(ppath("./packs/" + name + "/.minecraft/mcext/"))

    print("[BUILD] Installing CoreMods...")
    if not os.path.isdir(ppath("./packs/" + name + "/.minecraft/coremods/")):
        os.mkdir(ppath("./packs/" + name + "/.minecraft/coremods/"))
    for coremod in packconfig.getvalues("CoreMods"):
        if not coremod == "None":
            print("[BUILD][COREMOD] " + coremod)
            cache_copy(coremod.split(",")[0], coremod.split(",")[1], ppath("./packs/" + name + "/.minecraft/coremods/" + coremod.split(",")[0] + cache_getftype(coremod.split(",")[0], coremod.split(",")[1])))
            if packconfig.getvalue("UniversalConfigType") == "Default":
                uniconfig_default(coremod.split(",")[0], coremod.split(",")[1], "./packs/" + name + "/.minecraft/config/", verbose)
            #if not getcachepath(coremod.split(",")[0], coremod.split(",")[1]) == None:
            #    shutil.copyfile(getcachepath(coremod.split(",")[0], coremod.split(",")[1]) + coremod.split(",")[0] + getcachetype(coremod.split(",")[0], coremod.split(",")[1]), ppath("./packs/" + name + "/.minecraft/coremods/" + coremod.split(",")[0] + getcachetype(coremod.split(",")[0], coremod.split(",")[1])))
                #shutil.copyfile(ppath(getcachepath(coremod.split(",")[0], coremod.split(",")[1]) + coremod.split(",")[0] + getcachetype(coremod.split(",")[0]), coremod.split(",")[1]), ppath("./packs/" + name + "/.minecraft/coremods/"))

    print("[BUILD] Installing Mods...")
    if not os.path.isdir(ppath("./packs/" + name + "/.minecraft/mods/")):
        os.mkdir(ppath("./packs/" + name + "/.minecraft/mods/"))
    for mod in packconfig.getvalues("Mods"):
        if not mod == "None":
            print("[BUILD][MOD] " + mod)
            cache_copy(mod.split(",")[0], mod.split(",")[1], ppath("./packs/" + name + "/.minecraft/mods/" + mod.split(",")[0] + cache_getftype(mod.split(",")[0], mod.split(",")[1])))
            if packconfig.getvalue("UniversalConfigType") == "Default":
                uniconfig_default(mod.split(",")[0], mod.split(",")[1], "./packs/" + name + "/.minecraft/config/", verbose)
            #if not getcachepath(mod.split(",")[0], mod.split(",")[1]) == None:
            #    shutil.copyfile(getcachepath(mod.split(",")[0], mod.split(",")[1]) + mod.split(",")[0] + getcachetype(mod.split(",")[0], mod.split(",")[1]), ppath("./packs/" + name + "/.minecraft/mods/" + mod.split(",")[0] + getcachetype(mod.split(",")[0], mod.split(",")[1])))
                #shutil.copyfile(ppath(getcachepath(mod.split(",")[0], mod.split(",")[1]) + mod.split(",")[0] + getcachetype(mod.split(",")[0], mod.split(",")[1])), ppath("./packs/" + name + "/.minecraft/mods/"))

    print("[BUILD] Installing Configs...")
    if not os.path.isdir(ppath("./packs/" + name + "/.minecraft/config/")):
        os.mkdir(ppath("./packs/" + name + "/.minecraft/config/"))
    for config in packconfig.getvalues("Configs"):
        if not config == "None":
            print("[BUILD][CONFIG] " + config)
            cache_copy(config.split(",")[0], config.split(",")[1], ppath("./packs/" + name + "/.minecraft/config/" + config.split(",")[0] + cache_getftype(config.split(",")[0], config.split(",")[1])))
            #if not getcachepath(config.split(",")[0], config.split(",")[1]) == None:
            #    shutil.copyfile(getcachepath(config.split(",")[0], config.split(",")[1]) + config.split(",")[0] + getcachetype(config.split(",")[0], config.split(",")[1]), ppath("./packs/" + name + "/.minecraft/config/" + config.split(",")[0] + getcachetype(config.split(",")[0], config.split(",")[1])))
                #shutil.copyfile(ppath(getcachepath(config.split(",")[0], config.split(",")[1]) + config.split(",")[0] + getcachetype(config.split(",")[0]), config.split(",")[1]), ppath("./packs/" + name + "/.minecraft/config/"))

    status_setcomplete(name)
    


if __name__ == "__main__":

    if not os.path.isdir(ppath("./cache/")):
        print("No cache found.  Making one...")
        downloadfile("https://dl.dropbox.com/s/05pj49fdimgyr4i/cache.tar.gz?dl=1", ppath("./cache.tar.gz"))
        tarball = tarfile.open(ppath("./cache.tar.gz"), "r:gz")
        tarball.extractall(".")

    if not os.path.isdir(ppath("./packs/")):
        print("No packs found.  Making some...")
        downloadfile("https://dl.dropbox.com/s/jhkvwaekdygh27n/packs.tar.gz?dl=1", ppath("./packs.tar.gz"))
        tarball = tarfile.open(ppath("./packs.tar.gz"), "r:gz")
        tarball.extractall(".")

    if not os.path.isfile(ppath("./ModdleFrontend.exe")):
        print("No Windows Launcher found.  Making one...")
        downloadfile("https://dl.dropbox.com/s/up8ezqyqy6yz7le/winlauncher.tar.gz?dl=1", ppath("./winlauncher.tar.gz"))
        tarball = tarfile.open(ppath("./winlauncher.tar.gz"), "r:gz")
        tarball.extractall(".")

    if not os.path.isfile(ppath("./config.vn")):
        print("No config file found.  Making one...")
        with open(ppath("./config.vn"), "w") as f:
            f.write("***\n*** Moddle Configuration\n***\n\nMemory=1024\n\nJavaPath=C:\Program Files\Java\jre7\bin\n\nVerbose=false")

    dotminecraft = ""

    if not os.path.isdir(ppath("./cache/__MinecraftAssets/")):
        print("No assets found!  Making some...")
        if dotminecraft == "":
            print("Please enter the path to your CLEAN MINECRAFT INSTALLATION ('%APPDATA%/.minecraft/' directory):")
            dotminecraft = input()
        os.makedirs(ppath("./cache/__MinecraftAssets/1.6.4/"))
        compressdir(os.path.join(dotminecraft, ppath("/assets/")), ppath("./cache/__MinecraftAssets/1.6.4/__MinecraftAssets.zip"), False)
        with open(ppath("./cache/__MinecraftAssets/1.6.4/filetype.vn"), "w") as f:
            f.write("FileType=.zip")

    '''if not os.path.isdir(ppath("./data/")):
        print("No data directory found.  Making one...")
        os.mkdir(ppath("./data/"))

    if not os.path.isdir(ppath("./data/launch/")):
        print("No launch files found.  Making some...")
        downloadfile("https://dl.dropbox.com/s/m2yunzwbjt22d5v/launch.tar.gz?dl=1", ppath("./launch.tar.gz"))
        tarball = tarfile.open(ppath("./launch.tar.gz"), "r:gz")
        tarball.extractall(ppath("./data/"))

    if not os.path.isdir(ppath("./data/minecraft/")):
        print("No minecraft data directory found.  Making one...")
        os.mkdir(ppath("./data/minecraft/"))

    if not os.path.isdir(ppath("./data/minecraft/Minecraft/")):
        print("No minecraft download directory found.  Making one...")
        os.mkdir(ppath("./data/minecraft/Minecraft/"))

    if not os.path.isdir(ppath("./data/minecraft/Libraries/")):
        print("No libraries found.  Making some...")
        downloadfile("https://dl.dropbox.com/s/h4mjby6cs0lruag/mclib.tar.gz?dl=1", ppath("./mclib.tar.gz"))
        tarball = tarfile.open(ppath("./mclib.tar.gz"), "r:gz")
        tarball.extractall(ppath("./data/minecraft/"))

    if not os.path.isdir(ppath("./data/minecraft/Natives/")):
        print("No natives found.  Making some...")
        downloadfile("https://dl.dropbox.com/s/5ex2yiqg5ntec1i/natlib.tar.gz?dl=1", ppath("./natlib.tar.gz"))
        tarball = tarfile.open(ppath("./natlib.tar.gz"), "r:gz")
        tarball.extractall(ppath("./data/minecraft/"))

    if not os.path.isdir(ppath("./packs/")):
        print("No pack directrory found.  Making one...")
        os.mkdir(ppath("./packs/"))

    if not os.path.isdir(ppath("./data/minecraft/Natives/")):
        print("No config.vn found.  Making one...")
        downloadfile("https://dl.dropbox.com/s/vft455hxdvdvvh9/config.vn?dl=1", ppath("./config.vn"))

    if not os.path.isdir(ppath("./data/minecraft/Assets/")):
        print("No assets found!  Please enter the location of your Minecraft assets v1.6.4 directory:")
        assetdiruser = input()
        os.makedirs(ppath("./data/minecraft/Assets/1.6.4/"))
        compressdir(assetdiruser, ppath("./data/minecraft/Assets/1.6.4/Assets.zip"), False)
        with open(ppath("./data/minecraft/Assets/1.6.4/filetype.vn"), "w") as f:
            f.write("FileType=.zip")'''

    print("")
    print("")

    

    username = ""
    #loginname = ""
    password = ""
    modpack = ""
    forceupdate = False
    memory = ""
    javapath = ""
    verbose = False
    nogui = False

    if len(sys.argv) > 1:
        for i in range(1, len(sys.argv)):
            if sys.argv[i].split("=")[0] == "-username":
                username = sys.argv[i].split("=")[1]
            elif sys.argv[i].split("=")[0] == "-password":
                password = sys.argv[i].split("=")[1]
            #elif sys.argv[i].split("=")[0] == "-loginname":
            #    loginname = sys.argv[i].split("=")[1]
            elif sys.argv[i].split("=")[0] == "-modpack":
                modpack = sys.argv[i].split("=")[1]
            elif sys.argv[i].split("=")[0] == "-memory":
                memory = sys.argv[i].split("=")[1]
            elif sys.argv[i].lower() == "-forceupdate=true":
                forceupdate = True
            elif sys.argv[i].lower() == "-nogui":
                nogui = True
            elif sys.argv[i].split("=")[0] == "-javapath":
                javapath = sys.argv[i].split("=")[1]
            elif sys.argv[i].split("=")[0] == "-verbose":
                verbose = sys.argv[i].split("=")[1]


    if platform.system() == "Windows" and nogui == False:
        p = subprocess.Popen([ppath("./ModdleFrontend.exe")], shell=False)
    else:
        print("###########################################################")
        print("#########  Moddle Integrated Minecraft Launcher  ##########")
        print("###########################################################")
        print("")
        print("")

        
        if modpack == "":
            print("Please enter the name of the modpack you wish to play.")
            print("Modpack:")
            modpackex = input()
            modpack = modpackex.split(":")[0]
            if modpackex.find(":forceupdate") > -1:
                print("Forcing update.")
                forceupdate = True
            if modpackex.find(":f") > -1:
                print("Forcing update.")
                forceupdate = True
            if modpackex.find(":verbose") > -1:
                print("Verbose mode.")
                verbose = True
            if modpackex.find(":v") > -1:
                print("Verbose mode.")
                verbose = True
            print("Modpack '" + modpack + "' selected.")


        
        if os.path.exists(ppath("./login.vn")):
            loginconfig = VictoryNotationFile(ppath("./login.vn"))
            if username == "":
                username = loginconfig.getvalue("Username")
            if password == "":
                password = loginconfig.getvalue("Password")
        else:
            if username == "" and password == "":
                print("")
                print("Please enter your Minecraft account info.")
                print("")
                print("Username:")
                username = input()
                '''regexp = re.compile(r'\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b')
                if regexp.search(loginname) is None:
                    print("Minecraft Displayname:")
                    username = input()
                else:
                    username = loginname'''
                print("Password:")
                password = input()
                

        print("")
        print("")
        print("[LOGIN] Obtaining session ID...")

        downloadfile("http://login.minecraft.net/?user=" + username + "&password=" + password + "&version=12", ppath("./login.tmp"))
        #with open(ppath("./login.tmp"), 'b+w') as f:
        #    f.write(d.read())
        for lines in open(ppath("./login.tmp"), "r"):
            username = lines.split(":")[2]
            sessionid = lines.split(":")[3]
        os.remove(ppath("./login.tmp"))
        print("[LOGIN] Session ID " + sessionid + " generated.")


        print("[PREBUILD] Parsing config.mmlx...")
        moddleconfig = VictoryNotationFile(ppath("./config.vn"))
        if javapath == "":
            javapath = moddleconfig.getvalue("JavaPath")
        if memory == "":
            memory = moddleconfig.getvalue("Memory")
        if verbose == False:
            if moddleconfig.getvalue("Verbose") == "true":
                verbose = True
            else:
                verbose = False
            

        print("[PREBUILD] Starting modpack builder...")

        if not os.path.isfile(ppath("./packs/" + modpack + ".zip")):
            print("[PREBUILD][WARNING] Pack does not exist!")
            input()
            exit(0)

        if os.path.isdir(ppath("./ext/")):
            print("[PREBUILD] Cleaning extraction directory...")
            shutil.rmtree(ppath("./ext/"))
            os.mkdir(ppath("./ext/"))
        else:
            print("[PREBUILD] Creating extraction directory...")
            os.mkdir(ppath("./ext/"))

        print("[PREBUILD] Extracting pack...")
        extractfile(ppath("./packs/" + modpack + ".zip"), ppath("./ext/"), verbose)

        print("[PREBUILD] Loading pack config...")
        packconfig = VictoryNotationFile(ppath("./ext/config.vn"))
        
        if os.path.isfile(ppath("./packs/" + modpack + "/inst_complete")):
            if forceupdate == True:
                if packconfig.getvalue("PackType") == "Forge":
                    build_forge(modpack, packconfig, verbose)
                else:
                    builder = imp.load_source(packconfig.getvalue("PackBuilder"), ppath("./builders/" + packconfig.getvalue("PackBuilder") + ".py"))
                    builder.build(modpack, packconfig, verbose)
        else:
            if packconfig.getvalue("PackType") == "Forge":
                build_forge(modpack, packconfig, verbose)
            else:
                builder = imp.load_source(packconfig.getvalue("PackBuilder"), ppath("./builders/" + packconfig.getvalue("PackBuilder") + ".py"))
                builder.build(modpack, packconfig, verbose)

        print("[POSTBUILD] Cleaning extraction directory...")
        shutil.rmtree(ppath("./ext/"))


        print("[LAUNCH] Copying launch template...")
        with open (ppath("./packs/" + modpack + "/start.bat"), "r") as f:
            data=f.read()
            
        print("[LAUNCH] Generating launch script...")
        data = data.replace("{JAVAPATH}", javapath.replace("\n", ""))
        data = data.replace("{MEMORY}", memory.replace("\n", ""))
        data = data.replace("{USERNAME}", username)
        data = data.replace("{SESSIONID}", sessionid)
        data = data.replace("{MODPACK}", modpack)
        data = data.replace("{APPDATA}", os.getcwd() + "\\packs\\" + modpack + "\\")
        data = data.replace("{GAMEDIR}", os.getcwd() + "\\packs\\" + modpack + "\\.minecraft\\")
        data = data.replace("{VERSION}", packconfig.getvalue("MinecraftVersion"))
        data = data.replace("{PACKTYPE}", packconfig.getvalue("PackType"))
        with open (ppath("./packs/" + modpack + "/start.bat"), "w") as f:
            f.write(data)
        print("[LAUNCH] Starting Minecraft...")
        p = subprocess.Popen([ppath("./packs/" + modpack + "/start.bat")], shell=False)

