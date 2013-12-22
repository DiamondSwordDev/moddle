#!/usr/bin/env python3

import urllib.request
import subprocess
import traceback
import platform
import getpass
import zipfile
import tarfile
import shutil
import string
import imp
import sys
import os



class VictoryNotationValue:
    
    name = ""
    values = []

    def __init__(self):
        self.values = []
        self.name = "null"



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
                    v.values = [ l.split("=")[1] ] 
                    self.values.append(v)
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
        return None

    def getvalues(self, name):
        for m in self.values:
            if m.name == name:
                if not m.values == []:
                    return m.values
                else:
                    return None
        return None

    def getnodes(self):
        nodelist = []
        for m in self.values:
            nodelist.append(m.name)
        return nodelist

    def setnode(self, name, value):
        for m in self.values:
            if m.name == name:
                m.values = value

    def makenode(self, name, value):
        newnode = VictoryNotationValue()
        newnode.name = name
        newnode.values = value
        self.values.append(newnode)

    def save(self, filename):
        contents = ""
        for m in self.values:
            if len(m.values) > 1:
                contents = contents + m.name + ":\n"
                for value in m.values:
                    contents = contents + "-" + value + "\n"
                contents = contents + "\n"
            else:
                contents = contents + m.name + "=" + m.values[0] + "\n\n"
        with open(filename, "w") as f:
            f.write(contents)


def ppath(path):
    if platform.system() == "Windows":
        return path.replace("/", "\\")
    elif platform.system() == "Linux":
        return path.replace("\\", "/")
    else:
        return path.replace("\\", "/")


def getshellformat():
    if platform.system() == "Windows":
        return ".bat"
    elif platform.system() == "Linux":
        return ".sh"
    else:
        return ".sh"

def extractfile(filename, target):
    with zipfile.ZipFile(filename, 'r') as z:
        for n in z.namelist():
            if n.endswith("/"):
                try:
                    os.makedirs(target + ppath(n))
                except:
                    pass
                continue
            if not os.path.isdir(os.path.dirname(target + ppath(n))):
                os.makedirs(os.path.dirname(target + ppath(n)))
            with open(target + ppath(n).replace("aux.class", "_aux.class"), "wb") as f:
                f.write(z.read(n))

def compress__directory(startingdir, directory, z):
    for root, directories, files in os.walk(directory):
        for file in files:
            #fpath = os.join(root, file) #root + ppath("/") + file
            fpath = root + ppath("/") + file
            z.write(fpath, os.path.join(root.replace(startingdir, ""), file).replace("_aux.class", "aux.class"))
        for sub in directories:
            compress__directory(startingdir, os.path.join(root + sub), z)

def compress__directory__old(rootdir, directory, z):
    for root, directorys, files in os.walk(directory):
        for file in files:
            z.write(os.path.join(directory + file), directory.replace(rootdir, "") + file.replace("_aux.class", "aux.class"))
        for direx in dirs:
            compress__directory(rootdir, direx, z)

def compressdir(directory, target):
    z = zipfile.ZipFile(target, 'w')
    compress__directory(directory, directory, z)
    z.close()
    
def downloadfile(url, target):
    d = urllib.request.urlopen(url)
    with open(target, 'b+w') as f:
        f.write(d.read())


def copyfile_macro(source, target, config):
    contents = ""
    with open(source, "r") as f:
        contents = f.read()
    newcontents = ""
    skipping = False
    for line in contents.split("\n"):
        if line[:6] == "{$IF} ":
            hasnode = False
            for node in config.getnodes():
                if node == line[6:]:
                    hasnode = True
                else:
                    for value in config.getvalues(node):
                        if value == line[6:]:
                            hasnode = True
            if hasnode == False:
                skipping = True
        elif line[:9] == "{$IFHAS} ":
            hasnode = False
            for node in config.getnodes():
                if node.find(line[9:]) > -1:
                    hasnode = True
                else:
                    for value in config.getvalues(node):
                        if value.find(line[9:]) > -1:
                            hasnode = True
            if hasnode == False:
                skipping = True
        elif line[:11] == "{$IFHASNO} ":
            hasnode = False
            for node in config.getnodes():
                if node.find(line[11:]) > -1:
                    hasnode = True
                else:
                    for value in config.getvalues(node):
                        if value.find(line[11:]) > -1:
                            hasnode = True
            if hasnode == True:
                skipping = True
        elif line[:8] == "{$ENDIF}":
            skipping = False
        else:
            if skipping == False:
                modline = line
                for node in config.getnodes():
                    modline = modline.replace("{" + node + "}", config.getvalue(node))
                newcontents = newcontents + modline + "\n"
    with open(target, "w") as f:
        f.write(newcontents)


