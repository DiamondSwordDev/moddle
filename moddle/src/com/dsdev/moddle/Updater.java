
package com.dsdev.moddle;

import com.dsdev.moddle.util.Logger;
import com.dsdev.moddle.util.Util;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Diamond Sword Development
 */
public class Updater {
    
    public static int DialogResult = -1;
    
    
    public static void checkForUpdates(boolean verbose) {
        
        Logger.info("Updater.checkForUpdates", "Starting update check...");
        
        //Show progress
        if (verbose) {
            GlobalDialogs.showProgressDialog();
            GlobalDialogs.setProgressCaption("Checking for updates...");
            GlobalDialogs.setProgressIndeterminate(true);
        }
        
        //Load current version info
        Logger.info("Updater.checkForUpdates", "Loading current version info...");
        JSONObject currentVersionConfig;
        try {
            currentVersionConfig = Util.readJSONFile("./update/version.json");
        } catch (IOException ex) {
            Logger.warning("MainForm.startUpdateCheck", "Could not determine the current launcher version!");
            if (verbose) {
                GlobalDialogs.hideProgressDialog();
                GlobalDialogs.showNotification("Update check failed: Could not determine what version of the launcher you are running!");
            }
            return;
        }
        
        //Download the versions file
        Logger.info("Updater.checkForUpdates", "Downloading version index...");
        try {
            FileUtils.copyURLToFile(new URL("https://sites.google.com/site/moddlerepo/versions.json"), new File("./update/dl_versions.json"));
        } catch (IOException ex) {
            Logger.warning("MainForm.startUpdateCheck", "Failed to retrieve version index!");
            if (verbose) {
                GlobalDialogs.hideProgressDialog();
                GlobalDialogs.showNotification("Update check failed: Could not retrieve the remote version index!");
            }
            return;
        }
        
        //Load the versions file
        Logger.info("Updater.checkForUpdates", "Loading version index...");
        JSONObject versionsConfig;
        try {
            versionsConfig = Util.readJSONFile("./update/dl_versions.json");
        } catch (IOException ex) {
            Logger.warning("MainForm.startUpdateCheck", "Failed to load version index file!");
            if (verbose) {
                GlobalDialogs.hideProgressDialog();
                GlobalDialogs.showNotification("Update check failed: Could not load the list of versions!");
            }
            return;
        }
        
        //Hide the progress dialog
        if (verbose) {
            GlobalDialogs.hideProgressDialog();
        }
        
        //Run update for the cache
        String newCacheVersion = checkForLauncherUpdates(verbose, currentVersionConfig.get("cacheversion").toString(), versionsConfig);
        
        //Write to version file
        if (newCacheVersion != null) {
            //Add value to version config
            currentVersionConfig.put("cacheversion", newCacheVersion);
            //Write to file
            try {
                FileUtils.writeStringToFile(new File("./update/version.json"), currentVersionConfig.toJSONString());
            } catch (IOException ex) {
                Logger.warning("Updater.checkForUpdates", "Failed to update 'version.json'!");
                if (verbose) {
                    GlobalDialogs.hideProgressDialog();
                    GlobalDialogs.showNotification("Failed to update current version info! (Things could break!)");
                }
            }
        }
        
        //Run update for launcher
        checkForLauncherUpdates(verbose, currentVersionConfig.get("moddleversion").toString(), versionsConfig);
        
        //Give finish message
        if (verbose) {
            GlobalDialogs.showNotification("Update check is finished!");
        }
        
    }
    
    
    public static String checkForBootstrapperUpdates(boolean verbose, String currentVersion, JSONObject versionsConfig) {
        
        if (versionIsUpToDate(currentVersion, versionsConfig.get("latestmupdate").toString())) {
            
            //Jump out if the launcher is up to date
            Logger.info("Updater.checkForBootstrapperUpdates", "MUpdate is up to date!");
            return null;
            
        } else {
            
            Logger.info("Updater.checkForBootstrapperUpdates", "Updates were found!");
            
            //Init message variable
            String message = "";
            
            //Init update queue stack
            Stack<String> updateQueue = new Stack();
            
            //Build a list of versions to install and the update messages
            for (Object updateObj : (JSONArray)versionsConfig.get("mupdateversions")) {
                JSONObject update = (JSONObject)updateObj;
                if (update.get("name").toString().equals(currentVersion)) {
                    //Break out of loop if the current version is reached
                    break;
                } else {
                    //Add to queue
                    updateQueue.push(update.get("name").toString());
                    
                    //Append to message
                    message += "Version " + update.get("name").toString() + ":\n";
                    for (Object messagesObj : (JSONArray)update.get("messages")) {
                        message += messagesObj.toString() + "\n";
                    }
                    message += "\n";
                }
            }
            
            //Show the update dialog
            GlobalDialogs.showUpdateNotification(message);
            
            //Wait for the dialog result
            while (DialogResult == -1);
            int updateDialogResult = DialogResult;
            DialogResult = -1;
            
            if (updateDialogResult == 1) {
                
                //Update!
                if (doBootstrapperUpdate(updateQueue, versionsConfig)) {
                    return versionsConfig.get("latestmupdate").toString();
                } else {
                    return null;
                }
                
            } else {
                return null;
            }
        }
    }
    
