/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dsdev.yggdrasil;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 *
 * @author LukeSmalley
 */
public class Yggdrasilinator {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            
            System.out.println("Loading account info...");
            String inputFile = FileUtils.readFileToString(new File("./yggdrasil.in.tmp"));
        
            String uname = inputFile.split(":")[0];
            String pword = inputFile.split(":")[1];

            System.out.println("Building JSON...");
            JSONObject agent = new JSONObject();
            agent.put("name", "Minecraft");
            agent.put("version", "1");
            JSONObject payload = new JSONObject();
            payload.put("agent", agent);
            payload.put("username", uname);
            payload.put("password", pword);

            System.out.println("Submitting POST request...");
            String resultString = doJSONPost("https://authserver.mojang.com/authenticate", payload);
            JSONObject result = (JSONObject)JSONValue.parse(resultString);
            
            if (result.get("error") == null) {
                
                System.out.println("Request was presumably successful!");
                System.out.println("Writing results to output file...");
                
                String accessToken = (String)result.get("accessToken");
                String clientToken = (String)result.get("clientToken");
                
                JSONObject selectedProfile = (JSONObject)result.get("selectedProfile");
                
                String UUID = (String)selectedProfile.get("id");
                String Username = (String)selectedProfile.get("name");
                
                FileUtils.writeStringToFile(new File("./yggdrasil.out.tmp"), accessToken + ":" + clientToken + ":" + UUID + ":" + Username);
                        
            } else {
                
                System.out.println("Request failed!");
                System.out.println("Writing results to output file...");
                
                FileUtils.writeStringToFile(new File("./yggdrasil.out.tmp"), (String)result.get("error") + ": " + (String)result.get("errorMessage"));
                
            }
        
        } catch (Exception ex) {
            
            System.out.println("Login failed:");
            System.out.println(ex.getMessage());
            String outputFile = "failed:" + ex.getMessage();
            try {
                FileUtils.writeStringToFile(new File("./yggdrasil.out.tmp"), outputFile);
            } catch (Exception exc) {
                System.out.println("Critical error writing to output file:");
                System.out.println(ex.getMessage());
            }
        }
    }
    
    public static String doJSONPost(String url, JSONObject request) throws Exception {
        URL u = new URL(url);
        String json = request.toJSONString();
        byte[] bytes = json.getBytes();

        HttpURLConnection connection = (HttpURLConnection) u.openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(15000);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        connection.setRequestProperty("Content-Length", "" + bytes.length);
        connection.setRequestProperty("Content-Language", "en-US");

        connection.setUseCaches(false);
        connection.setDoInput(true);
        connection.setDoOutput(true);

        DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
        dos.write(bytes);
        dos.flush();
        dos.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        StringBuilder result = new StringBuilder();
        while ((line = br.readLine()) != null) {
            result = result.append(line).append('\r');
        }
        br.close();
        connection.disconnect();
        return result.toString();
    }
    
}
