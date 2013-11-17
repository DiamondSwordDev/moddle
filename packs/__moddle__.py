import os
import sys
import urllib.request
import shutil
import zipfile
import subprocess

from moddle import *

def checkwdir():
    if not sys.argv[1] == None:
        os.chdir(str(sys.argv[1]))


class CacheDirectory:

    gamedir = ""
    
    def __init__(self, name):
        self.gamedir = ".\\" + name + "\\.minecraft\\"
    
    def contains(self, name):
        return os.path.isfile(".\\__cache__\\" + name)

    def add(self, name, source):
        shutil.copyfile(self.gamedir + source, ".\\__cache__\\" + name);

    def remove(self, name):
        os.remove(".\\__cache__\\" + name)

    def get(self, name, target):
        shutil.copyfile(".\\__cache__\\" + name, self.gamedir + target);

class PackDirectory:
    
    gamedir = ""
    
    def __init__(self, name):
        self.gamedir = ".\\" + name + "\\.minecraft\\"
        if not os.path.isdir(".\\" + name + "\\"):
            os.mkdir(".\\" + name + "\\")
        if not os.path.isdir(self.gamedir):
            os.mkdir(self.gamedir)

    def copyfile(self, source, target):
        shutil.copyfile(self.gamedir + source, self.gamedir + target);

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
        p = subprocess.Popen(["..\\nzippie.exe", "extract-zip", self.gamedir + source, self.gamedir + target], shell=False)
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
        p = subprocess.Popen(["..\\nzippie.exe", "compress-zip", self.gamedir + target, self.gamedir + directory], shell=False)
        p.wait()

    def downloadfile(self, address, target):
        d = urllib.request.urlopen(address)
        with open(self.gamedir + target, 'b+w') as f:
            f.write(d.read())


class ForgeModpack:
    
    gamedir = ""

    packdirectory = ""
    cache = ""

    def __init__(self, name):
        if os.path.isdir(".\\" + name + "\\"):
            print("[WARNING] Pack already exists! Overwrite? (y/n)")
            yesno = input()
            if yesno == "yes" or yesno == "y":
                shutil.rmtree(".\\" + name + "\\.minecraft\\")
                self.gamedir = ".\\" + name + "\\.minecraft\\"
                self.packdirectory = PackDirectory(name)
                self.cache = CacheDirectory(name)
            else:
                return
        else:
            self.gamedir = ".\\" + name + "\\.minecraft\\"
            self.packdirectory = PackDirectory(name)
            self.cache = CacheDirectory(name)

    def install_init(self, version):
        self.packdirectory.makedir("temp\\")
        self.packdirectory.makedir("bin\\")
        
        self.cache.get("bin-lib.zip", "temp\\bin-lib.zip")
        self.packdirectory.extractfile("temp\\bin-lib.zip", "bin\\")
        
        self.packdirectory.makedir("mcextract\\")
        self.packdirectory.downloadfile("http://assets.minecraft.net/" + version + "/minecraft.jar", "bin\\minecraft.jar")
        self.packdirectory.extractfile("bin\\minecraft.jar", "mcextract\\")
        
        self.packdirectory.removedir("mcextract\\META-INF\\")
        
        self.packdirectory.makedir("mods\\")
        self.packdirectory.makedir("coremods\\")

    def install_finish(self):
        self.packdirectory.removedir("temp\\")
        self.packdirectory.compressdir("mcextract\\", "bin\\minecraft.jar")
        self.packdirectory.removedir("mcextract\\")

    def get_forge_lib(self):
        self.cache.get("fml-lib.zip", "temp\\fml-lib.zip")
        self.packdirectory.makedir("lib\\")
        self.packdirectory.extractfile("temp\\fml-lib.zip", "lib\\")

    def get_jarmod(self, name, url):
        if (self.cache.contains(name)):
            self.cache.get(name, "temp\\" + name)
        else:
            self.packdirectory.downloadfile(url, "temp\\" + name)
            self.cache.add(name, "temp\\" + name)
        self.packdirectory.extractfile("temp\\" + name, "mcextract\\")

    def get_mod(self, name, url):
        if (self.cache.contains(name)):
            self.cache.get(name, "mods\\" + name)
        else:
            self.packdirectory.downloadfile(url, "mods\\" + name)
            self.cache.add(name, "mods\\" + name)
            
    def get_coremod(self, name, url):
        if (self.cache.contains(name)):
            self.cache.get(name, "coremods\\" + name)
        else:
            self.packdirectory.downloadfile(url, "coremods\\" + name)
            self.cache.add(name, "coremods\\" + name)