    public static boolean doBootstrapperUpdate(Stack<String> updateQueue, JSONObject versionsConfig) {
        
        Logger.info("Updater.doBootstrapperUpdate", "Starting update...");
        
        //Show progress
        GlobalDialogs.showProgressDialog();
        GlobalDialogs.setProgressCaption("Cleaning...");
        GlobalDialogs.setProgressIndeterminate(true);
        
        //Clean patch directory
        Logger.info("Updater.doBootstrapperUpdate", "Cleaning patch directory...");
        try {
            FileUtils.deleteDirectory(new File("./update/mupdatepatch"));
        } catch (IOException ex) {
            Logger.error("Updater.doBootstrapperUpdate", "Failed to clean the patch directory!", false, ex.getMessage());
            GlobalDialogs.hideProgressDialog();
            GlobalDialogs.showNotification("Failed to clean the patch directory!");
            return false;
        }
        
        //Download and extract patches
        while (!updateQueue.empty()) {
            //Load update from JSON
            String updateName = updateQueue.pop();
            JSONObject update = getVersionByName(updateName, versionsConfig);
            
            //Update status
            GlobalDialogs.setProgressCaption("Getting patch '" + updateName + "'...");

            //Download version patch
            Logger.info("Updater.doBootstrapperUpdate", "Downloading patch '" + updateName + "'...");
            try {
                FileUtils.copyURLToFile(new URL(update.get("patch").toString()), new File("./update/mupdate-" + updateName + "-patch.zip"));
            } catch (IOException ex) {
                Logger.error("Updater.doBootstrapperUpdate", "Failed to download patch!", false, ex.getMessage());
                GlobalDialogs.hideProgressDialog();
                GlobalDialogs.showNotification("Failed to download patch '" + updateName + "'!");
                return false;
            }

            //Extract version patch
            Logger.info("Updater.doUpdate", "Extracting patch '" + updateName + "'...");
            try {
                Util.decompressZipfile("./update/mupdate-" + updateName + "-patch.zip", "./update/mupdatepatch");
            } catch (ZipException ex) {
                Logger.error("Updater.doBootstrapperUpdate", "Failed to extract patch!", false, ex.getMessage());
                GlobalDialogs.hideProgressDialog();
                GlobalDialogs.showNotification("Failed to extract patch '" + updateName + "'!");
                return false;
            }
        }
        
        //Update status
        GlobalDialogs.setProgressCaption("Applying patches...");
        
        //Apply patch
        Logger.info("Updater.doBootstrapperUpdate", "Applying patch directory...");
        try {
            FileUtils.copyDirectory(new File("./update/mupdatepatch"), new File("./update"));
        } catch (IOException ex) {
            Logger.error("Updater.doBootstrapperUpdate", "Failed to apply patch!", false, ex.getMessage());
            GlobalDialogs.hideProgressDialog();
            GlobalDialogs.showNotification("Failed to apply MUpdate patch!");
            return false;
        }
        
        //Hide progress window
        GlobalDialogs.hideProgressDialog();
        
        return true;
    }
    
    
    public static String checkForCacheUpdates(boolean verbose, String currentVersion, JSONObject versionsConfig) {
        
        if (versionIsUpToDate(currentVersion, versionsConfig.get("latestcache").toString())) {
            
            //Jump out if the launcher is up to date
            Logger.info("Updater.checkForCacheUpdates", "Cache is up to date!");
            return null;
            
        } else {
            
            Logger.info("Updater.checkForCacheUpdates", "Updates were found!");
            
            //Init message variable
            String message = "";
            
            //Init update queue stack
            Stack<String> updateQueue = new Stack();
            
            //Build a list of versions to install and the update messages
            for (Object updateObj : (JSONArray)versionsConfig.get("cacheversions")) {
                JSONObject update = (JSONObject)updateObj;
                if (update.get("name").toString().equals(currentVersion)) {
                    //Break out of loop if the current version is reached
                    break;
                } else {
                    //Add to queue
                    updateQueue.push(update.get("name").toString());
                    
                    //Append to message
                    message += "Update " + update.get("name").toString() + ":\n";
                    for (Object messagesObj : (JSONArray)update.get("messages")) {
                        message += messagesObj.toString() + "\n";
                    }
                    message += "\n";
                }
            }
            
            //Show the update dialog
            GlobalDialogs.showUpdateNotification(message);
            
            //Wait for the dialog result
            while (DialogResult == -1);
            int updateDialogResult = DialogResult;
            DialogResult = -1;
            
            if (updateDialogResult == 1) {
                
                //Update!
                if (doCacheUpdate(updateQueue, versionsConfig)) {
                    return versionsConfig.get("latestcache").toString();
                } else {
                    return null;
                }
                
            } else {
                return null;
            }
        }
    }
    
