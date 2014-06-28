package com.dsdev.moddle;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.DefaultListModel;
import org.apache.commons.io.FileUtils;

/**
 * Contains the methods to be used for logging
 *
 * @author Diamond Sword Development
 */
public class Logger {

    public static String instanceLoggerFile = null;
    public static DefaultListModel redirectionList = null;

    public static void info(String method, String message) {
        safelyLog("[" + getCurrentTimeStamp() + "][Moddle][" + method + "] - " + message);
        if (Dialogs.progressDialogStatusLabel != null) {
            Dialogs.progressDialogStatusLabel.setText(message);
        }
    }

    public static void warning(String method, String message) {
        safelyLog("[" + getCurrentTimeStamp() + "][Moddle][" + method + "][WARNING] - " + message);
        if (Dialogs.progressDialogStatusLabel != null) {
            Dialogs.progressDialogStatusLabel.setText("Warning: " + message);
        }
    }

    public static void error(String method, String message) {
        safelyLog("[" + getCurrentTimeStamp() + "][Moddle][" + method + "][ERROR] - " + message);
    }

    public static void error(String method, String message, boolean isFatal, String exMessage) {
        safelyLog("[" + getCurrentTimeStamp() + "][Moddle] +=======================+  ERROR  +=======================+");
        safelyLog("[" + getCurrentTimeStamp() + "][Moddle] |  Error occured in method: " + method);
        safelyLog("[" + getCurrentTimeStamp() + "][Moddle] |  Message: " + message);
        if (isFatal) {
            safelyLog("[" + getCurrentTimeStamp() + "][Moddle] |  Is fatal:  Yes.");
        } else {
            safelyLog("[" + getCurrentTimeStamp() + "][Moddle] |  Is fatal:  No.");
        }
        safelyLog("[" + getCurrentTimeStamp() + "][Moddle] |  Exception: " + exMessage);
        if (Dialogs.progressDialogStatusLabel != null) {
            Dialogs.progressDialogStatusLabel.setText("Error: " + message);
        }
    }

    private static void safelyLog(String line) {
        System.out.println(line);
        safelyLogToFile(line);
        if (redirectionList != null) {
            redirectionList.addElement(line);
        }
    }

    public static void begin() {
        try {
            Util.assertDirectoryExistence("./logs");
            instanceLoggerFile = "./logs/" + getLogfileFriendlyTimeStamp() + ".txt";
            FileUtils.writeStringToFile(new File(instanceLoggerFile), "");
            System.out.println("[" + getCurrentTimeStamp() + "][Moddle][Logger.begin] - Logger has been instantiated.");
        } catch (Exception ex) {
            instanceLoggerFile = null;
            System.out.println("[" + getCurrentTimeStamp() + "][Moddle] +=======================+  ERROR  +=======================+");
            System.out.println("[" + getCurrentTimeStamp() + "][Moddle] |  Error occured in method: Logger.begin");
            System.out.println("[" + getCurrentTimeStamp() + "][Moddle] |  Message: Could not create logfile!");
            System.out.println("[" + getCurrentTimeStamp() + "][Moddle] |  Is fatal:  Not barely.");
            System.out.println("[" + getCurrentTimeStamp() + "][Moddle] |  Exception: " + ex.getMessage());
        }
    }

    private static void safelyLogToFile(String line) {
        try {
            if (instanceLoggerFile != null) {
                String fileContent = FileUtils.readFileToString(new File(instanceLoggerFile));
                FileUtils.writeStringToFile(new File(instanceLoggerFile), fileContent + line + "\r\n");
            }
        } catch (Exception ex) {
            //Log failed.
        }
    }

    public static void setProgress(int progress) {
        if (Dialogs.progressDialogStatusBar != null) {
            Dialogs.progressDialogStatusBar.setValue(progress);
        }
    }

    public static void incrementProgress(int progress) {
        if (Dialogs.progressDialogStatusBar != null) {
            if (progress > 100 || Dialogs.progressDialogStatusBar.getValue() + progress > 100) {
                Dialogs.progressDialogStatusBar.setValue(100);
            } else {
                Dialogs.progressDialogStatusBar.setValue(Dialogs.progressDialogStatusBar.getValue() + progress);
            }
        }
    }

    private static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

    private static String getLogfileFriendlyTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

}
