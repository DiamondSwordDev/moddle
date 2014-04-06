package com.dsdev.moddle;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Contains the methods to be used for logging.
 *
 * @author Greenlock28
 */
public class Logger {

    public static void info(String message) {
        System.out.println("[" + getCurrentTimeStamp() + "][Moddle][Info] " + message);
    }

    public static void info(String prefix, String message) {
        System.out.println("[" + getCurrentTimeStamp() + "][Moddle][" + prefix + "][Info] " + message);
    }

    public static void warning(String message) {
        System.out.println("[" + getCurrentTimeStamp() + "][Moddle][Warning] " + message);
    }

    public static void warning(String prefix, String message) {
        System.out.println("[" + getCurrentTimeStamp() + "][Moddle][" + prefix + "][Warning] " + message);
    }

    public static void error(String message) {
        System.out.println("[" + getCurrentTimeStamp() + "][Moddle][Error] " + message);
    }

    public static void error(String prefix, String message) {
        System.out.println("[" + getCurrentTimeStamp() + "][Moddle][" + prefix + "][Error] " + message);
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }

}
