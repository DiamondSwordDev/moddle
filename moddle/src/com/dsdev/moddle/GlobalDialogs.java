
package com.dsdev.moddle;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;

/**
 *
 * @author Diamond Sword Development
 */
public class GlobalDialogs {
    
    public static JDialog popupDialog = null;
    public static JLabel popupDialogCaptionLabel = null;
    
    public static JDialog progressDialog = null;
    public static JLabel progressDialogStatusLabel = null;
    public static JProgressBar progressDialogStatusBar = null;
    
    public static JDialog consoleDialog = null;
    public static JTextPane consoleDialogLogPane = null;
    
    public static JDialog updateDialog = null;
    public static JTextPane updateDialogDescriptionPane = null;
    public static JLabel updateDialogCaptionLabel = null;
    
    public static void showNotification(String message) {
        popupDialogCaptionLabel.setText("<html><body style='width: 300px'>" + message + "</body></html>");
        popupDialog.setVisible(true);
    }
    
    public static void showUpdateNotification(String message, String updateType) {
        updateDialogCaptionLabel.setText("The following " + updateType + " updates are available for automatic installation:");
        updateDialogDescriptionPane.setText(message);
        updateDialog.setVisible(true);
    }
    
    public static void showFirstUpdateNotification(String message) {
        updateDialogCaptionLabel.setText("It looks like this is your first run.  The following items are queued to install:");
        updateDialogDescriptionPane.setText(message);
        updateDialog.setVisible(true);
    }
    
    public static void showProgressDialog() {
        progressDialogStatusLabel.setText("Initializing...");
        progressDialogStatusBar.setValue(0);
        progressDialogStatusBar.setIndeterminate(false);
        progressDialog.setVisible(true);
    }
    
    public static void hideProgressDialog() {
        progressDialog.setVisible(false);
    }
    
    public static void setProgressCaption(String caption) {
        progressDialogStatusLabel.setText(caption);
    }
    
    public static void setProgressValue(int n) {
        progressDialogStatusBar.setValue(n);
    }
    
    public static void incrementProgressValue(int n) {
        if (!(progressDialogStatusBar.getValue() + n > 100)) {
            progressDialogStatusBar.setValue(progressDialogStatusBar.getValue() + n);
        } else {
            progressDialogStatusBar.setValue(100);
        }
    }
    
    public static void setProgressIndeterminate(boolean indeterminate) {
        progressDialogStatusBar.setIndeterminate(indeterminate);
    }
    
    public static void showConsole() {
        consoleDialog.setVisible(true);
    }
    
    public static void hideConsole() {
        consoleDialog.setVisible(false);
    }
    
}
