import string
import os
import shutil

import vnlib
import mcore



def cache_copydir_macro(source, target, config):
    for root, dirs, files in os.walk(source):
        for file in files:
            try:
                os.makedirs(os.path.join(target, root.replace(source, "")))
            except:
                pass
            vnlib.copyfile_macro(os.path.join(root, file), os.path.join(target, root.replace(source, ""), file), config)




def cache_get(name, version, target, packconfig):
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

        if subentry == "Author" or subentry == "HasUnifiedConfigCollection" or subentry == "ConfigInstallDir":
            continue
        
        se_extract = False
        se_url = ""
        se_installdir = ""
        se_hasconfig = False
        se_configdir = ""
        
        for l in entryconfig.getvalues(subentry):
            if l.split("=")[0].lower() == "extract" and l.split("=")[1].lower() == "true":
                se_extract = True
            elif l.split("=")[0].lower() == "url":
                se_url = l.split("=")[1]
            elif l.split("=")[0].lower() == "installdir":
                se_installdir = l.split("=")[1]
            elif l.split("=")[0].lower() == "hasconfig" and l.split("=")[1].lower() == "true":
                se_hasconfig = True
            elif l.split("=")[0].lower() == "configdir" and l.split("=")[1].lower() == "true":
                se_configdir = l.split("=")[1]
                
        if not os.path.isfile(os.path.join(cachelocation, subentry)):
            print("[CACHE] Checking for " + os.path.join(cachelocation, subentry))
            try:
                mcore.downloadfile(se_url, os.path.join(cachelocation, subentry))
            except:
                print("[BUILD][WARNING] Failed to download " + name)
                return
            
        try:
            if se_extract == True:
                if se_installdir.lower() == "root" or se_installdir.lower() == "none":
                    mcore.extractzipfile(os.path.join(cachelocation, subentry), target)
                else:
                    mcore.extractzipfile(os.path.join(cachelocation, subentry), os.path.join(target, se_installdir))
            else:
                if se_installdir.lower() == "root" or se_installdir.lower() == "none":
                    shutil.copyfile(os.path.join(cachelocation, subentry), os.path.join(target, subentry))
                else:
                    shutil.copyfile(os.path.join(cachelocation, subentry), os.path.join(target, se_installdir, subentry))
        except Exception as ex:
            print("[BUILD][WARNING] Failed to copy or extract " + name)
            return
        
        try:
            if se_hasconfig == True:
                if os.path.isfile(os.path.join(cachelocation, subentry) + ".config.zip"):
                    mcore.extractzipfile(os.path.join(cachelocation, subentry) + ".config.zip", os.path.join(".", "etmp"))#os.path.join(target, se_configdir))
                    cache_copydir_macro(os.path.join(".", "etmp"), os.path.join(target, se_configdir), packconfig)
                    try:
                        shutil.rmtree(os.path.join(".", "etmp"))
                    except:
                        pass
                elif os.path.isdir(os.path.join(cachelocation, subentry) + ".config"):
                    cache_copydir_macro(os.path.join(cachelocation, subentry) + ".config", os.path.join(target, se_configdir), packconfig)
        except:
            pass

    if str(entryconfig.getvalue("HasUnifiedConfigCollection")).lower() == "true":
        if os.path.isfile(os.path.join(cachelocation, name) + ".config.zip"):
            mcore.extractzipfile(os.path.join(cachelocation, name) + ".config.zip", os.path.join(".", "etmp"))#os.path.join(target, se_configdir))
            cache_copydir_macro(os.path.join(".", "etmp"), os.path.join(target, entryconfig.getvalue("ConfigInstallDir")), packconfig)
            try:
                shutil.rmtree(os.path.join(".", "etmp"))
            except:
                pass
        elif os.path.isdir(os.path.join(cachelocation, name) + ".config"):
            cache_copydir_macro(os.path.join(cachelocation, name) + ".config", os.path.join(target, entryconfig.getvalue("ConfigInstallDir")), packconfig)
