import urllib.request
import platform
import zipfile
import string
import os

def downloadfile(url, target):
    d = urllib.request.urlopen(url)
    with open(target, 'b+w') as f:
        f.write(d.read())

def extractzipfile(filename, target):
    with zipfile.ZipFile(filename, 'r') as z:
        for name in z.namelist():
            if name.endswith("/"):
                continue
            if not os.path.isdir(os.path.dirname(os.path.join(target, name))):
                try:
                    os.makedirs(os.path.dirname(os.path.join(target, name)))
                except:
                    pass
            with open(os.path.join(target, name.replace("aux.class", "_aux.class")), "wb") as f:
                f.write(z.read(name))

def compresszipfile(directory, target):
    with zipfile.ZipFile(target, 'w') as z:
        for root, directories, files in os.walk(directory):
            for file in files:
                z.write(os.path.join(root, file), os.path.join(root.replace(directory, ""), file).replace("_aux.class", "aux.class"))

def ppath(path):
    if platform.system() == "Windows":
        return path.replace("/", "\\")
    elif platform.system() == "Linux":
        return path.replace("\\", "/")
    else:
        return path.replace("\\", "/")

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