    public static boolean doCacheUpdate(Stack<String> updateQueue, JSONObject versionsConfig) {
        
        Logger.info("Updater.doCacheUpdate", "Starting update...");
        
        //Show progress
        GlobalDialogs.showProgressDialog();
        GlobalDialogs.setProgressCaption("Cleaning...");
        GlobalDialogs.setProgressIndeterminate(true);
        
        //Clean patch directory
        Logger.info("Updater.doCacheUpdate", "Cleaning patch directory...");
        try {
            FileUtils.deleteDirectory(new File("./update/cachepatch"));
        } catch (IOException ex) {
            Logger.error("Updater.doCacheUpdate", "Failed to clean the patch directory!", false, ex.getMessage());
            GlobalDialogs.hideProgressDialog();
            GlobalDialogs.showNotification("Failed to clean the patch directory!");
            return false;
        }
        
        //Download and extract patches
        while (!updateQueue.empty()) {
            //Load update from JSON
            String updateName = updateQueue.pop();
            JSONObject update = getVersionByName(updateName, versionsConfig);
            
            //Update status
            GlobalDialogs.setProgressCaption("Getting patch '" + updateName + "'...");

            //Download version patch
            Logger.info("Updater.doCacheUpdate", "Downloading patch '" + updateName + "'...");
            try {
                FileUtils.copyURLToFile(new URL(update.get("patch").toString()), new File("./update/cache-" + updateName + "-patch.zip"));
            } catch (IOException ex) {
                Logger.error("Updater.doCacheUpdate", "Failed to download patch!", false, ex.getMessage());
                GlobalDialogs.hideProgressDialog();
                GlobalDialogs.showNotification("Failed to download patch '" + updateName + "'!");
                return false;
            }

            //Extract version patch
            Logger.info("Updater.doUpdate", "Extracting patch '" + updateName + "'...");
            try {
                Util.decompressZipfile("./update/cache-" + updateName + "-patch.zip", "./update/cachepatch");
            } catch (ZipException ex) {
                Logger.error("Updater.doCacheUpdate", "Failed to extract patch!", false, ex.getMessage());
                GlobalDialogs.hideProgressDialog();
                GlobalDialogs.showNotification("Failed to extract patch '" + updateName + "'!");
                return false;
            }
        }
        
        //Update status
        GlobalDialogs.setProgressCaption("Applying patches...");
        
        //Apply patch
        Logger.info("Updater.doCacheUpdate", "Applying patch directory...");
        for (File f : new File("./update/cachepatch").listFiles()) {
            if (f.isFile() && f.getName().contains(".zip")) {
                
                Logger.info("Updater.doCacheUpdate", "Applying entry '" + f.getName().replace(".zip", ""));
                
                //Delete extracted archive if necessary
                if (new File("./cache", f.getName().replace(".zip", "")).isDirectory()) {
                    try {
                        FileUtils.deleteDirectory(new File("./cache", f.getName().replace(".zip", "")));
                    } catch (IOException ex) {
                        Logger.error("Updater.doCacheUpdate", "Failed to delete existing extracted entry!", false, ex.getMessage());
                    }
                }
                
                //Copy entry
                try {
                    FileUtils.copyFile(f, new File("./cache", f.getName()));
                } catch (IOException ex) {
                    Logger.error("Updater.doCacheUpdate", "Failed to copy patch!", false, ex.getMessage());
                    continue;
                }
            }
        }
        
        //Hide progress window
        GlobalDialogs.hideProgressDialog();
        
        return true;
    }
    
    
    public static String checkForLauncherUpdates(boolean verbose, String currentVersion, JSONObject versionsConfig) {
        
        if (versionIsUpToDate(currentVersion, versionsConfig.get("latestmoddle").toString())) {
            
            //Jump out if the launcher is up to date
            Logger.info("Updater.checkForLauncherUpdates", "Launcher is up to date!");
            return null;
            
        } else {
            
            Logger.info("Updater.checkForLauncherUpdates", "Updates were found!");
            
            //Init message variable
            String message = "";
            
            //Init update queue stack
            Stack<String> updateQueue = new Stack();
            
            //Build a list of versions to install and the update messages
            for (Object updateObj : (JSONArray)versionsConfig.get("moddleversions")) {
                JSONObject update = (JSONObject)updateObj;
                if (update.get("name").toString().equals(currentVersion)) {
                    //Break out of loop if the current version is reached
                    break;
                } else {
                    //Add to queue
                    updateQueue.push(update.get("name").toString());
                    
                    //Append to message
                    message += "Version " + update.get("name").toString() + ":\n";
                    for (Object messagesObj : (JSONArray)update.get("messages")) {
                        message += messagesObj.toString() + "\n";
                    }
                    message += "\n";
                }
            }
            
            //Show the update dialog
            GlobalDialogs.showUpdateNotification(message);
            
            //Wait for the dialog result
            while (DialogResult == -1);
            int updateDialogResult = DialogResult;
            DialogResult = -1;
            
            if (updateDialogResult == 1) {
                
                //Update!
                if (doLauncherUpdate(updateQueue, versionsConfig)) {
                    return versionsConfig.get("latestmoddle").toString();
                } else {
                    return null;
                }
                
            } else {
                return null;
            }
        }
    }
    
