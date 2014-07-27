
package com.dsdev.moddle;

import com.dsdev.moddle.util.Logger;
import com.dsdev.moddle.util.Util;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Diamond Sword Development
 */
public class Updater {
    
    public static void checkForUpdates(boolean verbose) {
        
        //Show progress
        if (verbose) {
            GlobalDialogs.showProgressDialog();
            GlobalDialogs.setProgressCaption("Checking for updates...");
            GlobalDialogs.setProgressIndeterminate(true);
        }
        
        //Load current version info
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
        try {
            FileUtils.copyURLToFile(new URL("https://sites.google.com/site/moddlerepo/versions.json"), new File("./update/dl_versions.json"));
        } catch (IOException ex) {
            Logger.warning("MainForm.startUpdateCheck", "Failed to retrieve version index!");
            if (verbose) {
                GlobalDialogs.hideProgressDialog();
                GlobalDialogs.showNotification("Update check failed: Could not retrieve the list of versions!");
            }
            return;
        }

        //Load the versions file
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

        //Get the list of version numbers for the current version
        List<Integer> currentVersionBreakout = new ArrayList();
        for (String versionNumber : currentVersionConfig.get("version").toString().split(".")) {
            currentVersionBreakout.add(Integer.parseInt(versionNumber));
        }
        
        //Get the list of version numbers for the latest version
        List<Integer> remoteVersionBreakout = new ArrayList();
        for (String versionNumber : versionsConfig.get("latestversion").toString().split(".")) {
            remoteVersionBreakout.add(Integer.parseInt(versionNumber));
        }
        
        //Compare each version number to determine whether the user is up to date or not
        boolean isUpToDate = false;
        for (int i = 0; i < currentVersionBreakout.size(); i++) {
            if (remoteVersionBreakout.size() - 1 < i) {
                break;
            } else {
                if (currentVersionBreakout.get(i) > remoteVersionBreakout.get(i)) {
                    isUpToDate = true;
                    break;
                }
            }
        }
        
        //Hide the progress dialog
        if (verbose) {
            GlobalDialogs.hideProgressDialog();
        }
        
        //Display results
        if (isUpToDate) {
            if (verbose) {
                GlobalDialogs.showNotification("No updates were found.  You are most likely completely up-to-date!");
            }
        } else {
            
            //Init message variable
            String message = "";
            
            //Build update message
            for (Object updateObj : (JSONArray)versionsConfig.get("versions")) {
                JSONObject update = (JSONObject)updateObj;

                if (update.get("name").toString().equals(currentVersionConfig.get("version").toString())) {
                    //Break out of loop if the current version is reached
                    break;
                } else {
                    //Append version info to message
                    message += "Version " + update.get("name").toString() + ":\n";
                    for (Object breaksObj : (JSONArray)update.get("breaks")) {
                        message += "- " + breaksObj.toString() + "\n";
                    }
                    message += "\n";
                }
            }
            
            //Show the update dialog
            GlobalDialogs.showUpdateNotification(message);
        }
    }
    
    //This method can only be run AFTER checkForUpdates() has been called!
    public static void doUpdate() {
        
        Logger.info("Updater.doUpdate", "Starting update...");
        
        //Show progress
        GlobalDialogs.showProgressDialog();
        GlobalDialogs.setProgressCaption("Initializing...");
        GlobalDialogs.setProgressIndeterminate(true);
        
        //Load current version info
        Logger.info("Updater.doUpdate", "Loading 'version.json' file...");
        JSONObject currentVersionConfig;
        try {
            currentVersionConfig = Util.readJSONFile("./update/version.json");
        } catch (IOException ex) {
            Logger.error("Updater.doUpdate", "Failed to load current version!", false, ex.getMessage());
            GlobalDialogs.hideProgressDialog();
            GlobalDialogs.showNotification("Failed to load current version info!");
            return;
        }
        
        //Load the versions file
        Logger.info("Updater.doUpdate", "Loading 'dl_versions.json' file...");
        JSONObject versionsConfig;
        try {
            versionsConfig = Util.readJSONFile("./update/dl_versions.json");
        } catch (IOException ex) {
            Logger.error("Updater.doUpdate", "Failed to load version index file!", false, ex.getMessage());
            GlobalDialogs.hideProgressDialog();
            GlobalDialogs.showNotification("Failed to load the version index!");
            return;
        }
        
        //Update status
        GlobalDialogs.setProgressCaption("Cleaning...");
        
        //Clean patch directory
        Logger.info("Updater.doUpdate", "Cleaning patch directory...");
        try {
            FileUtils.deleteDirectory(new File("./update/patch"));
        } catch (IOException ex) {
            Logger.error("Updater.doUpdate", "Failed to clean the patch directory!", false, ex.getMessage());
            GlobalDialogs.hideProgressDialog();
            GlobalDialogs.showNotification("Failed to clean the patch directory!");
            return;
        }
        
        //Download and extract patches
        for (Object updateObj : (JSONArray)versionsConfig.get("versions")) {
            JSONObject update = (JSONObject)updateObj;

            if (update.get("name").toString().equals(currentVersionConfig.get("version").toString())) {
                //Break out of loop if the current version is reached
                break;
            } else {
                
                //Update status
                GlobalDialogs.setProgressCaption("Getting patch '" + update.get("name").toString() + "'...");
                
                //Download version patch
                Logger.info("Updater.doUpdate", "Downloading patch '" + update.get("name").toString() + "'...");
                try {
                    FileUtils.copyURLToFile(new URL(update.get("patch").toString()), new File("./update/" + update.get("name").toString() + "-patch.zip"));
                } catch (IOException ex) {
                    Logger.error("Updater.doUpdate", "Failed to download patch!", false, ex.getMessage());
                    GlobalDialogs.hideProgressDialog();
                    GlobalDialogs.showNotification("Failed to download patch '" + update.get("name").toString() + "'!");
                    return;
                }
                
                //Extract version patch
                Logger.info("Updater.doUpdate", "Extracting patch '" + update.get("name").toString() + "'...");
                try {
                    Util.decompressZipfile("./update/" + update.get("name").toString() + "-patch.zip", "./update/patch");
                } catch (ZipException ex) {
                    Logger.error("Updater.doUpdate", "Failed to extract patch!", false, ex.getMessage());
                    GlobalDialogs.hideProgressDialog();
                    GlobalDialogs.showNotification("Failed to extract patch '" + update.get("name").toString() + "'!");
                    return;
                }
                
            }
        }
        
        //Update status
        GlobalDialogs.setProgressCaption("Restarting...");
        
        //Start MUpdate
        ProcessBuilder updater = new ProcessBuilder(new String[] { "javaw.exe", "-jar", "./update/MUpdate.jar" });
        updater.directory(new File("./update"));
        try {
            updater.start();
        } catch (Exception ex) {
            Logger.error("Updater.doUpdate", "Failed to start MUpdate!", false, ex.getMessage());
            GlobalDialogs.hideProgressDialog();
            GlobalDialogs.showNotification("Could not start MUpdate!");
            return;
        }
        
        //Exit Moddle
        GlobalDialogs.hideProgressDialog();
        System.exit(0);
    }
}
