/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dsdev.mupdate;

import com.dsdev.mupdate.resources.Resources;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;
import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author LukeSmalley
 */
public class MainForm extends javax.swing.JFrame {

    /**
     * Creates new form MainForm
     */
    public MainForm() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupDialog = new javax.swing.JDialog();
        popupDialogCaptionLabel = new javax.swing.JLabel();
        popupDialogOkButton = new javax.swing.JButton();
        popupDialogImageLabel = new javax.swing.JLabel();
        popupDialogYesButton = new javax.swing.JButton();
        UpdateLogoLabel = new javax.swing.JLabel();
        StatusBar = new javax.swing.JProgressBar();

        popupDialog.setMinimumSize(new java.awt.Dimension(505, 143));
        popupDialog.setModalityType(java.awt.Dialog.ModalityType.APPLICATION_MODAL);
        popupDialog.setResizable(false);
        popupDialog.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                popupDialogWindowClosed(evt);
            }
        });

        popupDialogCaptionLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        popupDialogOkButton.setText("Ok");
        popupDialogOkButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupDialogOkButtonActionPerformed(evt);
            }
        });

        popupDialogYesButton.setText("Yes");
        popupDialogYesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupDialogYesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout popupDialogLayout = new javax.swing.GroupLayout(popupDialog.getContentPane());
        popupDialog.getContentPane().setLayout(popupDialogLayout);
        popupDialogLayout.setHorizontalGroup(
            popupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popupDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(popupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(popupDialogLayout.createSequentialGroup()
                        .addComponent(popupDialogImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(popupDialogCaptionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 410, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, popupDialogLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(popupDialogYesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(popupDialogOkButton, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        popupDialogLayout.setVerticalGroup(
            popupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(popupDialogLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(popupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, popupDialogLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(popupDialogCaptionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(popupDialogImageLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(popupDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(popupDialogOkButton)
                    .addComponent(popupDialogYesButton))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MUpdate");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        StatusBar.setIndeterminate(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(StatusBar, javax.swing.GroupLayout.DEFAULT_SIZE, 501, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(143, 143, 143)
                        .addComponent(UpdateLogoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(UpdateLogoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StatusBar, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    private static String[] Arguments;
    
    private final int RESULT_OK = 0;
    private final int RESULT_NO = 0;
    private final int RESULT_YES = 1;
    
    private int DialogResult = -1;
    
    private int showPopupDialog(String message) {
        popupDialog.setLocationRelativeTo(null);

        popupDialogYesButton.setVisible(false);
        popupDialogOkButton.setText("Ok");

        popupDialogCaptionLabel.setText("<html><body style='width: 300px'>" + message + "</body></html>");
        
        popupDialog.setVisible(true);
        
        while (DialogResult < 0) { }
        
        int returnValue = DialogResult;
        DialogResult = -1;
        
        return returnValue;
    }
    
    private int showYesNoDialog(String message) {
        popupDialog.setLocationRelativeTo(null);

        popupDialogYesButton.setVisible(true);
        popupDialogOkButton.setText("No");

        popupDialogCaptionLabel.setText("<html><body style='width: 300px'>" + message + "</body></html>");
        
        popupDialog.setVisible(true);
        
        while (DialogResult < 0) { }
        
        int returnValue = DialogResult;
        DialogResult = -1;
        
        return returnValue;
    }
    
    
    private void copyDirectoryAndBackUpOldFiles(File source, File target) {
        for (File f : source.listFiles()) {
            if (f.isFile()) {
                try {
                    copyFileAndBackUpOldCopy(f, new File(target, f.getName()));
                } catch (IOException ex) {
                    System.out.println("Failed to copy file '" + f.getName() + "'.");
                    ex.printStackTrace(System.out);
                }
            } else if (f.isDirectory()) {
                copyDirectoryAndBackUpOldFiles(f, new File(target, f.getName()));
            }
        }
    }
    
    private void copyFileAndBackUpOldCopy(File source, File target) throws IOException {
        if (target.isFile()) {
            if (!target.getName().endsWith(".jar")) {
                String[] extensions = target.getName().split(".");
                String extension = "." + extensions[extensions.length-1];
                copyFileAndBackUpOldCopy(target, new File(target.getParentFile(), target.getName().replace(extension, ".old" + extension)));
            }
        }
        FileUtils.copyFile(source, target);
    }
    
    
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        //Set window properties
        this.setLocationRelativeTo(null);
        this.setIconImage(Resources.getImageResource("icon.png").getImage());
        popupDialog.setIconImage(Resources.getImageResource("icon.png").getImage());
        UpdateLogoLabel.setIcon(Resources.getImageResource("update.png"));
        popupDialogImageLabel.setIcon(Resources.getImageResource("alert.png"));
        
        SimpleSwingWorker worker = new SimpleSwingWorker() {
            
            @Override
            protected void task() {
                
                //Copy updates
                copyDirectoryAndBackUpOldFiles(new File("./launcherpatch"), new File(".."));
                
                //Load version config
                JSONObject versionConfig;
                try {
                    versionConfig = (JSONObject)JSONValue.parse(FileUtils.readFileToString(new File("./version.json")));
                } catch (IOException ex) {
                    showPopupDialog("Warning:  The version file could not be updated!  This may cause problems.");
                    System.exit(0);
                    return;
                }
                
                //Get new version from arguments
                String newVersion = "0";
                for (String arg : Arguments) {
                    if (arg.startsWith("--version=")) {
                        newVersion = arg.substring(10);
                        break;
                    }
                }
                
                //Save new version file
                try {
                    versionConfig.put("moddleversion", newVersion);
                    FileUtils.writeStringToFile(new File("./version.json"), versionConfig.toJSONString());
                } catch (IOException ex) {
                    showPopupDialog("Warning:  The version file could not be updated!  This may cause problems.");
                    System.exit(0);
                    return;
                }
                
                //Start Moddle
                try {
                    ProcessBuilder moddle = new ProcessBuilder(new String[] { "javaw.exe", "-jar", "\"" + new File("../Moddle.jar").getCanonicalPath() + "\"" });
                    moddle.directory(new File(".."));
                    moddle.start();
                } catch (IOException ex) {
                    try {
                        ProcessBuilder moddle = new ProcessBuilder(new String[] { "javaw.exe", "-jar", "\"../Moddle.jar\"" });
                        moddle.directory(new File(".."));
                        moddle.start();
                    } catch (IOException ex2) { showPopupDialog("Failed to start Moddle!"); }
                }
                
                //Exit
                System.exit(0);
                
            }
            
        };
        worker.execute();
    }//GEN-LAST:event_formWindowOpened

    private void popupDialogWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_popupDialogWindowClosed
        DialogResult = 0;
        popupDialog.setVisible(false);
    }//GEN-LAST:event_popupDialogWindowClosed

    private void popupDialogYesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupDialogYesButtonActionPerformed
        DialogResult = 1;
        popupDialog.setVisible(false);
    }//GEN-LAST:event_popupDialogYesButtonActionPerformed

    private void popupDialogOkButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupDialogOkButtonActionPerformed
        DialogResult = 0;
        popupDialog.setVisible(false);
    }//GEN-LAST:event_popupDialogOkButtonActionPerformed

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        Arguments = args;
        
        if (args.length < 1) {
            System.out.println("Not enough arguments.");
            System.exit(0);
        }
        
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            SeaGlassLookAndFeel sglaf = new SeaGlassLookAndFeel();
            javax.swing.UIManager.setLookAndFeel(sglaf);
            
            /*for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }*/
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JProgressBar StatusBar;
    private javax.swing.JLabel UpdateLogoLabel;
    private javax.swing.JDialog popupDialog;
    private javax.swing.JLabel popupDialogCaptionLabel;
    private javax.swing.JLabel popupDialogImageLabel;
    private javax.swing.JButton popupDialogOkButton;
    private javax.swing.JButton popupDialogYesButton;
    // End of variables declaration//GEN-END:variables
}
