
package com.dsdev.moddle;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

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
    
    public static void showNotification(String message) {
        popupDialogCaptionLabel.setText(message);
        popupDialog.setVisible(true);
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
    
    public static void setProgressIndeterminate(boolean indeterminate) {
        progressDialogStatusBar.setIndeterminate(indeterminate);
    }
    
}