def cache_getftype(name, version):
    if os.path.isfile(ppath("./ext/cache/" + name + "/" + version + "/filetype.vn")):
        filetype = VictoryNotationFile(ppath("./ext/cache/" + name + "/" + version + "/filetype.vn"))
        return filetype.getvalue("FileType")
    elif os.path.isfile(ppath("./cache/" + name + "/" + version + "/filetype.vn")):
        filetype = VictoryNotationFile(ppath("./cache/" + name + "/" + version + "/filetype.vn"))
        return filetype.getvalue("FileType")
    else:
        return ".zip"


def cache_hasperms(name, version):
    if os.path.isfile(ppath("./ext/cache/" + name + "/" + version + "/perms.vn")):
        print("[PERMS] Found permissions data for " + name)
    elif os.path.isfile(ppath("./cache/" + name + "/" + version + "/perms.vn")):
        print("[PERMS] Found permissions data for " + name)
    else:
        print("[WARNING] Permissions data for " + name + " cannot be found!")
        return False

    if os.path.isfile(ppath("./ext/cache/" + name + "/" + version + "/perms.png")):
        print("[PERMS] Found permissions snapshot for " + name)
    elif os.path.isfile(ppath("./cache/" + name + "/" + version + "/perms.png")):
        print("[PERMS] Found permissions snapshot for " + name)
    else:
        print("[WARNING] Permissions snapshot for " + name + " cannot be found!")
        return False

    if os.path.isfile(ppath("./ext/cache/" + name + "/" + version + "/perms.vn")):
        permfile = VictoryNotationFile(ppath("./ext/cache/" + name + "/" + version + "/perms.vn"))
        print("[PERMS] Permission granted for " + name + ", by " + permfile.getvalue("Author"))
    elif os.path.isfile(ppath("./cache/" + name + "/" + version + "/perms.vn")):
        permfile = VictoryNotationFile(ppath("./cache/" + name + "/" + version + "/perms.vn"))
        print("[PERMS] Permission granted for " + name + ", by " + permfile.getvalue("Author"))
    else:
        print("[WARNING] Permissions data for " + name + " cannot be accessed!")
        return False

    return True

    '''if os.path.isfile(ppath(cachedir + name + "/" + version + "/perms.vn")):
        permfile = VictoryNotationFile(ppath(cachedir + name + "/" + version + "/perms.vn"))
        if permfile.getvalue("Author") == None:
            print("[WARNING] " + name + " v" + version + " does not provide an author name!")
            return False
        else:
            print("[PERMS] Validating " + name + ", by " + permfile.getvalue("Author") + "...")
        if permfile.getvalue("PermType") == "Snapshot":
            if not os.path.isfile(ppath(cachedir + name + "/" + version + "/perms.png")):
                print("[WARNING] " + name + " v" + version + " does not provide a snapshot of permissions data!")
                return False
        elif permfile.getvalue("PermType") == "Webpage":
            if permfile.getvalue("PageUrl") == None:
                print("[WARNING] " + name + " v" + version + " does not provide a valid URL for permissions data!")
                return False
        return True
    else:
        print("[WARNING] " + name + " v" + version + " does not have available permissions data!")
        return False'''
        

