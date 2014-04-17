package com.dsdev.moddle;

import java.io.File;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * The main GUI.
 *
 * @author Greenlock28
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

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        UsernameField = new javax.swing.JTextField();
        ModpackComboBox = new javax.swing.JComboBox();
        PasswordField = new javax.swing.JPasswordField();
        MainTabPane = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        ModpackDescriptionPane = new javax.swing.JTextPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        NewsContentPane = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Moddle Launcher");
        setMaximumSize(new java.awt.Dimension(800, 450));
        setMinimumSize(new java.awt.Dimension(800, 450));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton1.setText("Play");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Modpack:");

        jLabel2.setText("Username:");

        jLabel3.setText("Password:");

        ModpackComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModpackComboBoxActionPerformed(evt);
            }
        });

        MainTabPane.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        MainTabPane.setName(""); // NOI18N

        ModpackDescriptionPane.setEditable(false);
        ModpackDescriptionPane.setBackground(new java.awt.Color(250, 250, 250));
        ModpackDescriptionPane.setBorder(null);
        jScrollPane3.setViewportView(ModpackDescriptionPane);

        MainTabPane.addTab("Modpack", jScrollPane3);
        MainTabPane.addTab("Settings", jTabbedPane1);

        NewsContentPane.setEditable(false);
        NewsContentPane.setBackground(new java.awt.Color(250, 250, 250));
        jScrollPane1.setViewportView(NewsContentPane);

        MainTabPane.addTab("News", jScrollPane1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(UsernameField)
                    .addComponent(PasswordField, javax.swing.GroupLayout.DEFAULT_SIZE, 186, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ModpackComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 100, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(MainTabPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(MainTabPane, javax.swing.GroupLayout.DEFAULT_SIZE, 387, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(UsernameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(ModpackComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(PasswordField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
        
            try {
                FileUtils.writeStringToFile(Util.getFile("./lastlogin.dat"), UsernameField.getText() + "\n" + PasswordField.getText() + "\n" + ModpackComboBox.getSelectedItem().toString());
            } catch (Exception ex) { }

            if (UsernameField.getText().equals("")) {
                Logger.error("No account name given!");
                return;
            }
            
            if (PasswordField.getText().equals("")) {
                Logger.error("No password given!");
                return;
            }
            
            Logger.info("Logging in...");
            MinecraftLogin login = new MinecraftLogin();
            login.doLogin(UsernameField.getText(), PasswordField.getText());

            LaunchArgs launchArgs = new LaunchArgs();

            try {
                Logger.info("Applying global settings...");
                if (new File("./users/global.json").exists()) {
                    JSONObject globalConfig = Util.readJSONFile("./users/global.json");
                    launchArgs.loadSettings((JSONArray)globalConfig.get("settings"));
                } else {
                    Util.assertDirectoryExistence("./users");
                    FileUtils.writeStringToFile(new File("./users/global.json"), "{ settings : [ ] }");
                }
            } catch (Exception ex) { }

            try {
                Logger.info("Applying user settings...");
                if (new File("./users/" + login.Username + "/userprefs.json").exists()) {
                    JSONObject userConfig = Util.readJSONFile("./users/" + login.Username + "/userprefs.json");
                    launchArgs.loadSettings((JSONArray)userConfig.get("settings"));
                } else {
                    Util.assertDirectoryExistence("./users/" + login.Username);
                    FileUtils.writeStringToFile(new File("./users/" + login.Username + "/userprefs.json"), "{ settings : [ ] }");
                }
            } catch (Exception ex) { }
            
            try {
                Logger.info("Applying pack-specific settings...");
                String selectedPack = ModpackComboBox.getSelectedItem().toString();
                if (new File("./users/" + login.Username + "/" + selectedPack + ".json").exists()) {
                    JSONObject selpackConfig = Util.readJSONFile("./users/" + login.Username + "/" + selectedPack + ".json");
                    launchArgs.loadSettings((JSONArray)selpackConfig.get("settings"));
                } else {
                    Util.assertDirectoryExistence("./users/" + login.Username);
                    FileUtils.writeStringToFile(new File("./users/" + login.Username + "/" + selectedPack + ".json"), "{ settings : [ ] }");
                }
            } catch (Exception ex) { }

            Logger.info("Invoking pack builder...");
            PackBuilder pack = new PackBuilder(ModpackComboBox.getSelectedItem().toString());
            pack.build(launchArgs, login);

            Logger.info("Preparing to launch modpack...");
            pack.run(launchArgs, login);
        
        } catch (Exception ex) {
            Logger.error("MainForm.jButton1ActionPerformed", ex.getMessage(), true, ex.getMessage());
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {

            Logger.info("Startup", "Centering...");
            this.setLocationRelativeTo(null);

            Logger.info("Startup", "Loading icon...");
            this.setIconImage((new ImageIcon(this.getClass().getResource("icon_mb.png"))).getImage());

            Logger.info("Startup", "Clearing temporary file cache...");
            if (Util.getFile("./tmp").exists()) {
                FileUtils.deleteDirectory(Util.getFile("./tmp"));
            }
            
            Logger.info("Startup", "Loading modpacks...");
            DefaultListModel model = new DefaultListModel();
            for (File f : Util.getFile("./packs").listFiles()) {
                if (f.getName().endsWith(".zip")) {
                    if (!Util.getFile("./tmp/launcher/" + f.getName().replace(".zip", "") + "/pack.png").exists()) {
                        if (!Util.decompressZipfile(f.getCanonicalPath(), "./tmp/launcher/" + f.getName().replace(".zip", ""))) {
                            Logger.warning("Startup", "Failed to load pack image for " + f.getName().replace(".zip", ""));
                        }
                    }
                    ModpackComboBox.addItem(f.getName().replace(".zip", ""));
                }
            }
            
            Logger.info("Startup", "Loading last login info...");
            if (Util.getFile("./lastlogin.dat").exists()) {
                String lastlogin = FileUtils.readFileToString(Util.getFile("./lastlogin.dat"));
                String[] lastloginLines = lastlogin.split("\n");
                UsernameField.setText(lastloginLines[0]);
                PasswordField.setText(lastloginLines[1]);
                ModpackComboBox.setSelectedItem(lastloginLines[2]);
            }
            
            //<editor-fold defaultstate="collapsed" desc="Load modpack content">
            
            String selectedPack = ModpackComboBox.getSelectedItem().toString();
            Logger.info("Startup", "Loading Modpack description pane content (" + selectedPack + ")...");
            
            String contentLocation = "./tmp/launcher/" + selectedPack + "/";
            List<String> contentLines = FileUtils.readLines(new File("./tmp/launcher/" + selectedPack + "/description.txt"));
            SimpleAttributeSet keyWord = new SimpleAttributeSet();
            ModpackDescriptionPane.setText("");
            
            for (String line : contentLines) {
                
                if (line.startsWith("${{") && line.endsWith("}}")) {
                    String styleString = line.substring(3, line.length() - 2);
                    for (String style : styleString.split(",")) {
                        
                        String styleArg = style;
                        String styleValue = "";
                        
                        try {
                            styleArg = style.split(":")[0];
                            styleValue = style.split(":")[1];
                        } catch (Exception ex) { }
                        
                        Logger.info("Style", style);
                        
                        if (styleArg.equalsIgnoreCase("image")) {
                            ModpackDescriptionPane.insertIcon(new ImageIcon(contentLocation + styleValue));
                            ModpackDescriptionPane.getStyledDocument().insertString(ModpackDescriptionPane.getStyledDocument().getLength(), "\n", keyWord);
                        } else if (styleArg.equalsIgnoreCase("reset")) {
                            keyWord = new SimpleAttributeSet();
                        } else {
                            try {
                                for (Method m : StyleConstants.class.getMethods()) {
                                    if (m.getName().toLowerCase().equalsIgnoreCase("set" + styleArg)) {
                                        if (styleValue.equalsIgnoreCase("true") || styleValue.equalsIgnoreCase("false")) {
                                            m.invoke(null, new Object[] { keyWord, styleValue.equalsIgnoreCase("true") });
                                        } else if (Util.isNumeric(styleValue)) {
                                            m.invoke(null, new Object[] { keyWord, Integer.parseInt(styleValue) });
                                        } else {
                                            m.invoke(null, new Object[] { keyWord, styleValue });
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                Util.isNumeric("0");
                            }
                        }
                        
                    }
                } else {
                    ModpackDescriptionPane.getStyledDocument().insertString(ModpackDescriptionPane.getStyledDocument().getLength(), line + "\n", keyWord);
                }
                
            }
            
            ModpackDescriptionPane.setCaretPosition(0);
            
            //</editor-fold>
            
            //<editor-fold defaultstate="collapsed" desc="Load news content">
            
            Logger.info("Startup", "Fetching news...");
            
            InetAddress addr = InetAddress.getByName("sites.google.com");
            if (addr.isReachable(600)) {
                FileUtils.copyURLToFile(new URL("https://sites.google.com/site/moddleframework/news.zip"), new File("./news.zip"));
            } else {
                Logger.warning("Startup", "Failed to update news!");
            }
            
            if (!Util.decompressZipfile("./news.zip", "./tmp/news/")) {
                Logger.warning("Startup", "Failed to load news!");
            }
            
            contentLocation = "./tmp/news/";
            contentLines = FileUtils.readLines(new File("./tmp/news/content.txt"));
            keyWord = new SimpleAttributeSet();
            NewsContentPane.setText("");
            
            for (String line : contentLines) {
                
                if (line.startsWith("${{") && line.endsWith("}}")) {
                    String styleString = line.substring(3, line.length() - 2);
                    for (String style : styleString.split(",")) {
                        
                        String styleArg = style;
                        String styleValue = "";
                        
                        try {
                            styleArg = style.split(":")[0];
                            styleValue = style.split(":")[1];
                        } catch (Exception ex) { }
                        
                        Logger.info("Style", style);
                        
                        if (styleArg.equalsIgnoreCase("image")) {
                            NewsContentPane.insertIcon(new ImageIcon(contentLocation + styleValue));
                            NewsContentPane.getStyledDocument().insertString(NewsContentPane.getStyledDocument().getLength(), "\n", keyWord);
                        } else if (styleArg.equalsIgnoreCase("reset")) {
                            keyWord = new SimpleAttributeSet();
                        } else {
                            try {
                                for (Method m : StyleConstants.class.getMethods()) {
                                    if (m.getName().toLowerCase().equalsIgnoreCase("set" + styleArg)) {
                                        if (styleValue.equalsIgnoreCase("true") || styleValue.equalsIgnoreCase("false")) {
                                            m.invoke(null, new Object[] { keyWord, styleValue.equalsIgnoreCase("true") });
                                        } else if (Util.isNumeric(styleValue)) {
                                            m.invoke(null, new Object[] { keyWord, Integer.parseInt(styleValue) });
                                        } else {
                                            m.invoke(null, new Object[] { keyWord, styleValue });
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                Util.isNumeric("0");
                            }
                        }
                        
                    }
                } else {
                    NewsContentPane.getStyledDocument().insertString(NewsContentPane.getStyledDocument().getLength(), line + "\n", keyWord);
                }
                
            }
            
            NewsContentPane.setCaretPosition(0);
            
            //</editor-fold>
            
            Logger.info("Startup", "Finished loading.");

        } catch (Exception ex) {
            Logger.error("MainForm.formWindowOpened", ex.getMessage(), false, ex.getMessage());
        }
    }//GEN-LAST:event_formWindowOpened

    private void ModpackComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModpackComboBoxActionPerformed
        try {

            String selectedPack = ModpackComboBox.getSelectedItem().toString();
            Logger.info("Startup", "Loading Modpack description pane content (" + selectedPack + ")...");
            
            String contentLocation = "./tmp/launcher/" + selectedPack + "/";
            List<String> contentLines = FileUtils.readLines(new File("./tmp/launcher/" + selectedPack + "/description.txt"));
            SimpleAttributeSet keyWord = new SimpleAttributeSet();
            ModpackDescriptionPane.setText("");
            
            for (String line : contentLines) {
                
                if (line.startsWith("${{") && line.endsWith("}}")) {
                    String styleString = line.substring(3, line.length() - 2);
                    for (String style : styleString.split(",")) {
                        
                        String styleArg = style.split(":")[0];
                        String styleValue = style.split(":")[1];
                        
                        Logger.info("Style", style);
                        
                        if (styleArg.equalsIgnoreCase("image")) {
                            ModpackDescriptionPane.insertIcon(new ImageIcon(contentLocation + styleValue));
                            ModpackDescriptionPane.getStyledDocument().insertString(ModpackDescriptionPane.getStyledDocument().getLength(), "\n", keyWord);
                        } else if (styleArg.equalsIgnoreCase("reset")) {
                            keyWord = new SimpleAttributeSet();
                        } else {
                            try {
                                for (Method m : StyleConstants.class.getMethods()) {
                                    if (m.getName().toLowerCase().equalsIgnoreCase("set" + styleArg)) {
                                        if (styleValue.equalsIgnoreCase("true") || styleValue.equalsIgnoreCase("false")) {
                                            m.invoke(null, new Object[] { keyWord, styleValue.equalsIgnoreCase("true") });
                                        } else if (Util.isNumeric(styleValue)) {
                                            m.invoke(null, new Object[] { keyWord, Integer.parseInt(styleValue) });
                                        } else {
                                            m.invoke(null, new Object[] { keyWord, styleValue });
                                        }
                                    }
                                }
                            } catch (Exception ex) {
                                Util.isNumeric("0");
                            }
                        }
                        
                    }
                } else {
                    ModpackDescriptionPane.getStyledDocument().insertString(ModpackDescriptionPane.getStyledDocument().getLength(), line + "\n", keyWord);
                }
                
            }
            
            ModpackDescriptionPane.setCaretPosition(0);
            
            MainTabPane.setSelectedIndex(0);
            
        } catch (Exception ex) {
            Logger.error("MainForm.ModpackComboBoxActionPerformed", ex.getMessage(), false, ex.getMessage());
        }
    }//GEN-LAST:event_ModpackComboBoxActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            /*for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
             if ("Nimbus".equals(info.getName())) {
             javax.swing.UIManager.setLookAndFeel(info.getClassName());
             break;
             }
             }*/
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
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
    private javax.swing.JTabbedPane MainTabPane;
    private javax.swing.JComboBox ModpackComboBox;
    private javax.swing.JTextPane ModpackDescriptionPane;
    private javax.swing.JTextPane NewsContentPane;
    private javax.swing.JPasswordField PasswordField;
    private javax.swing.JTextField UsernameField;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
