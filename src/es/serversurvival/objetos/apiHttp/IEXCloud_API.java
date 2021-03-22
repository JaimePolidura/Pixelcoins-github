package es.serversurvival.objetos.apiHttp;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IEXCloud_API {
    private static final String token = "Tsk_604d13bfa8544d66ab14f9d5e62ff80f";

    public static double getOnlyPrice(String ticker) throws Exception {
        URL url = new URL("https://sandbox.iexapis.com/stable/stock/" + ticker + "/price?token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        return Double.parseDouble(in.readLine());
    }

    public static JSONArray getDividendo(String ticker, String rango) throws Exception {
        URL url = new URL("https://sandbox.iexapis.com/stable/time-series/advanced_dividends/" + ticker + "?range=next-" + rango + "&token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bfr = new BufferedReader(new InputStreamReader(con.getInputStream()));

        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = bfr.readLine()) != null) {
            response.append(responseLine.trim());
        }

        JSONParser parser = new JSONParser();

        JSONArray jsonObject = (JSONArray) parser.parse(response.toString());
        return jsonObject;
    }
}