def cache_assert(name, version):
    if os.path.isfile(ppath("./ext/cache/" + name + "/" + version + "/" + name + cache_getftype(name, version))):
        return cache_hasperms(name, version)
    elif os.path.isfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version))):
        return cache_hasperms(name, version)
    else:
        if cache_hasperms(name, version):
            installed = False
            for root, dirs, files in os.walk(ppath("./cache/")):
                for file in files:
                    if os.path.isfile(ppath("./cache/" + file)): # and file.endswith(".vn"): WHY THIS NO WORK??
                        modlistfile = VictoryNotationFile(ppath("./cache/" + file))
                        for node in modlistfile.getnodes():
                            if node == name:
                                for v in modlistfile.getvalues(node):
                                    if v.split(",")[0] == version:
                                        if v.split(",")[1] == "{tarball}":
                                            downloadfile(v.split(",")[2], ppath("./cache/" + name + "/" + version + "/" + name + ".tar.gz"))
                                            tarball = tarfile.open(ppath("./cache/" + name + "/" + version + "/" + name + ".tar.gz"), "r:gz")
                                            tarball.extractall(ppath("./cache/" + name + "/" + version + "/"))
                                        else:
                                            try:
                                                os.makedirs(ppath("./cache/" + name + "/" + version + "/"))
                                            except:
                                                pass
                                            downloadfile(v.split(",")[2], ppath("./cache/" + name + "/" + version + "/" + name + v.split(",")[1]))
                                            with open(ppath("./cache/" + name + "/" + version + "/filetype.vn"), "w") as f:
                                                f.write("FileType=" + v.split(",")[1])
                                        installed = True
            return installed
        else:
            return False


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
    elif name == "__MinecraftServer":
        if os.path.isdir(ppath("./cache/__MinecraftServer/" + version + "/")):
            shutil.copyfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target))
        else:
            print("[BUILD] Server version not found.  Downloading to cache...")
            os.makedirs(ppath("./cache/__MinecraftServer/" + version + "/"))
            downloadfile("http://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/minecraft_server." + version + ".jar", ppath("./cache/__MinecraftServer/" + version + "/__MinecraftServer.jar"))
            with open(ppath("./cache/__MinecraftServer/" + version + "/filetype.vn"), "w") as f:
                f.write("FileType=.jar")
            shutil.copyfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target))
    else:
        if os.path.isdir(ppath("./ext/cache/" + name + "/" + version + "/")):
            shutil.copyfile(ppath("./ext/cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target))
        elif os.path.isdir(ppath("./cache/" + name + "/" + version + "/")):
            shutil.copyfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target))
        else:
            print("[BUILD][WARNING] " + name + " " + version + " was not found and was unable to be installed.")


