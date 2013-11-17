import mlaunch

class META:
    NAME = "Swordcraft"
    DESCRIPTION = "A tech modpack with the best mods available, based upon poular modpacks."
    AUTHOR = "Nathan2055"
    VERSION = "1.5.2"

def build():
    pack = mlaunch.Modpack("sword", ["Core"])

    if not pack.install_begin_legacy("1.5.2"):
        return
    
    pack.get_jarmod("Forge", "7.8.1.738")

    pack.get_lib("ForgeLegacyLib", "7.8.1.738")
    
    '''print("[BUILD][MOD] Installing Buildcraft")
    pack.get_mod("buildcraft-A-3.7.1.jar", "http://bit.ly/16o6GtL")
    
    print("[BUILD][COREMOD] Installing COFH Core")
    pack.get_coremod("CoFHCore-1.5.2.5.jar", "https://dl.dropboxusercontent.com/u/57416963/Minecraft/Mods/CoFHCore/release/CoFHCore-1.5.2.5.jar")
    
    print("[BUILD][MOD] Installing Thermal Expansion")
    pack.get_mod("ThermalExpansion-2.4.6.0.jar", "https://dl.dropboxusercontent.com/u/57416963/Minecraft/Mods/ThermalExpansion/release/ThermalExpansion-2.4.6.0.jar")

    print("[BUILD][MOD] Installing IndustrialCraft2")
    pack.get_mod("industrialcraft-2_1.117.921.jar", "http://141.28.27.36:8080/job/IC2/921/artifact/packages/industrialcraft-2_1.117.921.jar")

    print("[BUILD][MOD] Installing ChargePads")
    pack.get_mod("chargepads-1.5.2-universal-2.7.1.89.jar", "http://www.curseforge.com/media/files/716/178/chargepads-1.5.2-universal-2.7.1.89.jar")

    print("[BUILD][MOD] Installing ModularPowersuits")
    pack.get_mod("ModularPowersuits-0.7.0-534.jar", "http://build.technicpack.net/view/MachineMuse/job/Machine-Muse-Power-Suits/534/artifact/build/dist/ModularPowersuits-0.7.0-534.jar")

    print("[BUILD][MOD] Installing EquivilantExchange 3")
    pack.get_mod("ee3-universal-pre1h-16.jar", "https://dl.dropboxusercontent.com/u/25591134/EE3/MC%201.5.2/pre1h/ee3-universal-pre1h-16.jar")

    print("[BUILD][MOD] Installing Applied Energistics")
    pack.get_mod("AppliedEnergistics-r13.jar", "http://goo.gl/VZA7Lh")

    print("[BUILD][MOD] Installing Thaumcraft 3")
    pack.get_mod("Thaumcraft3.0.5i.zip", "https://dl.dropboxusercontent.com/u/47135879/Thaumcraft3.0.5i.zip")

    print("[BUILD][MOD] Installing Bibliocraft")
    pack.get_mod("BiblioCraft-v1.3.3.zip", "http://download967.mediafire.com/zhujj6a9ei8g/ymotjf04g3kzyc1/BiblioCraft%5Bv1.3.3%5D.zip")

    print("[BUILD][MOD] Installing Forestry")
    pack.get_mod("forestry-A-2.2.8.4.jar", "http://www.curseforge.com/media/files/718/248/forestry-A-2.2.8.4.jar")

    print("[BUILD][MOD] Installing Binnie Mods")
    pack.get_mod("binnie-mods-1.7.5.jar", "http://download617.mediafire.com/qubx07f6lftg/o7dbvknuhdfae6t/binnie-mods-1.7.5.jar")

    print("[BUILD][MOD] Installing BiblioWoods for Forestry")
    pack.get_mod("BiblioWoods-Forestry-v1.0.zip", "http://download1446.mediafire.com/4eh6em03i4ng/nh8wjjh7x5qj7f8/BiblioWoods%5BForestry%5D%5Bv1.0%5D.zip")

    print("[BUILD][COREMOD] Installing CodeChickenCore")
    pack.get_coremod("CodeChickenCore-0.8.7.3.jar", "http://www.chickenbones.craftsaddle.org/Files/Old_Versions/1.5.2/CodeChickenCore%200.8.7.3.jar")

    print("[BUILD][MOD] Installing ChickenChunks")
    pack.get_mod("ChickenChunks-1.3.2.12.jar", "http://www.chickenbones.craftsaddle.org/Files/Old_Versions/1.5.2/ChickenChunks%201.3.2.12.jar")

    print("[BUILD][MOD] Installing EnderStorage")
    pack.get_mod("EnderStorage-1.4.2.15.jar", "http://www.chickenbones.craftsaddle.org/Files/Old_Versions/1.5.2/EnderStorage%201.4.2.15.jar")

    print("[BUILD][COREMOD] Installing NotEnoughItems")
    pack.get_coremod("NotEnoughItems-1.5.2.27.jar", "http://www.chickenbones.craftsaddle.org/Files/Old_Versions/1.5.2/NotEnoughItems%201.5.2.27.jar")

    print("[BUILD][MOD] Installing Wireless Redstone ChickenBones Edition")
    pack.get_mod("WR-CBE-Core-1.4.jar", "http://www.chickenbones.craftsaddle.org/Files/Old_Versions/1.5.2/WR-CBE%20Core%201.4.jar")

    print("[BUILD][MOD] Installing Wireless Redstone ChickenBones Edition (Logic)")
    pack.get_mod("WR-CBE-Logic-1.4.0.2.jar", "http://www.chickenbones.craftsaddle.org/Files/Old_Versions/1.5.2/WR-CBE%20Logic%201.4.0.2.jar")

    print("[BUILD][MOD] Installing ComputerCraft")
    pack.get_mod("ComputerCraft1.53.zip", "http://download1149.mediafire.com/at32wyay2vug/skwmkw7ybr18rua/ComputerCraft1.53.zip")

    print("[BUILD][MOD] Installing MiscPeripherals")
    pack.get_mod("miscperipherals-3.3f.jar", "https://dl.dropboxusercontent.com/u/861751/Mods/miscperipherals/miscperipherals-3.3f.jar")

    print("[BUILD][MOD] Installing OpenCCSensors")
    pack.get_mod("OpenCCSensors-1.5.2.0.jar", "https://dl.dropboxusercontent.com/u/4295615/OpenCCSensors-1.5.2.0.jar")

    print("[BUILD][MOD] Installing CreeperCollateral")
    pack.get_mod("CreeperCollateral-1.5.2-2.0.3.jar", "https://dl.dropboxusercontent.com/sh/qqgsswnyxh2mq9f/XEMS0hgWlz/CreeperCollateral/1.5.2/CreeperCollateral-1.5.2-2.0.3.jar?dl=1&token_hash=AAGVEWNeMyUs6qCivHtWjSL4s_x15ftKx7yqBEJco8nPAQ")

    print("[BUILD][MOD] Installing DamageIndicators")
    pack.get_mod("1.5.2-DamageIndicators-v2.7.0.1.zip", "https://dl.dropboxusercontent.com/u/74770478/1.5.2%20DamageIndicators%20v2.7.0.1.zip")

    print("[BUILD][MOD] Installing DartCraft")
    pack.get_mod("DartCraft-Beta-0.1.20.jar", "https://dl.dropboxusercontent.com/s/gph0yi2brxmef9d/DartCraft%20Beta%200.1.20.jar?dl=1&token_hash=AAHULAlR8MF616gB6ggeGAzg_pebqlXKQIA1Qsg8AOigkg")

    print("[BUILD][MOD] Installing Factorization")
    pack.get_mod("Factorization-0.8.01.jar", "http://dl.dropbox.com/u/76265666/old/Factorization-0.8.01.jar")

    print("[BUILD][MOD] Installing InventoryTweaks")
    pack.get_mod("inventorytweaks-1.54b.jar", "https://github.com/Kobata/inventory-tweaks/releases/download/1.54b/inventorytweaks-1.54b.jar")

    #print("[BUILD][MOD] Installing IChunUtil")
    #pack.get_mod("iChunUtil1.0.1.zip", "http://www.creeperrepo.net/download/ichun/files%5EiChunUtil1.0.1.zip")

    #print("[BUILD][MOD] Installing GravityGun")
    #pack.get_mod("GravityGun1.5.1.zip", "http://www.creeperrepo.net/download/ichun/files%5EGravityGun1.5.1.zip")

    print("[BUILD][MOD] Installing IronChest")
    pack.get_mod("ironchest-universal-recommended.zip", "http://files.minecraftforge.net/IronChests2/ironchest-universal-recommended.zip")

    print("[BUILD][MOD] Installing LogisticsPipes")
    pack.get_mod("LogisticsPipes-MC1.5.2-0.7.3.dev.666.jar", "http://ci.thezorro266.com/job/LogisticsPipes-Dev/666/artifact/build/dist/LogisticsPipes-MC1.5.2-0.7.3.dev.666.jar")

    print("[BUILD][MOD] Installing MagicBees")
    pack.get_mod("MagicBees-2.1.1.jar", "http://bit.ly/16OrlXB")

    print("[BUILD][COREMOD] Installing denLib")
    pack.get_coremod("denLib-1.5.2-3.0.14_Quickfix.jar", "https://dl.dropboxusercontent.com/sh/qqgsswnyxh2mq9f/-2X6L562kT/denLib/1.5.2/denLib-1.5.2-3.0.14_Quickfix.jar?dl=1&token_hash=AAGVEWNeMyUs6qCivHtWjSL4s_x15ftKx7yqBEJco8nPAQ")

    print("[BUILD][MOD] Installing ForgeMultipart")
    pack.get_mod("ForgeMultipart-universal-1.5.2-1.0.0.149.jar", "http://files.minecraftforge.net/ForgeMultipart/ForgeMultipart-universal-1.5.2-1.0.0.149.jar")

    print("[BUILD][COREMOD] Installing PowerCrystals Core")
    pack.get_coremod("PowerCrystalsCore-1.1.6-107.jar", "http://build.technicpack.net/view/PowerCrystals/job/PowerCrystalsCore/107/artifact/build/dist/PowerCrystalsCore-1.1.6-107.jar")

    print("[BUILD][MOD] Installing MineFactoryReloaded")
    pack.get_mod("MineFactoryReloaded-2.6.4-975.jar", "http://build.technicpack.net/job/MineFactoryReloaded/975/artifact/build/dist/MineFactoryReloaded-2.6.4-975.jar")

    print("[BUILD][MOD] Installing PowerConverters")
    pack.get_mod("PowerConverters-2.3.1-58.jar", "http://build.technicpack.net/job/PowerConverters/58/artifact/build/dist/PowerConverters-2.3.1-58.jar")

    print("[BUILD][MOD] Installing FlatBedrock")
    pack.get_mod("FlatBedrock-1.1.1-32.jar", "http://build.technicpack.net/job/FlatBedrock/32/artifact/build/dist/FlatBedrock-1.1.1-32.jar")

    #print("[BUILD][MOD] Installing Modular Force Field System")
    #pack.get_mod("MFFS_v3.1.0.175.jar", "http://calclavia.com/download.php?name=Modular%20Force%20Field%20System&r=http%3A%2F%2Fcalclavia.com%2Fjenkins%2Fjobs%2FModular+Force+Field+System%2Fbuilds%2F175%2Farchive%2Foutput%2FMFFS_v3.1.0.175.jar")
    #pack.get_mod("MFFS_v3.1.0.175.jar", "http://calclavia.com/download.php?name=Modular%20Force%20Field%20System&r=http%3A%2F%2Fcalclavia.com%2Fjenkins%2Fjobs%2FModular+Force+Field+System%2Fbuilds%2F175%2Farchive%2Foutput%2FMFFS_v3.1.0.175.jar")

    print("[BUILD][MOD] Installing MystCraft")
    pack.get_mod("mystcraft-uni-1.5.2-0.10.5.00.zip", "http://binarymage.com/mystcraft/publish/mystcraft-uni-1.5.2-0.10.5.00.zip")

    print("[BUILD][MOD] Installing Tinkers Construct")
    pack.get_mod("TConstruct_1.5.2_1.4.3.final.jar", "https://dl.dropboxusercontent.com/u/42769935/Tinker_1.5.1/TConstruct_1.5.2_1.4.3.final.jar")

    print("[BUILD][MOD] Installing Natura")
    pack.get_mod("Natura_1.5.2_2.1.5.1.jar", "https://dl.dropboxusercontent.com/u/42769935/Natura_1.5.1/Natura_1.5.2_2.1.5.1.jar")

    print("[BUILD][MOD] Installing NEI Addons")
    pack.get_mod("NEIAddons.jar", "http://bit.ly/13oQPrX")

    print("[BUILD][MOD] Installing NEI Plugins")
    pack.get_mod("NEIPlugins.jar", "http://bit.ly/ZBogcm")

    print("[BUILD][MOD] Installing OmniTools")
    pack.get_mod("OmniTools-3.1.6.0.jar", "https://dl.dropboxusercontent.com/u/57416963/Minecraft/Mods/OmniTools/release/OmniTools-3.1.6.0.jar")

    print("[BUILD][MOD] Installing Obsidiplates")
    pack.get_mod("obsidiplates-1.5.2-universal-1.5.0.13.jar", "http://addons.curse.cursecdn.com/files/712/871/obsidiplates-1.5.2-universal-1.5.0.13.jar")

    print("[BUILD][MOD] Installing OpenPeripheral")
    pack.get_mod("OpenPeripheral-0.1.9.jar", "https://dl.dropboxusercontent.com/u/4295615/OpenPeripheral-0.1.9.jar")

    print("[BUILD][MOD] Installing Plugins for Forestry")
    pack.get_mod("PluginsforForestry-1.5.2-3.0.17.jar", "https://dl.dropboxusercontent.com/sh/qqgsswnyxh2mq9f/yripoi_kl-/PluginsforForestry/1.5.2/PluginsforForestry-1.5.2-3.0.17.jar?dl=1&token_hash=AAGVEWNeMyUs6qCivHtWjSL4s_x15ftKx7yqBEJco8nPAQ")

    print("[BUILD][MOD] Installing RailCraft")
    pack.get_mod("Railcraft_1.5.2-7.3.0.0.jar", "https://dl.dropboxusercontent.com/u/38558957/Minecraft/Railcraft_1.5.2-7.3.0.0.jar")

    print("[BUILD][MOD] Installing Steve's Carts")
    pack.get_mod("StevesCarts2.0.0.a122.zip", "http://adf.ly/1027704/dl.dropbox.com/u/46486053/StevesCarts2.0.0.a122.zip")

    print("[BUILD][MOD] Installing Thaumic Tinkerer")
    pack.get_mod("ThaumicTinkerer.jar", "https://www.sugarsync.com/pf/D9740002_63478821_025274?directDownload=true")

    print("[BUILD][MOD] Installing Twilight Forest")
    pack.get_mod("twilightforest-1.18.2.zip", "https://dl.dropboxusercontent.com/u/38679977/twilightforest-1.18.2.zip")

    print("[BUILD][MOD] Installing Zans Minimap")
    pack.get_mod("ZansMinimap1.5.2.zip", "http://download2065.mediafire.com/wjhbn4ng4xjg/aq5wvbcdr892was/ZansMinimap1.5.2.zip")'''

    pack.install_finish_legacy()

    return "1.5.2"
