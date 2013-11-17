import mlaunch

class META:
    NAME = "Minecraft 1.6.4"
    DESCRIPTION = "A test to check compatibility with Minecraft 1.6."
    AUTHOR = "Greenlock28"
    VERSION = "1.6.4"

def build():
    pack = mlaunch.Modpack("16", ["Core"])

    if not pack.install_begin("1.6.4"):
        return

    pack.get_jarmod("Forge", "9.11.0.883")

    #pack.get_lib("ForgeLegacyLib", "7.8.1.738")
    
    #pack.install_finish()

    return "1.6.4"