def cache_extract(name, version, target):
    if name == "__Minecraft":
        if os.path.isdir(ppath("./cache/__Minecraft/" + version + "/")):
            extractfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target))
        else:
            print("[BUILD] Minecraft version not found.  Downloading to cache...")
            os.makedirs(ppath("./cache/__Minecraft/" + version + "/"))
            downloadfile("http://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".jar", ppath("./cache/__Minecraft/" + version + "/__Minecraft.jar"))
            with open(ppath("./cache/__Minecraft/" + version + "/filetype.vn"), "w") as f:
                f.write("FileType=.jar")
            extractfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target))
    elif name == "__MinecraftServer":
        if os.path.isdir(ppath("./cache/__MinecraftServer/" + version + "/")):
            extractfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target))
        else:
            print("[BUILD] Server version not found.  Downloading to cache...")
            os.makedirs(ppath("./cache/__MinecraftServer/" + version + "/"))
            downloadfile("http://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/minecraft_server." + version + ".jar", ppath("./cache/__MinecraftServer/" + version + "/__MinecraftServer.jar"))
            with open(ppath("./cache/__MinecraftServer/" + version + "/filetype.vn"), "w") as f:
                f.write("FileType=.jar")
            extractfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target))
    else:
        if os.path.isdir(ppath("./ext/cache/" + name + "/" + version + "/")):
            extractfile(ppath("./ext/cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target))
        elif os.path.isdir(ppath("./cache/" + name + "/" + version + "/")):
            extractfile(ppath("./cache/" + name + "/" + version + "/" + name + cache_getftype(name, version)), ppath(target))
        else:
            print("[BUILD][WARNING] " + name + " " + version + " was not found and was unable to be installed.")


def cache_uniconfig_copydir(source, target, config):
    for root, dirs, files in os.walk(source):
        for file in files:
            try:
                os.makedirs(os.path.join(target, root.replace(source, "")))
            except:
                pass
            copyfile_macro(os.path.join(root, file), os.path.join(target, root.replace(source, ""), file), config)
            

def cache_uniconfig(name, version, modpack, config):
    if not packconfig.getvalue("UniversalConfigType") == "None":
        cache_extract("__UniversalConfig" + config.getvalue("UniversalConfigType"), name + "." + version, ppath("./etmp/"))
        if modpack.find("_server") > -1:
            if not os.path.isdir(ppath("./packs/" + modpack + "/config/")):
                os.mkdir(ppath("./packs/" + modpack + "/config/"))
            cache_uniconfig_copydir(ppath("./etmp/"), ppath("./packs/" + modpack + "/config/"), config)
        else:
            if not os.path.isdir(ppath("./packs/" + modpack + "/.minecraft/config/")):
                os.mkdir(ppath("./packs/" + modpack + "/.minecraft/config/"))
            cache_uniconfig_copydir(ppath("./etmp/"), ppath("./packs/" + modpack + "/.minecraft/config/"), config)
        try:
            shutil.rmtree(ppath("./etmp/"))
        except:
            pass

        
def uniconfig_default(name, version, target):
    if not os.path.isdir(ppath(target)):
        os.makedirs(ppath(target))
    if os.path.isdir(ppath("./cache/__UniversalConfigDefault/" + name + "/" + version + "/")):
        extractfile(ppath("./cache/__UniversalConfigDefault/" + name + "/" + version + "/" + name + ".zip"), ppath(target))
    

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


def build_forge(name, packconfig):
    
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
        print("[BUILD][MC] Installing Natives..")
        cache_extract("__MinecraftNatives", packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/.minecraft/")
        print("[BUILD][MC] Installing Libraries...")
        cache_extract("__MinecraftLibraries", packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/.minecraft/")
        print("[BUILD][MC] Installing Assets...")
        cache_extract("__MinecraftAssets", packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/.minecraft/")
        print("[BUILD][MC] Installing Moddle Asset Patches...")
        cache_extract("__ModdleAssets", "0.0.1", "./packs/" + name + "/.minecraft/")
    elif packconfig.getvalue("MinecraftInstallType") == "Legacy":
        print("[BUILD][MC] Installing Natives...")
        cache_extract("__MinecraftNatives", packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/.minecraft/")

    if packconfig.getvalue("MinecraftInstallType") == "MultiVersion":
        print("[BUILD] Installing MultiVersion Forge...")
        cache_extract("Forge", packconfig.getvalue("ForgeVersion"), "./packs/" + name + "/.minecraft/")
        cache_uniconfig("Forge", packconfig.getvalue("ForgeVersion"), name, packconfig)
        
    print("[BUILD] Installing ForgeLibraries...")
    cache_extract("ForgeLibraries", packconfig.getvalue("ForgeVersion"), "./packs/" + name + "/.minecraft/")
    
    print("[BUILD] Creating launch script...")
    #INCLUDES VOLATILE cache_getftype() CODE!!!
    #if cache_assert("__MinecraftLauncher", platform.system() + "." + packconfig.getvalue("MinecraftVersion")) == True:
    cache_copy("__MinecraftLauncher", platform.system() + "." + packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/start_template" + cache_getftype("__MinecraftLauncher", platform.system() + "." + packconfig.getvalue("MinecraftVersion"))) #shutil.copyfile(ppath("./data/launch/" + platform.system() + "." + packconfig.getvalue("LaunchVersion") + getshellformat()), ppath("./packs/" + name + "/start" + getshellformat()))

    print("[BUILD] Obtaining Minecraft jarfile...")
    if packconfig.getvalue("MinecraftInstallType") == "Legacy":
        cache_copy("__Minecraft", packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/.minecraft/bin/minecraft.jar")
    elif packconfig.getvalue("MinecraftInstallType") == "MultiVersion":
        cache_copy("__Minecraft", packconfig.getvalue("MinecraftVersion"), "./packs/" + name + "/.minecraft/versions/" + packconfig.getvalue("MinecraftVersion") + "/" + packconfig.getvalue("MinecraftVersion") + ".jar")
        
    if not packconfig.getvalue("JarMods") == "None":
        
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
                cache_copy(config.split(",")[0], config.split(",")[1], ppath("./packs/" + name + "/.minecraft/config/" + config.split(",")[0] + cache_getftype(config.split(",")[0], config.split(",")[1])))
            
    status_setcomplete(name)


def build_forge_server(name, packconfig):
    
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
    

    


if __name__ == "__main__":
    try:

        username = ""
        password = ""
        modpack = ""
        forceupdate = False
        memory = ""
        javapath = ""
        nogui = False
        runserver = False

        if len(sys.argv) > 1:
            for i in range(1, len(sys.argv)):
                if sys.argv[i].split("=")[0] == "-username":
                    username = sys.argv[i].split("=")[1]
                elif sys.argv[i].split("=")[0] == "-password":
                    password = sys.argv[i].split("=")[1]
                elif sys.argv[i].split("=")[0] == "-modpack":
                    modpack = sys.argv[i].split("=")[1]
                elif sys.argv[i].split("=")[0] == "-memory":
                    memory = sys.argv[i].split("=")[1]
                elif sys.argv[i].lower() == "-forceupdate=true":
                    forceupdate = True
                elif sys.argv[i].lower() == "-nogui":
                    nogui = True
                elif sys.argv[i].lower() == "-runserver":
                    runserver = True
                elif sys.argv[i].split("=")[0] == "-javapath":
                    javapath = sys.argv[i].split("=")[1]

        

        if nogui == False:

            print("Checking for updates...")

            if not os.path.isfile(ppath("./mupdate.py")):
                print("Downloading MupDate...")
                downloadfile("https://dl.dropboxusercontent.com/u/242871063/moddle/mupdate.py?dl=1", ppath("./mupdate.py"))

            experimental = False

            if os.path.isfile(ppath("./config.vn")):
                moddleconfig = VictoryNotationFile(ppath("./config.vn"))
                if moddleconfig.getvalue("Experimental") == "true":
                    experimental = True

            versiontext = ""
            if os.path.isfile(ppath("./version.vn")):
                versionconfig = VictoryNotationFile(ppath("./version.vn"))
                versiontext = versionconfig.getvalue("ModdleVersion")
            else:
                versiontext = "0"

            if experimental == False:
                downloadfile("https://dl.dropboxusercontent.com/u/242871063/moddle/latestversion_stable.vn?dl=1", ppath("./latestversion.vn"))
            else:
                downloadfile("https://dl.dropboxusercontent.com/u/242871063/moddle/latestversion_experimental.vn?dl=1", ppath("./latestversion.vn"))

            latestversionconfig = VictoryNotationFile(ppath("./latestversion.vn"))
            
            if not latestversionconfig.getvalue("ModdleVersion") == versiontext:

                if os.path.isdir(ppath("./cache/")):
                    print("-----------------------------------------------------")
                    print("MODDLE VERSION " + latestversionconfig.getvalue("ModdleVersion") + " IS NOW AVAILABLE!")
                    print("-----------------------------------------------------")
                    print("")
                    print("Do you want to update right now? (y/n)")
                    yesno = input()
                    if yesno == "y" or yesno == "yes":
                        if experimental == False:
                            p = subprocess.Popen(["python", ppath("./mupdate.py")], shell=False)
                        else:
                            p = subprocess.Popen(["python", ppath("./mupdate.py"), "-experimental"], shell=False)
                        exit(0)
                    else:
                        print("Very well then, continuing on with business...")
                else:
                    print("-----------------------------------------------------")
                    print("INSTALL MODDLE VERSION " + latestversionconfig.getvalue("ModdleVersion"))
                    print("-----------------------------------------------------")
                    print("")
                    print("Do you want to install Moddle now? (y/n)")
                    yesno = input()
                    if yesno == "y" or yesno == "yes":
                        p = subprocess.Popen(["python", ppath("./mupdate.py")], shell=False)
                        exit(0)
                    else:
                        print("Very well then, Moddle will not be installed on your system...")
                        input()
                        exit(0)
                        
                
        '''if not os.path.isdir(ppath("./cache/")):
            print("Downloading the cache... (This could take a while!)")
            downloadfile("https://dl.dropbox.com/s/pq0pjn2uvyv5qhq/cache.tar.gz?dl=1", ppath("./cache.tar.gz"))
            tarball = tarfile.open(ppath("./cache.tar.gz"), "r:gz")
            tarball.extractall(".")
        
        if not os.path.isdir(ppath("./packs/")):
            print("Fetching modpacks... (This could take a while!)")
            downloadfile("https://dl.dropbox.com/s/tx6qfnhb5ml1z5f/packs.tar.gz?dl=1", ppath("./packs.tar.gz"))
            tarball = tarfile.open(ppath("./packs.tar.gz"), "r:gz")
            tarball.extractall(".")

        if not os.path.isfile(ppath("./ModdleFrontend.exe")):
            print("Downloading the Windows launcher... (This could take a while!)")
            downloadfile("https://dl.dropbox.com/s/5uyvfxbga7volc7/winlauncher.tar.gz?dl=1", ppath("./winlauncher.tar.gz"))
            tarball = tarfile.open(ppath("./winlauncher.tar.gz"), "r:gz")
            tarball.extractall(".")

        if not os.path.isfile(ppath("./config.vn")):
            print("Making the main config file... (This won't take very long!)")
            with open(ppath("./config.vn"), "w") as f:
                f.write("***\n*** Moddle Configuration\n***\n\nMemory=1024\n\nJavaPath=C:\\Program Files\\Java\\jre7\\bin\\")'''

        if not os.path.isdir(ppath("./cache/__MinecraftAssets/")):
            print("My good player, it appears that you have not installed your Minecraft assets")
            print("into Moddle yet.  Please, allow me to install them for you.  This could take")
            print("a bit of time.")
            print("Please enter the path to your '%APPDATA%/.minecraft' directory:")
            print("(This is for building Minecraft assets.  Note that your assets")
            print("must be UNMODIFIED!)")
            dotminecraft = input()
            os.makedirs(ppath("./cache/__MinecraftAssets/1.6.4/atmp/"))
            assetdir = dotminecraft + ppath("/assets/virtual/legacy/")
            assetdir = assetdir.replace(ppath("//"), ppath("/"))
            print("Copying...")
            shutil.copytree(assetdir, ppath("./cache/__MinecraftAssets/1.6.4/atmp/assets/"))
            print("Compressing...")
            compressdir(ppath("./cache/__MinecraftAssets/1.6.4/atmp/"), ppath("./cache/__MinecraftAssets/1.6.4/__MinecraftAssets.zip"))
            print("Cleaning up...")
            shutil.rmtree(ppath("./cache/__MinecraftAssets/1.6.4/atmp/"))
            with open(ppath("./cache/__MinecraftAssets/1.6.4/filetype.vn"), "w") as f:
                f.write("FileType=.zip")
            print("Done.")



        if platform.system() == "Windows" and nogui == False:
            p = subprocess.Popen([ppath("./ModdleFrontend.exe")], shell=False)
        else:

            if not platform.system() == "Windows":
                print("")
                print("")
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
                print("Modpack '" + modpack + "' selected.")


            if runserver == False:
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
                        password = getpass.getpass(prompt='Password:\n', stream=None)
                print("")
                print("")

                print("[LOGIN] Obtaining session ID...")

                downloadfile("http://login.minecraft.net/?user=" + username + "&password=" + password + "&version=12", ppath("./login.tmp"))
                for lines in open(ppath("./login.tmp"), "r"):
                    username = lines.split(":")[2]
                    sessionid = lines.split(":")[3]
                os.remove(ppath("./login.tmp"))
                print("[LOGIN] Session ID " + sessionid + " generated.")


            print("[PREBUILD] Parsing config.vn...")
            moddleconfig = VictoryNotationFile(ppath("./config.vn"))
            if javapath == "":
                javapath = moddleconfig.getvalue("JavaPath")
            if memory == "":
                memory = moddleconfig.getvalue("Memory")
                

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
            extractfile(ppath("./packs/" + modpack + ".zip"), ppath("./ext/"))

            print("[PREBUILD] Loading pack config...")
            packconfig = VictoryNotationFile(ppath("./ext/config.vn"))

            if runserver == True:
                if os.path.isfile(ppath("./packs/" + modpack + "_server/inst_complete")):
                    if forceupdate == True:
                        if packconfig.getvalue("PackType") == "Forge":
                            build_forge_server(modpack, packconfig)
                        else:
                            builder = imp.load_source(packconfig.getvalue("PackType"), ppath("./builders/" + packconfig.getvalue("PackType") + ".py"))
                            builder.build_server(modpack, packconfig)
                else:
                    if packconfig.getvalue("PackType") == "Forge":
                        build_forge_server(modpack, packconfig)
                    else:
                        builder = imp.load_source(packconfig.getvalue("PackType"), ppath("./builders/" + packconfig.getvalue("PackType") + ".py"))
                        builder.build_server(modpack, packconfig)
            else:
                if os.path.isfile(ppath("./packs/" + modpack + "/inst_complete")):
                    if forceupdate == True:
                        if packconfig.getvalue("PackType") == "Forge":
                            build_forge(modpack, packconfig)
                        else:
                            builder = imp.load_source(packconfig.getvalue("PackType"), ppath("./builders/" + packconfig.getvalue("PackType") + ".py"))
                            builder.build(modpack, packconfig)
                else:
                    if packconfig.getvalue("PackType") == "Forge":
                        build_forge(modpack, packconfig)
                    else:
                        builder = imp.load_source(packconfig.getvalue("PackType"), ppath("./builders/" + packconfig.getvalue("PackType") + ".py"))
                        builder.build(modpack, packconfig)

            print("[POSTBUILD] Cleaning extraction directory...")
            shutil.rmtree(ppath("./ext/"))

            if runserver == True:
                print("[LAUNCH] Copying launch template...")
                with open (ppath("./packs/" + modpack + "_server/start_template.bat"), "r") as f:
                    data=f.read()
                    
                print("[LAUNCH] Generating launch script...")
                data = data.replace("{JAVAPATH}", javapath.replace("\n", ""))
                data = data.replace("{MEMORY}", memory.replace("\n", ""))
                data = data.replace("{MODPACK}", modpack)
                data = data.replace("{APPDATA}", os.getcwd() + "\\packs\\" + modpack + "_server\\")
                data = data.replace("{GAMEDIR}", os.getcwd() + "\\packs\\" + modpack + "_server\\.minecraft\\")
                data = data.replace("{VERSION}", packconfig.getvalue("MinecraftVersion"))
                data = data.replace("{PACKTYPE}", packconfig.getvalue("PackType"))
                data = data.replace("{FORGEVERSION}", packconfig.getvalue("ForgeVersion"))
                with open (ppath("./packs/" + modpack + "_server/start.bat"), "w") as f:
                    f.write(data)
                    
                print("[LAUNCH] Starting Minecraft...")
                p = subprocess.Popen([ppath("./packs/" + modpack + "_server/start.bat")], shell=False)
            else:
                '''print("[LAUNCH] Copying launch template...")
                with open (ppath("./packs/" + modpack + "/start_template.bat"), "r") as f:
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
                data = data.replace("{FORGEVERSION}", packconfig.getvalue("ForgeVersion"))
                with open (ppath("./packs/" + modpack + "/start.bat"), "w") as f:
                    f.write(data)'''

                print("[LAUNCH] Copying launch template (NEW!)...")
                fullconfig = packconfig
                
                usernameval = VictoryNotationValue()
                usernameval.name = "Username"
                usernameval.values.append(username)
                fullconfig.values.append(usernameval)

                sessionval = VictoryNotationValue()
                sessionval.name = "SessionID"
                sessionval.values.append(sessionid)
                fullconfig.values.append(sessionval)

                modpackval = VictoryNotationValue()
                modpackval.name = "Modpack"
                modpackval.values.append(modpack)
                fullconfig.values.append(modpackval)

                memoryval = VictoryNotationValue()
                memoryval.name = "Memory"
                memoryval.values.append(memory)
                fullconfig.values.append(memoryval)

                javapathval = VictoryNotationValue()
                javapathval.name = "JavaPath"
                javapathval.values.append(javapath)
                fullconfig.values.append(javapathval)

                appdataval = VictoryNotationValue()
                appdataval.name = "AppData"
                appdataval.values.append(os.getcwd() + "\\packs\\" + modpack + "\\")
                fullconfig.values.append(appdataval)
                
                copyfile_macro(ppath("./packs/" + modpack + "/start_template.bat"), ppath("./packs/" + modpack + "/start.bat"), packconfig)
                    
                print("[LAUNCH] Starting Minecraft...")
                p = subprocess.Popen([ppath("./packs/" + modpack + "/start.bat")], shell=False)



    except Exception as e:
        type_, value_, traceback_ = sys.exc_info()
        for line in traceback.format_exception(type_, value_, traceback_):
            print(line)
        input()