    public static boolean doLauncherUpdate(Stack<String> updateQueue, JSONObject versionsConfig) {
        
        Logger.info("Updater.doLauncherUpdate", "Starting update...");
        
        //Show progress
        GlobalDialogs.showProgressDialog();
        GlobalDialogs.setProgressCaption("Cleaning...");
        GlobalDialogs.setProgressIndeterminate(true);
        
        //Clean patch directory
        Logger.info("Updater.doLauncherUpdate", "Cleaning patch directory...");
        try {
            FileUtils.deleteDirectory(new File("./update/launcherpatch"));
        } catch (IOException ex) {
            Logger.error("Updater.doLauncherUpdate", "Failed to clean the patch directory!", false, ex.getMessage());
            GlobalDialogs.hideProgressDialog();
            GlobalDialogs.showNotification("Failed to clean the patch directory!");
            return false;
        }
        
        //Download and extract patches
        while (!updateQueue.empty()) {
            //Load update from JSON
            String updateName = updateQueue.pop();
            JSONObject update = getVersionByName(updateName, versionsConfig);
            
            //Update status
            GlobalDialogs.setProgressCaption("Getting patch '" + updateName + "'...");

            //Download version patch
            Logger.info("Updater.doLauncherUpdate", "Downloading patch '" + updateName + "'...");
            try {
                FileUtils.copyURLToFile(new URL(update.get("patch").toString()), new File("./update/moddle-" + updateName + "-patch.zip"));
            } catch (IOException ex) {
                Logger.error("Updater.doLauncherUpdate", "Failed to download patch!", false, ex.getMessage());
                GlobalDialogs.hideProgressDialog();
                GlobalDialogs.showNotification("Failed to download patch '" + updateName + "'!");
                return false;
            }

            //Extract version patch
            Logger.info("Updater.doLauncherUpdate", "Extracting patch '" + updateName + "'...");
            try {
                Util.decompressZipfile("./update/moddle-" + updateName + "-patch.zip", "./update/launcherpatch");
            } catch (ZipException ex) {
                Logger.error("Updater.doLauncherUpdate", "Failed to extract patch!", false, ex.getMessage());
                GlobalDialogs.hideProgressDialog();
                GlobalDialogs.showNotification("Failed to extract patch '" + updateName + "'!");
                return false;
            }
        }
        
        //Clean patch directory
        Logger.info("Updater.doLauncherUpdate", "Writing new version file...");
        try {
            FileUtils.writeStringToFile(new File("./update/nversion.json"), "{\"version\":\"" + versionsConfig.get("latestmoddle").toString() + "\"}");
        } catch (IOException ex) {
            Logger.error("Updater.doLauncherUpdate", "Failed to write to 'nversion.json'!", false, ex.getMessage());
            GlobalDialogs.hideProgressDialog();
            GlobalDialogs.showNotification("Failed to write to new version file!");
            return false;
        }
        
        //Update status
        GlobalDialogs.setProgressCaption("Restarting...");
        
        //Start MUpdate
        ProcessBuilder updater = new ProcessBuilder(new String[] { "javaw.exe", "-jar", "./update/MUpdate.jar" });
        updater.directory(new File("./update"));
        try {
            updater.start();
        } catch (Exception ex) {
            Logger.error("Updater.doLauncherUpdate", "Failed to start MUpdate!", false, ex.getMessage());
            GlobalDialogs.hideProgressDialog();
            GlobalDialogs.showNotification("Could not start MUpdate!");
            return false;
        }
        
        //Exit Moddle
        GlobalDialogs.hideProgressDialog();
        System.exit(0);
        
        return true;
    }
    
    
    public static boolean versionIsUpToDate(String oldVersion, String newVersion) {
        //Get the list of version numbers for the current version
        List<Integer> newVersionBreakout = new ArrayList();
        for (String versionNumber : newVersion.split("\\.")) {
            newVersionBreakout.add(Integer.parseInt(versionNumber));
        }
        
        //Get the list of version numbers for the latest version
        List<Integer> oldVersionBreakout = new ArrayList();
        for (String versionNumber : oldVersion.split("\\.")) {
            oldVersionBreakout.add(Integer.parseInt(versionNumber));
        }
        
        //Compare each version number to determine whether the user is up to date or not
        boolean isUpToDate = true;
        int length = Math.max(newVersionBreakout.size(), oldVersionBreakout.size());
        for (int i = 0; i < length; i++) {
            int newDigit = newVersionBreakout.size() - 1 < i ? newVersionBreakout.get(i) : 0;
            int oldDigit = oldVersionBreakout.size() - 1 < i ? oldVersionBreakout.get(i) : 0;
            if (newDigit > oldDigit) {
                isUpToDate = false;
                break;
            }
        }
        
        return isUpToDate;
    }
    
    public static JSONObject getVersionByName(String name, JSONObject versionsConfig) {
        for (Object updateObj : (JSONArray)versionsConfig.get("versions")) {
            JSONObject update = (JSONObject)updateObj;
            if (update.get("name").equals(name)) {
                return update;
            }
        }
        return null;
    }
}
