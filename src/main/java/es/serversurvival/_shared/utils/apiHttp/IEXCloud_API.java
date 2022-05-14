package es.serversurvival._shared.utils.apiHttp;


import es.serversurvival._shared.utils.Funciones;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public final class IEXCloud_API {
    public static final String token = "Tsk_604d13bfa8544d66ab14f9d5e62ff80f";

    private IEXCloud_API() {
    }

    public synchronized static double getOnlyPrice(String ticker) throws Exception {
        Object response = Funciones.peticionHttp("https://sandbox.iexapis.com/stable/stock/" + ticker + "/price?token=" + token);

        return Double.parseDouble(String.valueOf(response));
    }

    public static JSONArray getDividendo(String ticker, String dentroDeCuando) throws Exception {
        Object response = Funciones.peticionHttp("https://sandbox.iexapis.com/stable/time-series/advanced_dividends/" + ticker + "?range=next-" + dentroDeCuando + "&token=" + token);

        return (JSONArray) response;
    }

    public static double getPrecioCriptomoneda(String simbolo) throws Exception {
        JSONObject json = (JSONObject) Funciones.peticionHttp("https://sandbox.iexapis.com/stable/crypto/" + simbolo + "/price?token=" + token);

        return Double.parseDouble((String) json.get("price"));
    }

    public static double getPrecioMateriaPrima(String simbolo) throws Exception {
        Object response = Funciones.peticionHttp("https://sandbox.iexapis.com/stable/data-points/market/" + simbolo + "?token=" + token);

        return Double.parseDouble(String.valueOf(response));
    }

    public static String getNombreEmpresa(String simbolo) throws Exception {
        Object response = Funciones.peticionHttp("https://sandbox.iexapis.com/stable/stock/" + simbolo + "/company?token=" + token);
        JSONObject json = (JSONObject) response;
        String nombreValor = String.valueOf(json.get("companyName"));

        nombreValor = Funciones.quitarCaracteres(nombreValor, '.', ',');
        nombreValor = Funciones.quitarPalabrasEntreEspacios(nombreValor, "group", "inc", "co", "corp", "holdings", "ltd", "adr");

        return nombreValor;
    }

    public static JSONObject getSplitInfoEmpresa(String ticker) throws Exception {
        Object response = Funciones.peticionHttp("https://sandbox.iexapis.com/stable/stock/" + ticker + "/splits/1m?token=" + token);
        JSONArray responseArray = (JSONArray) response;

        return (JSONObject) responseArray.get(0);
    }
}
