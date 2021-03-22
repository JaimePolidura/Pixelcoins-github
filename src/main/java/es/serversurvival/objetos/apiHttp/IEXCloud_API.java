package es.serversurvival.objetos.apiHttp;


import es.serversurvival.main.Funciones;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class IEXCloud_API {
    private static final String token = "Tsk_604d13bfa8544d66ab14f9d5e62ff80f";
    
    public synchronized static double getOnlyPrice(String ticker) throws IOException {
        URL url = new URL("https://sandbox.iexapis.com/stable/stock/" + ticker + "/price?token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        return Double.parseDouble(in.readLine());
    }

    public synchronized static JSONArray getDividendo(String ticker, String rango) throws Exception {
        URL url = new URL("https://sandbox.iexapis.com/stable/time-series/advanced_dividends/" + ticker + "?range=next-" + rango + "&token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bfr = new BufferedReader(new InputStreamReader(con.getInputStream()));

        return (JSONArray) Funciones.httpResponseToObject(con);
    }

    public synchronized static double getPrecioCriptomoneda(String symbol) throws Exception {
        URL url = new URL("https://sandbox.iexapis.com/stable/crypto/" + symbol +"/price?token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        JSONObject jsonObject = (JSONObject) Funciones.httpResponseToObject(con);

        return Double.parseDouble((String) jsonObject.get("price"));
    }

    public synchronized static double getPrecioMateriaPrima(String symbol) throws Exception {
        URL url = new URL("https://sandbox.iexapis.com/stable/data-points/market/" + symbol + "?token=" + token);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

        return Double.parseDouble(bufferedReader.readLine());
    }
}