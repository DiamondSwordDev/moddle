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
        if not filename == None:
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



def downloadfile(url, target):
    d = urllib.request.urlopen(url)
    with open(target, 'b+w') as f:
        f.write(d.read())



if not os.path.isfile(os.path.join(".", "config.vn")):
    print("Downloading MUpdate...")
    try:
        downloadfile("https://sites.google.com/site/moddleframework/mupdate.py?attredirects=0&d=1", os.path.join(".", "mupdate.py"))
    except:
        print("Download failed.  Moving on...")

experimental = False

if os.path.isfile(os.path.join(".", "config.vn")):
    moddleconfig = VictoryNotationFile(os.path.join(".", "config.vn"))
    if moddleconfig.getvalue("Experimental").lower() == "true":
        experimental = True

versiontext = ""

if os.path.isfile(os.path.join(".", "version.vn")):
    versionconfig = VictoryNotationFile(os.path.join(".", "version.vn"))
    versiontext = versionconfig.getvalue("ModdleVersion")
else:
    versiontext = "0"

try:
    if experimental == False:
        downloadfile("https://sites.google.com/site/moddleframework/version_stable.vn?attredirects=0&d=1", os.path.join(".", "latestversion.vn"))
    else:
        downloadfile("https://sites.google.com/site/moddleframework/version_experimental.vn?attredirects=0&d=1", os.path.join(".", "latestversion.vn"))
except:
    print("Failed to check for updates!  Moddle will run anyway...")
    exit(0)

latestversionconfig = VictoryNotationFile(os.path.join(".", "latestversion.vn"))

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
                p = subprocess.Popen(["python", os.path.join(".", "mupdate.py"), "-silent", "-version=" + latestversionconfig.getvalue("ModdleVersion")], shell=False)
            else:
                p = subprocess.Popen(["python", os.path.join(".", "mupdate.py"), "-experimental", "-silent", "-version=" + latestversionconfig.getvalue("ModdleVersion")], shell=False)
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
            p = subprocess.Popen(["python", os.path.join(".", "mupdate.py"), "-silent", "-version=" + latestversionconfig.getvalue("ModdleVersion")], shell=False)
            exit(0)
        else:
            print("Very well then, Moddle will not be installed on your system...")
            input()
            exit(0)


