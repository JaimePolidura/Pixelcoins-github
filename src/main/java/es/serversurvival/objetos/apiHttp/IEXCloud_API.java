package es.serversurvival.objetos.apiHttp;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IEXCloud_API {
    private static final String token = "Tsk_604d13bfa8544d66ab14f9d5e62ff80f";
    
    public synchronized static double getOnlyPrice(String ticker) throws Exception {
        URL url = new URL("https://sandbox.iexapis.com/stable/stock/" + ticker + "/price?token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        return Double.parseDouble(in.readLine());
    }

    public synchronized static JSONArray getDividendo(String ticker, String rango) throws Exception {
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

    public synchronized static double getPrecioCriptomoneda(String symbol) throws Exception {
        URL url = new URL("https://sandbox.iexapis.com/stable/crypto/" + symbol +"/price?token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = bufferedReader.readLine()) != null) {
            response.append(responseLine.trim());
        }
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(response.toString());

        return Double.parseDouble((String) jsonObject.get("price"));
    }

    public synchronized static double getPrecioMateriaPrima(String symbol) throws Exception {
        URL url = new URL("https://sandbox.iexapis.com/stable/data-points/market/" + symbol + "?token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        return Double.parseDouble(bufferedReader.readLine());
    }
}