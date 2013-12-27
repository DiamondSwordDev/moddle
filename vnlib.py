import string



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



def copyfile_macro(source, target, config):
    contents = ""
    with open(source, "r") as f:
        contents = f.read()
    newcontents = ""
    skipping = False
    for line in contents.split("\n"):
        if line[:6] == "{$IF} ":
            hasnode = False
            for node in config.getnodes():
                if node == line[6:]:
                    hasnode = True
                else:
                    for value in config.getvalues(node):
                        if value == line[6:]:
                            hasnode = True
            if hasnode == False:
                skipping = True
        elif line[:9] == "{$IFHAS} ":
            hasnode = False
            for node in config.getnodes():
                if node.find(line[9:]) > -1:
                    hasnode = True
                else:
                    for value in config.getvalues(node):
                        if value.find(line[9:]) > -1:
                            hasnode = True
            if hasnode == False:
                skipping = True
        elif line[:11] == "{$IFHASNO} ":
            hasnode = False
            for node in config.getnodes():
                if node.find(line[11:]) > -1:
                    hasnode = True
                else:
                    for value in config.getvalues(node):
                        if value.find(line[11:]) > -1:
                            hasnode = True
            if hasnode == True:
                skipping = True
        elif line[:8] == "{$ENDIF}":
            skipping = False
        else:
            if skipping == False:
                modline = line
                for node in config.getnodes():
                    modline = modline.replace("{" + node + "}", config.getvalue(node))
                newcontents = newcontents + modline + "\n"
    with open(target, "w") as f:
        f.write(newcontents)
