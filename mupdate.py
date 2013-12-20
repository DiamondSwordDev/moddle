#!/usr/bin/env python3

import urllib.request
import time
import os

def downloadfile(url, target):
    d = urllib.request.urlopen(url)
    with open(target, 'b+w') as f:
        f.write(d.read())

time.sleep(2)

print("------------------------------------------")
print("------------  Moddle Updater  ------------")
print("------------------------------------------")
print("")
print("")
print("This will update your moddle.py file.")
print("DO YOU WISH TO PROCEED? (y/n)")

yesno = input()
if yesno == "y" or yesno == "yes":
    print("")
    print("")
    print("Downloading...")
    downloadfile("https://dl.dropbox.com/s/41zhdvmz9gmjglf/moddle.py?dl=1", os.path.join(".", "moddle.py"))
    print("Done!  You may now run Moddle as normal.")
    input()
