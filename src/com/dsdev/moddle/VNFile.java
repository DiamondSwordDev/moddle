/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dsdev.moddle;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author Greenlock28
 */
public class VNFile {
    
    public List<VNValue> Nodes = new ArrayList();
    
    public void load(String filename) {
        try {
            
            System.out.println("[VNFile] Loading " + filename + " ...");
            
            File file = new File(filename);
            List<String> lines = FileUtils.readLines(file);
        
            VNValue currentVal = null;
            
            for (String line : lines) {
                
                System.out.println("[VNFile] " + line);
                
                if (line.equals("")) {
                    continue;
                } else if (line.substring(0, 1).equals("*")) {
                    continue;
                } else if (line.substring(0, 1).equals("-")) {
                    if (currentVal != null)
                        currentVal.Values.add(line.substring(1));
                } else if (line.contains("=")) {
                    if (currentVal != null)
                        this.Nodes.add(currentVal);
                    currentVal = new VNValue();
                    currentVal.Name = line.split("=")[0];
                    currentVal.Values.add(line.substring(line.split("=")[0].length()+1));
                    this.Nodes.add(currentVal);
                    currentVal = null;
                } else if (line.substring(line.length()-1).equals(":")) {
                    if (currentVal != null)
                        this.Nodes.add(currentVal);
                    currentVal = new VNValue();
                    currentVal.Name = line.substring(0, line.length()-2);
                } else {
                    continue;
                }
                
            }
            
            System.out.println("[VNFile] Loaded file.");
            
        } catch (Exception ex) {
            System.out.println("[VNFile][ERROR] " + ex.getMessage());
        }
    }
    
    
    
    public VNValue getNodeByName(String name) {
        for (VNValue val : Nodes)
            if (val.Name.equals(name))
                return val;
        return null;
    }
    
    public String getNodeValueByName(String name) {
        VNValue val = getNodeByName(name);
        if (val != null && !val.Values.isEmpty())
            return val.Values.get(0);
        else
            return null;
    }
    
    public List<String> getNodeValuesByName(String name) {
        VNValue val = getNodeByName(name);
        if (val != null && !val.Values.isEmpty())
            return val.Values;
        else
            return null;
    }
    
}
