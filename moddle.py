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

import vnlib
import mcore


def checkupdates():
    
    if not os.path.isfile(os.path.join(".", "config.vn")):
        print("Downloading MUpdate...")
        try:
            mcore.downloadfile("https://sites.google.com/site/moddleframework/mupdate.py?attredirects=0&d=1", os.path.join(".", "mupdate.py"))
        except:
            print("Download failed.  Moving on...")

    experimental = False

    if os.path.isfile(os.path.join(".", "config.vn")):
        moddleconfig = vnlib.VictoryNotationFile(os.path.join(".", "config.vn"))
        if moddleconfig.getvalue("Experimental") == "true":
            experimental = True

    versiontext = ""
    
    if os.path.isfile(os.path.join(".", "version.vn")):
        versionconfig = vnlib.VictoryNotationFile(os.path.join(".", "version.vn"))
        versiontext = versionconfig.getvalue("ModdleVersion")
    else:
        versiontext = "0"

    try:
        if experimental == False:
            mcore.downloadfile("https://sites.google.com/site/moddleframework/version_stable.vn?attredirects=0&d=1", os.path.join(".", "latestversion.vn"))
        else:
            mcore.downloadfile("https://sites.google.com/site/moddleframework/version_experimental.vn?attredirects=0&d=1", os.path.join(".", "latestversion.vn"))
    except:
        print("Failed to check for updates!  Moddle will run anyway...")
        return

    latestversionconfig = vnlib.VictoryNotationFile(os.path.join(".", "latestversion.vn"))
    
    if not latestversionconfig.getvalue("ModdleVersion") == versiontext:

        if os.path.isdir(os.path.join(".", "cache")):
            print("-----------------------------------------------------")
            print("MODDLE VERSION " + latestversionconfig.getvalue("ModdleVersion") + " IS NOW AVAILABLE!")
            print("-----------------------------------------------------")
            print("")
            print("Do you want to update right now? (y/n)")
            yesno = input()
            if yesno == "y" or yesno == "yes":
                if experimental == False:
                    p = subprocess.Popen(["python", os.path.join(".", "mupdate.py")], shell=False)
                else:
                    p = subprocess.Popen(["python", os.path.join(".", "mupdate.py")], shell=False)
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
                p = subprocess.Popen(["python", os.path.join(".", "mupdate.py")], shell=False)
                exit(0)
            else:
                print("Very well then, Moddle will not be installed on your system...")
                input()
                exit(0)



def checkassets():
    
    if not os.path.isdir(os.path.join(".", "cache", "__MinecraftAssets")):
        print("----------------------------------------------------------------------------")
        print("My good player, it appears that you have not installed your Minecraft assets into Moddle yet.  Please, allow me to install them for you.  This could take a bit of time.")
        print("Please enter the path to your \".minecraft\" directory (it must be unmodified for this to work and will not be modified in this process):")
        dotminecraft = input()
        os.makedirs(os.path.join(".", "cache", "__MinecraftAssets", "1.6.4", "atmp"))
        assetdir = os.path.join(dotminecraft, "assets", "virtual", "legacy")
        assetdir = assetdir.replace(ppath("//"), ppath("/"))
        print("Copying...")
        shutil.copytree(assetdir, os.path.join(".", "cache", "__MinecraftAssets", "1.6.4", "atmp", "assets"))
        print("Compressing...")
        mcore.compresszipfile(os.path.join(".", "cache", "__MinecraftAssets", "1.6.4", "atmp"), os.path.join(".", "cache", "__MinecraftAssets", "1.6.4", "__MinecraftAssets.zip"))
        print("Cleaning up...")
        shutil.rmtree(os.path.join(".", "cache", "__MinecraftAssets", "1.6.4", "atmp"))
        with open(os.path.join(".", "cache", "__MinecraftAssets", "1.6.4", "filetype.vn"), "w") as f:
            f.write("FileType=.zip")
        print("Done.")
        print("----------------------------------------------------------------------------")



def getsession(uname, pword):
    
    username = uname
    sessionid = "Bad login"
    
    mcore.downloadfile("http://login.minecraft.net/?user=" + uname + "&password=" + pword + "&version=12", os.path.join(".", "login.tmp"))
    for lines in open(os.path.join(".", "login.tmp"), "r"):
        if lines[0] == "Bad login":
            return "Bad login"
        username = lines.split(":")[2]
        sessionid = lines.split(":")[3]
    os.remove(os.path.join(".", "login.tmp"))
    return [username, sessionid]



