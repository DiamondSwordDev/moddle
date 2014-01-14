# Perspective - Single-method API for fetching the Minecraft assets using the new (1.6 and newer) server system
# Documentation: directory is the destination directory, version is the asset definition to fetch, verbose is an optional argument which activates logging...
# ...and prefix is another optional argument to create a prefix for the logging (required for Moddle)

import json
import os
import urllib.request

def getassets(directory, version, verbose=False, prefix=''):
    # Prefix requrires verbose to be true
    if verbose == False and prefix != '':
        print(prefix + ' It appears you have defined a logging prefix without activating verbose mode for Perspective. Verbose mode will now be activated...')
        verbose = True

    # Verbose printing!
    if verbose == True:
        print(prefix + ' Fetching asset index...')

    # HTTP GET the JSON file with the asset definitions in it and convert it to Unicode
    jsonrequest = urllib.request.Request('https://s3.amazonaws.com/Minecraft.Download/indexes/' + version + '.json')
    jsonrequest.add_header('User-Agent', 'Perspective/0.1')
    response = urllib.request.urlopen(jsonrequest)
    encoding = response.info().get_param('charset', 'utf-8')

    # Convert output to dictionary
    data = json.loads(response.read().decode(encoding))

    # Ensure the destination director exists, if not create it
    if not os.path.isdir(directory):
        os.makedirs(directory)

    # Start parsing stuff
    for name, info in data['objects'].items():

        # More verbose printing!
        if verbose == True:
            print(prefix + ' Downloading ' + name + '...')

        # Get the file's hash and parse it
        filehash = info['hash']
        limithash = filehash[:2]

        # Generate the URL to fetch the asset from
        url = 'http://resources.download.minecraft.net/' + limithash + '/' + filehash

        # Ensure the directory exists and if not, create it
        folder = os.path.dirname(os.path.join(directory, name))
        if not os.path.isdir(folder):
            os.makedirs(folder)

        # Build the request and set the headers
        request = urllib.request.Request(url)
        request.add_header('User-Agent', 'Perspective/0.1')  

        # Download and write the file
        download = urllib.request.urlopen(request)
        with open(os.path.join(directory, name), 'b+w') as f:
            f.write(download.read())

    # Guess what? It's verbose printing.
    if verbose == True:
        print(prefix + ' Asset download complete!')
