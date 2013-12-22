#!/usr/bin/env python3

import urllib.request
import tarfile
import time
import sys
import os

def downloadfile(url, target):
    d = urllib.request.urlopen(url)
    with open(target, 'b+w') as f:
        f.write(d.read())

try:

    time.sleep(2)

    print("")
    print("")
    print("===========================================")
    print("=========         MupDate        ==========")
    print("=========   The Moddle Updater   ==========")
    print("===========================================")
    print("")
    print("")
    print("This will update your Moddle installation.")
    print("THIS COULD BREAK STUFF! DO YOU WISH TO PROCEED? (y/n)")

    yesno = input()
    if yesno == "y" or yesno == "yes":
        print("")
        print("")
        print("Downloading...")
        experimental = False
        if len(sys.argv) > 1:
            for i in range(1, len(sys.argv)):
                if sys.argv[i].split("=")[0] == "-experimental":
                    experimental = True
        if experimental == True:
            downloadfile("https://dl.dropboxusercontent.com/u/242871063/moddle/experimental/moddle.tar.gz?dl=1", os.path.join(".", "moddle.tar.gz"))
        else:
            downloadfile("https://dl.dropboxusercontent.com/u/242871063/moddle/release/moddle.tar.gz?dl=1", os.path.join(".", "moddle.tar.gz"))
        print("Extracting...")
        tarball = tarfile.open(os.path.join(".", "moddle.tar.gz"), "r:gz")
        tarball.extractall(".")
        print("Done!  You may now restart Moddle.")
        input()

except Exception as e:
    type_, value_, traceback_ = sys.exc_info()
    for line in traceback.format_exception(type_, value_, traceback_):
        print(line)
    input()