def invokebuild(modpack, server):
    
    if not os.path.isfile(os.path.join(".", "packs", modpack + ".zip")):
        print("[PREBUILD][WARNING] Pack does not exist!")
        input()
        exit(0)

    if os.path.isdir(os.path.join(".", "ext")):
        print("[PREBUILD] Cleaning extraction directory...")
        shutil.rmtree(os.path.join(".", "ext") + "/")
        os.mkdir(os.path.join(".", "ext"))
    else:
        print("[PREBUILD] Creating extraction directory...")
        os.mkdir(os.path.join(".", "ext"))

    print("[PREBUILD] Extracting pack...")
    mcore.extractzipfile(os.path.join(".", "packs", modpack + ".zip"), os.path.join(".", "ext"))

    print("[PREBUILD] Loading pack config...")
    packconfig = vnlib.VictoryNotationFile(os.path.join(".", "ext", "config.vn"))

    if server == True:
        if os.path.isfile(os.path.join(".", "packs", modpack + "_server", "inst_complete")):
            if forceupdate == True:
                builder = imp.load_source(packconfig.getvalue("PackType").lower(), os.path.join(".", packconfig.getvalue("PackType").lower() + ".py"))
                builder.buildserver(modpack, packconfig)
        else:
            builder = imp.load_source(packconfig.getvalue("PackType").lower(), os.path.join(".", packconfig.getvalue("PackType").lower() + ".py"))
            builder.buildserver(modpack, packconfig)
    else:
        if os.path.isfile(os.path.join(".", "packs", modpack, "inst_complete")):
            if forceupdate == True:
                builder = imp.load_source(packconfig.getvalue("PackType").lower(), os.path.join(".", packconfig.getvalue("PackType").lower() + ".py"))
                builder.build(modpack, packconfig)
        else:
            builder = imp.load_source(packconfig.getvalue("PackType").lower(), os.path.join(".", packconfig.getvalue("PackType").lower() + ".py"))
            builder.build(modpack, packconfig)

    print("[POSTBUILD] Cleaning extraction directory...")
    shutil.rmtree(os.path.join(".", "ext"))

    return packconfig



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
            checkupdates()
        checkassets()    

        
        if platform.system() == "Windows" and nogui == False:
            
            p = subprocess.Popen([os.path.join(".", "ModdleFrontend.exe")], shell=False)
            
        else:

            if not platform.system() == "Windows":
                print("")
                print("")
                print("###########################################################")
                print("######## Moddle Integrated Minecraft Launcher TUI #########")
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
                if os.path.isfile(os.path.join(".", "login.vn")):
                    loginconfig = vnlib.VictoryNotationFile(os.path.join(".", "login.vn"))
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
                logindata = getsession(username, password)
                if logindata == "Bad login":
                    print("[LOGIN] Bad login!  Running in offline mode...")
                    username = "Player"
                    sessionid = "null"
                else:
                    username = logindata[0]
                    sessionid = logindata[1]
                    print("[LOGIN] Session ID " + sessionid + " generated.")


            print("[PREBUILD] Parsing config.vn...")
            moddleconfig = vnlib.VictoryNotationFile(os.path.join(".", "config.vn"))
            if javapath == "":
                javapath = moddleconfig.getvalue("JavaPath")
            if memory == "":
                memory = moddleconfig.getvalue("Memory")

            print("[PREBUILD] Starting modpack builder...")
            packconfig = invokebuild(modpack, runserver)

            '''if runserver == True:
                print("[LAUNCH] Copying launch template...")
                with open (os.path.join(".", "packs", modpack + "_server", "start_template.bat"), "r") as f:
                    data=f.read()
                    
                print("[LAUNCH] Generating launch script...")
                data = data.replace("{JAVAPATH}", javapath.replace("\n", ""))
                data = data.replace("{MEMORY}", memory.replace("\n", ""))
                data = data.replace("{MODPACK}", modpack)
                data = data.replace("{APPDATA}", os.path.join(os.getcwd, "packs", modpack + "_server"))
                data = data.replace("{GAMEDIR}", os.path.join(os.getcwd(), "packs", modpack + "_server", ".minecraft"))
                data = data.replace("{VERSION}", packconfig.getvalue("MinecraftVersion"))
                data = data.replace("{PACKTYPE}", packconfig.getvalue("PackType"))
                data = data.replace("{FORGEVERSION}", packconfig.getvalue("ForgeVersion"))
                with open (os.path.join(".", "packs", modpack + "_server", "start.bat"), "w") as f:
                    f.write(data)
                    
                print("[LAUNCH] Starting Minecraft...")
                p = subprocess.Popen([os.path.join(".", "packs", modpack + "_server", "start.bat")], shell=False)
            else:
                print("[LAUNCH] Copying launch template...")
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
            
            usernameval = vnlib.VictoryNotationValue()
            usernameval.name = "Username"
            usernameval.values.append(username)
            fullconfig.values.append(usernameval)

            sessionval = vnlib.VictoryNotationValue()
            sessionval.name = "SessionID"
            sessionval.values.append(sessionid)
            fullconfig.values.append(sessionval)

            modpackval = vnlib.VictoryNotationValue()
            modpackval.name = "Modpack"
            modpackval.values.append(modpack)
            fullconfig.values.append(modpackval)

            memoryval = vnlib.VictoryNotationValue()
            memoryval.name = "Memory"
            memoryval.values.append(memory)
            fullconfig.values.append(memoryval)

            javapathval = vnlib.VictoryNotationValue()
            javapathval.name = "JavaPath"
            javapathval.values.append(javapath)
            fullconfig.values.append(javapathval)

            appdataval = vnlib.VictoryNotationValue()
            appdataval.name = "AppData"
            appdataval.values.append(os.path.join(os.getcwd() + "\\packs\\" + modpack + "\\"))
            fullconfig.values.append(appdataval)
            
            vnlib.copyfile_macro(os.path.join(".", "packs", modpack, "start_template.bat"), os.path.join(".", "packs", modpack, "start.bat"), packconfig)
                
            print("[LAUNCH] Starting Minecraft...")
            p = subprocess.Popen([os.path.join(".", "packs", modpack, "start.bat")], shell=False)



    except Exception as e:
        type_, value_, traceback_ = sys.exc_info()
        for line in traceback.format_exception(type_, value_, traceback_):
            print(line)
        input()
