package es.serversurvival.objetos.apiHttp;

import es.serversurvival.main.Funciones;
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

        JSONObject jsonObject = (JSONObject) Funciones.httpResponseToObject(con);

        return (String) jsonObject.get("id");
    }
}
