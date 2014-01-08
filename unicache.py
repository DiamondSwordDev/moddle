import string
import os
import shutil
import tarfile

import vnlib
import mcore



class InstalledMods:

    dlist = []

    def __init__(self):
        self.dlist = []

    def add(name):
        InstalledMods.dlist.append(name)

    def getlist():
        return InstalledMods.dlist



def cache_copydir_macro(source, target, config):
    for root, dirs, files in os.walk(source):
        for file in files:
            try:
                os.makedirs(os.path.join(root.replace(source, target)))
            except:
                pass
            vnlib.copyfile_macro(os.path.join(root, file), os.path.join(root.replace(source, target), file), config)



def cache_get(name, version, target, packconfig, configroot):
    cachelocation = os.path.join(".", "cache")
    if os.path.isdir(os.path.join(".", "ext", "cache", name, version)):
        cachelocation = os.path.join(".", "ext", "cache", name, version)
    elif os.path.isdir(os.path.join(".", "cache", name, version)):
        cachelocation = os.path.join(".", "cache", name, version)
    else:
        print("[BUILD][WARNING] " + name + " version " + " could not be found!")
        return

    entryconfig = vnlib.VictoryNotationFile(os.path.join(cachelocation, "entry.vn"))

    if not os.path.isfile(os.path.join(cachelocation, "perms.png")):
        print("[BUILD][WARNING] " + name + " version " + " does not have a valid permissions snapshot!")
        return
    else:
        print("[BUILD][PERMS] Permission granted for " + name + ", by " + entryconfig.getvalue("Author"))

    for subentry in entryconfig.getnodes():

        if subentry == "Author" or subentry == "HasConfigs" or subentry == "ConfigInstallDir" or subentry == "Dependencies":
            continue
        
        se_extract = False
        se_url = ""
        
        for l in entryconfig.getvalues(subentry):
            if l.split("=")[0].lower() == "extract" and l.split("=")[1].lower() == "true":
                se_extract = True
            elif l.split("=")[0].lower() == "url":
                se_url = l[4:]
                
        if not os.path.isfile(os.path.join(cachelocation, subentry)):
            try:
                mcore.downloadfile(se_url, os.path.join(cachelocation, subentry))
            except Exception as ex:
                print("[BUILD][WARNING] Failed to download " + name)
                return
            
        try:
            if se_extract == True:
                if subentry.split(".")[-1] == "zip":
                    mcore.extractzipfile(os.path.join(cachelocation, subentry), target)
                elif subentry.split(".")[-2] == "tar" and subentry.split(".")[-1] == "gz":
                    tarball = tarfile.open(os.path.join(cachelocation, subentry), "r:gz")
                    tarball.extractall(target)
            else:
                shutil.copyfile(os.path.join(cachelocation, subentry), os.path.join(target, subentry))
        except Exception as ex:
            print("[BUILD][WARNING] Failed to copy or extract " + name)
            return
        
        '''if se_hasconfig == True:
            if os.path.isfile(os.path.join(cachelocation, subentry) + ".config.zip"):
                mcore.extractzipfile(os.path.join(cachelocation, subentry) + ".config.zip", os.path.join(".", "etmp"))#os.path.join(target, se_configdir))
                cache_copydir_macro(os.path.join(".", "etmp"), os.path.join(target, se_configdir), packconfig)
                try:
                    shutil.rmtree(os.path.join(".", "etmp"))
                except:
                    pass
            elif os.path.isdir(os.path.join(cachelocation, subentry) + ".config"):
                cache_copydir_macro(os.path.join(cachelocation, subentry) + ".config", os.path.join(target, se_configdir), packconfig)'''

    InstalledMods.add(name + "," + version)

    if str(entryconfig.getvalue("HasConfigs")).lower() == "true":
        print("[BUILD] " + name + " has configs.")
        if os.path.isfile(os.path.join(cachelocation, "config.zip")):
            print("[BUILD] Installing zipfile configs.")
            print("[BUILD] " + os.path.join(target, entryconfig.getvalue("ConfigInstallDir")))
            mcore.extractzipfile(os.path.join(cachelocation, "config.zip"), os.path.join(".", "etmp"))
            cache_copydir_macro(os.path.join(".", "etmp"), os.path.join(configroot, entryconfig.getvalue("ConfigInstallDir")), packconfig)
            try:
                shutil.rmtree(os.path.join(".", "etmp"))
            except:
                pass
        elif os.path.isfile(os.path.join(cachelocation, "config.tar.gz")):
            print("[BUILD] Installing tarball configs.")
            tarball = tarfile.open(os.path.join(cachelocation, "config.tar.gz"), "r:gz")
            tarball.extractall(os.path.join(".", "etmp"))
            cache_copydir_macro(os.path.join(".", "etmp"), os.path.join(configroot, entryconfig.getvalue("ConfigInstallDir")), packconfig)
            try:
                shutil.rmtree(os.path.join(".", "etmp"))
            except:
                pass
        elif os.path.isdir(os.path.join(cachelocation, "config")):
            print("[BUILD] Installing directory configs.")
            cache_copydir_macro(os.path.join(cachelocation, "config"), os.path.join(configroot, entryconfig.getvalue("ConfigInstallDir")), packconfig)

    if not entryconfig.getvalue("Dependencies") == "None":
        if not InstalledMods.getlist() == []:
            for dependency in entryconfig.getvalues("Dependencies"):
                if not dependency in InstalledMods.getlist():
                    cache_get(dependency.split(",")[0], dependency.split(",")[1], target, packconfig, configroot)
                    InstalledMods.add(dependency)
        else:
            for dependency in entryconfig.getvalues("Dependencies"):
                cache_get(dependency.split(",")[0], dependency.split(",")[1], target, packconfig, configroot)
                InstalledMods.add(dependency)
