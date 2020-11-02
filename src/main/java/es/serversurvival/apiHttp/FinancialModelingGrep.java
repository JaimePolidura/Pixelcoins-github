package es.serversurvival.apiHttp;

import org.json.simple.JSONArray;

import static es.serversurvival.util.Funciones.*;

public final class FinancialModelingGrep {
    private final static String token = "dfd506bddb5b08d4a6fcfce1d9b30537";

    private FinancialModelingGrep () {}

    public static JSONArray getPrecioIndice (String ticker) throws Exception {
        Object response = peticionHttp("https://financialmodelingprep.com/api/v3/quote/" + ticker + "?apikey=" + token);

        return (JSONArray) response;
    }
}