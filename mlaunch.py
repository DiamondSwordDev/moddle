import urllib.request
import subprocess
import platform
import zipfile
import shutil
import string
import imp
import sys
import re
import os



class CacheDirectory:

    gamedir = ""
    
    def __init__(self, name):
        self.gamedir = ".\\packs\\" + name + "\\.minecraft\\"

    def getfiletype(self, name, category):
        if os.path.exists(".\\cache\\" + category + "s\\" + name + "\\filetype.dat"):
            f = open(".\\cache\\" + category + "s\\" + name + "\\filetype.dat", "r")
            return f.readline()
        else:
            return False
    
    def contains(self, name, version, category):
        filetype = self.getfiletype(name, category)
        if filetype == False:
            return False
        else:
            return os.path.exists(".\\cache\\" + category + "s\\" + name + "\\" + version + filetype)

    def assertmod(self, name, version, category, filetype, url):
        if not os.path.exists(".\\cache\\" + category + "s\\" + name + "\\" + version + filetype):
            if not os.path.isdir(".\\cache\\" + category + "s\\" + name + "\\"):
                os.mkdir(".\\cache\\" + category + "s\\" + name + "\\")
            if not os.path.exists(".\\cache\\" + category + "s\\" + name + "\\filetype.dat"):
                f = open(".\\cache\\" + category + "s\\" + name + "\\filetype.dat", "w")
                f.write(filetype)
                f.close()
            if not os.path.exists(".\\cache\\" + category + "s\\" + name + "\\" + version + filetype):
                d = urllib.request.urlopen(url)
                with open(".\\cache\\" + category + "s\\" + name + "\\" + version + filetype, 'b+w') as f:
                    f.write(d.read())

    def add(self, name, version, category, filetype, url):
        if not os.path.isdir(".\\cache\\" + category + "s\\" + name + "\\"):
            os.mkdir(".\\cache\\" + category + "s\\" + name + "\\")
        if not os.path.exists(".\\cache\\" + category + "s\\" + name + "\\filetype.dat"):
            f = open(".\\cache\\" + category + "s\\" + name + "\\filetype.dat", "w")
            f.write(filetype)
            f.close()
        if not os.path.exists(".\\cache\\" + category + "s\\" + name + "\\" + version + filetype):
            d = urllib.request.urlopen(url)
            with open(".\\cache\\" + category + "s\\" + name + "\\" + version + filetype, 'b+w') as f:
                f.write(d.read())

    def remove(self, name, version, category):
        os.remove(".\\cache\\" + category + "s\\" + name + "\\" + version + self.getfiletype(name, category))

    def get(self, name, version, category, target):
        shutil.copyfile(".\\cache\\" + category + "s\\" + name + "\\" + version + self.getfiletype(name, category), self.gamedir + target);
        return self.getfiletype(name, category)


class PackDirectory:
    
    gamedir = ""
    
    def __init__(self, name):
        self.gamedir = ".\\packs\\" + name + "\\.minecraft\\"
        if not os.path.isdir(".\\packs\\" + name + "\\"):
            os.mkdir(".\\packs\\" + name + "\\")
        if not os.path.isdir(self.gamedir):
            os.mkdir(self.gamedir)

    def copyfile(self, source, target):
        shutil.copyfile(self.gamedir + source, self.gamedir + target)

    def deletefile(self, filename):
        os.remove(self.gamedir + filename)

    def isfile(self, path):
        return os.path.isfile(self.gamedir + path)

    def makedir(self, directory):
        os.mkdir(self.gamedir + directory)

    def removedir(self, directory):
        shutil.rmtree(self.gamedir + directory)

    def copydir(self, source, target):
        shutil.copytree(self.gamedir + source, self.gamedir + target)

    def isdir(self, directory):
        return os.path.isdir(directory)

    def exists(self, path):
        return os.path.exists(self.gamedir + path)

    def extractfile__old(self, source, target):
        with zipfile.ZipFile(self.gamedir + source, "r") as z:
            z.extractall(self.gamedir + target)

    def extractfile(self, source, target):
        p = subprocess.Popen([".\\nzippie.exe", "-e", self.gamedir + source, self.gamedir + target], shell=False)
        p.wait()

    def compress__directory(self, directory, zip):
        for root, dirs, files in os.walk(directory):
            for file in files:
                zip.write(os.path.join(directory, file))
            for directory in dirs:
                compress__directory(directory, zip)

    def compressdir__old(self, directory, target):
        zip = zipfile.ZipFile(self.gamedir + target, 'w')
        compress__directory(self.gamedir + directory, zip)
        zip.close()

    def compressdir(self, directory, target):
        p = subprocess.Popen([".\\nzippie.exe", "-c", self.gamedir + target, self.gamedir + directory], shell=False)
        p.wait()

    def patcharchive(self, source, target, name):
        p = subprocess.Popen([".\\nzippie.exe", "-p", self.gamedir + source, self.gamedir + target, name], shell=False)
        p.wait()

    def patchallarchive(self, sourcedir, target):
        p = subprocess.Popen([".\\nzippie.exe", "-pa", self.gamedir + sourcedir, self.gamedir + target], shell=False)
        p.wait()

    def removefromarchive(self, name, archive):
        p = subprocess.Popen([".\\nzippie.exe", "-d", name, self.gamedir + archive], shell=False)
        p.wait()

    def downloadfile(self, address, target):
        d = urllib.request.urlopen(address)
        with open(self.gamedir + target, 'b+w') as f:
            f.write(d.read())


