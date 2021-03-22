package es.serversurvival.objetos.apiHttp;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MojangAPI {

    public static synchronized String getUUID (String name) throws IOException, ParseException {
        URL url = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        BufferedReader bfr = new BufferedReader(new InputStreamReader(con.getInputStream()));

        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = bfr.readLine()) != null) {
            response.append(responseLine.trim());
        }

        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response.toString());

        return (String) jsonObject.get("id");
    }
}
