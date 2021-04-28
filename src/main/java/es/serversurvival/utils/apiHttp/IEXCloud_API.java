package es.serversurvival.utils.apiHttp;


import es.serversurvival.utils.Funciones;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public final class IEXCloud_API {
    private static final String token = "Tsk_604d13bfa8544d66ab14f9d5e62ff80f";

    private IEXCloud_API () {}

    public synchronized static double getOnlyPrice(String ticker) throws Exception {
        Object response = Funciones.peticionHttp("https://sandbox.iexapis.com/stable/stock/" + ticker + "/price?token=" + token);

        return Double.parseDouble(String.valueOf(response));
    }

    public static JSONArray getDividendo(String ticker, String dentroDeCuando) throws Exception {
        Object response = Funciones.peticionHttp("https://sandbox.iexapis.com/stable/time-series/advanced_dividends/" + ticker + "?range=next-" + dentroDeCuando + "&token=" + token);

        return (JSONArray) response;
    }

    public static JSONObject getProximosDividendos (String ticker) throws Exception {
        Object response = Funciones.peticionHttp("https://sandbox.iexapis.com/stable/stock/" + ticker + "/dividends/next?token=" + token);

        return (JSONObject) response;
    }

    public static double getPrecioCriptomoneda(String simbolo) throws Exception {
        JSONObject json = (JSONObject) Funciones.peticionHttp("https://sandbox.iexapis.com/stable/crypto/" + simbolo +"/price?token=" + token);

        return Double.parseDouble((String) json.get("price"));
    }

    public static double getPrecioMateriaPrima(String simbolo) throws Exception {
        Object response = Funciones.peticionHttp("https://sandbox.iexapis.com/stable/data-points/market/" + simbolo + "?token=" + token);

        return Double.parseDouble(String.valueOf(response));
    }

    public static String getNombreEmpresa (String simbolo) throws Exception {
        Object response = Funciones.peticionHttp("https://sandbox.iexapis.com/stable/stock/" + simbolo + "/company?token=" + token);
        JSONObject json = (JSONObject) response;
        String nombreValor = String.valueOf(json.get("companyName"));

        nombreValor = Funciones.quitarCaracteres(nombreValor, '.', ',');
        nombreValor = Funciones.quitarPalabrasEntreEspacios(nombreValor, "group", "inc", "co", "corp", "holdings", "ltd", "adr");

        return nombreValor;
    }

    public static JSONObject getSplitInfoEmpresa (String ticker) throws Exception {
        Object response = Funciones.peticionHttp("https://sandbox.iexapis.com/stable/stock/"+ticker+"/splits/1m?token=" + token);
        JSONArray responseArray = (JSONArray) response;

        return (JSONObject) responseArray.get(0);
    }

    public static double getEPS (String ticker) throws Exception {
        JSONObject resposne = (JSONObject) Funciones.peticionHttp("https://sandbox.iexapis.com/stable/stock/"+ticker+"/earnings/1?period=annual&token=" + token);

        JSONArray arrayEearning = (JSONArray) resposne.get("earnings");
        JSONObject earningJSON = (JSONObject) arrayEearning.get(0);

        return Double.parseDouble(String.valueOf(earningJSON.get("actualEPS")));
    }
}
