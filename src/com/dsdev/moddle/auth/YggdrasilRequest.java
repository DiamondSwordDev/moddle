
package com.dsdev.moddle.auth;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONObject;

/**
 *
 * @author LukeSmalley
 */
public class YggdrasilRequest {
    
    private String Username = "";
    private String Password = "";
    
    public YggdrasilRequest(String username, String password) {
        this.Username = username;
        this.Password = password;
    }
    
    public YggdrasilResult send() throws Exception {
        JSONObject agent = new JSONObject();
        agent.put("name", "Minecraft");
        agent.put("version", "1");
        JSONObject payload = new JSONObject();
        payload.put("agent", agent);
        payload.put("username", Username);
        payload.put("password", Password);
        String resultString = doJSONPost("https://authserver.mojang.com/authenticate", payload);
        YggdrasilResult result = new YggdrasilResult(resultString);
        return result;
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
