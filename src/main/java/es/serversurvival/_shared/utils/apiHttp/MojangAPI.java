package es.serversurvival._shared.utils.apiHttp;

import es.serversurvival._shared.utils.Funciones;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public final class MojangAPI {
    private MojangAPI () {}

    public static String getUUID (String name) throws Exception {
        JSONObject reponse = (JSONObject) Funciones.peticionHttp("https://api.mojang.com/users/profiles/minecraft/" + name);

        return (String) reponse.get("id");
    }

    public static JSONObject getSkinInfo (String UUID) throws Exception {
        JSONObject response = (JSONObject) Funciones.peticionHttp("https://sessionserver.mojang.com/session/minecraft/profile/" + UUID + "?unsigned=false");

        return (JSONObject) ((JSONArray) response.get("properties")).get(0);
    }
}