class Modpack:
    
    gamedir = ""

    packdirectory = ""
    cache = ""
    packname = ""
    requiredmodlists = None

    versionname = ""

    def __init__(self, name, reqpacks):
        self.packname = name
        self.requiredmodlists = reqpacks

    def status_clean(self):
        if os.path.exists(".\\packs\\" + self.packname + "\\inst_partial"):
            os.remove(".\\packs\\" + self.packname + "\\inst_partial")
        if os.path.exists(".\\packs\\" + self.packname + "\\inst_complete"):
            os.remove(".\\packs\\" + self.packname + "\\inst_complete")

    def status_setpartial(self):
        self.status_clean()
        with open(".\\packs\\" + self.packname + "\\inst_partial", "w") as f:
            f.write("0")
            f.close()

    def status_setcomplete(self):
        self.status_clean()
        with open(".\\packs\\" + self.packname + "\\inst_complete", "w") as f:
            f.write("0")
            f.close()

    def install_begin(self, version):
        
        if os.path.exists(".\\packs\\" + self.packname + "\\inst_complete"):
            print("[BUILD][WARNING] Pack already exists! Overwrite? (y/n)")
            yesno = input()
            if yesno == "yes" or yesno == "y":
                if os.path.isdir(".\\packs\\" + self.packname + "\\.minecraft\\"):
                    shutil.rmtree(".\\packs\\" + self.packname + "\\.minecraft\\")
            else:
                return False
        elif os.path.exists(".\\packs\\" + self.packname + "\\inst_partial"):
            print("[BUILD][WARNING] Pack is already a partial installation! Overwrite? (y/n)")
            yesno = input()
            if yesno == "yes" or yesno == "y":
                if os.path.isdir(".\\packs\\" + self.packname + "\\.minecraft\\"):
                    shutil.rmtree(".\\packs\\" + self.packname + "\\.minecraft\\")
            else:
                return False

        self.versionname = version
        
        self.gamedir = ".\\packs\\" + self.packname + "\\.minecraft\\"
        self.packdirectory = PackDirectory(self.packname)
        self.cache = CacheDirectory(self.packname)

        self.status_setpartial()

        print("[BUILD] Creating launch script...")

        #TODO: Must fix to be platform-dependant.
        shutil.copyfile(".\\launch\\start.win.bat", ".\\packs\\" + self.packname + "\\start.bat");

        print("[BUILD] Obtaining Minecraft natives...")
        
        self.packdirectory.makedir("temp\\")
        self.packdirectory.makedir("versions\\")
        self.packdirectory.makedir("versions\\" + version + "\\")
        self.packdirectory.makedir("versions\\" + version + "\\" + version + "-natives\\")
        self.cache.get("MinecraftNatives", "1.6.4", "lib", "temp\\natives.zip")
        self.packdirectory.extractfile("temp\\natives.zip", "versions\\" + version + "\\" + version + "-natives\\")

        print("[BUILD] Obtaining Minecraft libraries...")
        
        self.packdirectory.makedir("libraries\\")
        self.cache.get("MinecraftLib", "1.6.4", "lib", "temp\\mclib.zip")
        self.packdirectory.extractfile("temp\\mclib.zip", "libraries\\")

        print("[BUILD] Obtaining Minecraft assets...")
        
        self.packdirectory.makedir("assets\\")
        self.cache.get("MinecraftAssets", "1.6.4", "lib", "temp\\assets.zip")
        self.packdirectory.extractfile("temp\\assets.zip", "assets\\")
        
        print("[BUILD] Getting Minecraft jarfile...")
        
        self.packdirectory.makedir("mcextract\\")
        self.packdirectory.downloadfile("http://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".jar", "versions\\" + version + "\\" + version + ".jar")
        #self.packdirectory.extractfile("versions\\" + version + "\\" + version + ".jar", "mcextract\\")
        
        #self.packdirectory.removedir("mcextract\\META-INF\\")

        print("[BUILD] Removing META-INF...")
        
        self.packdirectory.removefromarchive("META-INF\\MANIFEST.MF", "versions\\" + version + "\\" + version + ".jar")
        self.packdirectory.removefromarchive("META-INF\\MOJANGCS.SF", "versions\\" + version + "\\" + version + ".jar")
        self.packdirectory.removefromarchive("META-INF\\MOJANGCS.RSA", "versions\\" + version + "\\" + version + ".jar")

        print("[BUILD] Preparing for mod installation...")
        
        self.packdirectory.makedir("mods\\")
        self.packdirectory.makedir("coremods\\")

        return True
    

    def install_begin_legacy(self, version):
        
        if os.path.exists(".\\packs\\" + self.packname + "\\inst_complete"):
            print("[BUILD][WARNING] Pack already exists! Overwrite? (y/n)")
            yesno = input()
            if yesno == "yes" or yesno == "y":
                shutil.rmtree(".\\packs\\" + self.packname + "\\.minecraft\\")
            else:
                return False
        elif os.path.exists(".\\packs\\" + self.packname + "\\inst_partial"):
            print("[BUILD][WARNING] Pack is already a partial installation! Overwrite? (y/n)")
            yesno = input()
            if yesno == "yes" or yesno == "y":
                shutil.rmtree(".\\packs\\" + self.packname + "\\.minecraft\\")
            else:
                return False

        self.versionname = version
        
        self.gamedir = ".\\packs\\" + self.packname + "\\.minecraft\\"
        self.packdirectory = PackDirectory(self.packname)
        self.cache = CacheDirectory(self.packname)

        self.status_setpartial()

        print("[BUILD] Creating launch script...")

        #TODO: Must fix to be platform-dependant.
        shutil.copyfile(".\\launch\\start.win.legacy.bat", ".\\packs\\" + self.packname + "\\start.bat");

        print("[BUILD] Obtaining Minecraft dependencies...")
        self.packdirectory.makedir("temp\\")
        self.packdirectory.makedir("bin\\")
        self.cache.get("MinecraftLegacyLib", "1", "lib", "temp\\bin-lib.zip")
        self.packdirectory.extractfile("temp\\bin-lib.zip", "bin\\")
        
        print("[BUILD] Getting Minecraft jarfile...")
        
        self.packdirectory.makedir("mcextract\\")
        self.packdirectory.downloadfile("http://s3.amazonaws.com/Minecraft.Download/versions/" + version + "/" + version + ".jar", "bin\\minecraft.jar")

        print("[BUILD] Removing META-INF...")
        
        self.packdirectory.removefromarchive("META-INF\\MANIFEST.MF", "bin\\minecraft.jar")
        self.packdirectory.removefromarchive("META-INF\\MOJANG_C.SF", "bin\\minecraft.jar")
        self.packdirectory.removefromarchive("META-INF\\MOJANG_C.DSA", "bin\\minecraft.jar")

        print("[BUILD] Preparing for mod installation...")
        
        self.packdirectory.makedir("mods\\")
        self.packdirectory.makedir("coremods\\")

        return True

    def install_finish_legacy(self):
        print("[BUILD] Patching Minecraft jarfile...")
        p = subprocess.Popen([".\\nzippie.exe", "-pa", self.gamedir + "mcextract\\", self.gamedir + "bin\\minecraft.jar"], shell=False)
        p.wait()

        print("[BUILD] Cleaning up...")
        self.packdirectory.removedir("temp\\")
        self.packdirectory.removedir("mcextract\\")

        self.status_setcomplete()

    def install_finish(self):
        print("[BUILD] Compressing Minecraft jarfile...")
        #self.packdirectory.compressdir("mcextract\\", "versions\\" + self.versionname + "\\" + self.versionname + ".jar")
        p = subprocess.Popen([".\\nzippie.exe", "-pa", self.gamedir + "mcextract\\", self.gamedir + "bin\\minecraft.jar"], shell=False)
        p.wait()

        print("[BUILD] Cleaning up...")
        self.packdirectory.removedir("temp\\")
        self.packdirectory.removedir("mcextract\\")

        self.status_setcomplete()

    def get_jarmod(self, name, version):
        print("[BUILD][JARMOD] Installing " + name + ", version " + version)
        filetype = self.cache.getfiletype(name, "jarmod")
        if self.cache.contains(name, version, "jarmod"):
            self.cache.get(name, version, "jarmod", "temp\\" + name + filetype)
        else:
            for modlist in self.requiredmodlists:
                for line in open(".\\cache\\" + modlist + ".dat", "r"):
                    if not line == "" and line.find("|") > -1:
                        if line.split("|")[0] == name + "-" + version:
                            self.cache.assertmod(name, version, "jarmod", line.split("|")[1], line.split("|")[2])
            self.cache.get(name, version, "jarmod", "temp\\" + name + filetype)
        self.packdirectory.extractfile("temp\\" + name + filetype, "mcextract\\")

    def get_lib(self, name, version):
        print("[BUILD][LIB] Installing " + name + ", version " + version)
        filetype = self.cache.getfiletype(name, "lib")
        if self.cache.contains(name, version, "lib"):
            self.cache.get(name, version, "lib", "temp\\" + name + filetype)
        else:
            for modlist in self.requiredmodlists:
                for line in open(".\\cache\\" + modlist + ".dat", "r"):
                    if not line == "" and line.find("|") > -1:
                        if line.split("|")[0] == name + "-" + version:
                            self.cache.add(name, version, "lib", line.split("|")[1], line.split("|")[2])
            self.cache.get(name, version, "lib", "temp\\" + name + filetype)
        self.packdirectory.extractfile("temp\\" + name + filetype, "")

    def get_mod(self, name, version):
        print("[BUILD][MOD] Installing " + name + ", version " + version)
        if self.cache.contains(name, version, "mod"):
            self.cache.get(name, version, "mod", "mods\\" + name)
        else:
            for modlist in self.requiredmodlists:
                for line in open(".\\cache\\" + modlist + ".dat", "r"):
                    if not line == "" and line.find("|") > -1:
                        if line.split("|")[0] == name + "-" + version:
                            self.cache.add(name, version, "mod", line.split("|")[1], line.split("|")[2])
            self.cache.get(name, version, "mod", "mods\\" + name)
            
    def get_coremod(self, name, version):
        print("[BUILD][COREMOD] Installing " + name + ", version " + version)
        if self.cache.contains(name, version, "coremod"):
            self.cache.get(name, version, "coremod", "coremods\\" + name)
        else:
            for modlist in self.requiredmodlists:
                for line in open(".\\cache\\" + modlist + ".dat", "r"):
                    if not line == "" and line.find("|") > -1:
                        if line.split("|")[0] == name + "-" + version:
                            self.cache.add(name, version, "coremod", line.split("|")[1], line.split("|")[2])
            self.cache.get(name, version, "coremod", "coremods\\" + name)






if __name__ == "__main__":
    
    if not os.path.isdir(".\\packs\\"):
        print("No pack directory found! Creating one...")
        os.mkdir(".\\packs\\")
        
    if not os.path.isdir(".\\cache\\"):
        print("No cache directory found! Creating one...")
        os.mkdir(".\\cache\\")
        
    if not os.path.isdir(".\\cache\\mods\\"):
        os.mkdir(".\\cache\\mods\\")
    if not os.path.isdir(".\\cache\\coremods\\"):
        os.mkdir(".\\cache\\coremods\\")
    if not os.path.isdir(".\\cache\\jarmods\\"):
        os.mkdir(".\\cache\\jarmods\\")
    if not os.path.isdir(".\\cache\\configs\\"):
        os.mkdir(".\\cache\\configs\\")
    if not os.path.isdir(".\\cache\\libs\\"):
        os.mkdir(".\\cache\\libs\\")

    #TODO: Download FML and Minecraft Libraries

    #TODO: Download nzippie.exe and ICSharpCode.SharpZipLib.dll

    #TODO: Create config.dat and start scripts


    username = ""
    loginname = ""
    password = ""
    modpack = ""
    forceupdate = False
    memory = ""
    javapath = ""

    if len(sys.argv) > 1:
        for i in range(1, len(sys.argv)):
            if sys.argv[i].split("=")[0] == "-username":
                username = sys.argv[i].split("=")[1]
            elif sys.argv[i].split("=")[0] == "-password":
                password = sys.argv[i].split("=")[1]
            elif sys.argv[i].split("=")[0] == "-loginname":
                loginname = sys.argv[i].split("=")[1]
            elif sys.argv[i].split("=")[0] == "-modpack":
                modpack = sys.argv[i].split("=")[1]
            elif sys.argv[i].split("=")[0] == "-memory":
                memory = sys.argv[i].split("=")[1]
            elif sys.argv[i].split("=")[0] == "-forceupdate":
                forceupdate = sys.argv[i].split("=")[1]
            elif sys.argv[i].split("=")[0] == "-javapath":
                forceupdate = sys.argv[i].split("=")[1]


    print("###########################################################")
    print("#########  Moddle Integrated Minecraft Launcher  ##########")
    print("###########################################################")

    
    if modpack == "":
        print("")
        print("=== Installed Modpacks ===")
        print("")
        for file in os.listdir(".\\packs\\"):
            if not file.find("__") == 0 and not file.find(".py") == -1:
                pack = imp.load_source(file.replace(".py", ""), ".\\packs\\" + file)
                packmeta = pack.META()
                print("-- " + packmeta.NAME + " (" + file.replace(".py", "") + ") --")
                print(packmeta.DESCRIPTION)
                print("(by " + packmeta.AUTHOR + ")")
                print("")
        print("")
        print("Please enter the name of the modpack you wish to play.")
        print("Modpack:")
        modpack = input()
        if modpack.find(":forceupdate") > -1:
            modpack = modpack.split(":")[0]
            forceupdate = True


    if os.path.exists(".\\login.dat"):
        for line in open(".\\login.dat", "r"):
            if line.split("=")[0] == "LOGINNAME":
                if loginname == "":
                    loginname = line.split("=")[1].replace("\n", "")
            if line.split("=")[0] == "USERNAME":
                if username == "":
                    username = line.split("=")[1].replace("\n", "")
            if line.split("=")[0] == "PASSWORD":
                if password == "":
                    password = line.split("=")[1].replace("\n", "")
    else:
        if username == "" and loginname == "" and password == "":
            print("")
            print("Please enter your Minecraft account info.")
            print("")
            print("Username:")
            loginname = input()
            regexp = re.compile(r'\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}\b')
            if regexp.search(loginname) is None:
                print("Minecraft Displayname:")
                username = input()
            else:
                username = loginname
            print("Password:")
            password = input()
            

    print("")
    print("")
    print("[LOGIN] Obtaining session ID...")
    d = urllib.request.urlopen("http://login.minecraft.net/?user=" + loginname + "&password=" + password + "&version=12")
    with open(".\\login.tmp", 'b+w') as f:
        f.write(d.read())
    for lines in open(".\\login.tmp", "r"):
        sessionid = lines.split(":")[3]
    os.remove(".\\login.tmp")
    print("[LOGIN] Session ID " + sessionid + " generated.")

    print("[LOGIN] Starting modpack builder...")
    packversion = ""
    if os.path.exists(".\\packs\\" + modpack + "\\inst_complete"):
        if forceupdate:
            pack = imp.load_source(modpack, ".\\packs\\" + modpack + ".py")
            packversion = pack.build()
    else:
        pack = imp.load_source(modpack, ".\\packs\\" + modpack + ".py")
        packversion = pack.build()


    print("[LAUNCH] Copying launch template...")
    with open (".\\packs\\" + modpack + "\\start.bat", "r") as f:
        data=f.read()
    print("[LAUNCH] Parsing config.dat...")
    with open (".\\config.dat", "r") as f:
        clines=f.readlines()
    for line in clines:
        if line.split("=")[0] == "MEMORY":
            if memory == "":
                memory = line.split("=")[1]
        if line.split("=")[0] == "JAVAPATH":
            if javapath == "":
                javapath = line.split("=")[1]
    print("[LAUNCH] Generating launch script...")
    data = data.replace("{JAVAPATH}", javapath.replace("\n", ""))
    data = data.replace("{MEMORY}", memory.replace("\n", ""))
    data = data.replace("{USERNAME}", username)
    data = data.replace("{SESSIONID}", sessionid)
    data = data.replace("{MODPACK}", modpack)
    data = data.replace("{APPDATA}", os.getcwd() + "\\packs\\" + modpack + "\\")
    data = data.replace("{GAMEDIR}", os.getcwd() + "\\packs\\" + modpack + "\\.minecraft\\")
    packmeta = pack.META()
    data = data.replace("{VERSION}", packmeta.VERSION)
    with open (".\\packs\\" + modpack + "\\start.bat", "w") as f:
        f.write(data)
    print("[LAUNCH] Starting Minecraft...")
    p = subprocess.Popen([".\\packs\\" + modpack + "\\start.bat"], shell=False)
