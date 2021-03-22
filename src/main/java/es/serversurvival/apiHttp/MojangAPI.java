package es.serversurvival.apiHttp;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static es.serversurvival.util.Funciones.*;

public final class MojangAPI {
    private MojangAPI () {}

    public static synchronized String getUUID (String name) throws Exception {
        JSONObject reponse = (JSONObject) peticionHttp("https://api.mojang.com/users/profiles/minecraft/" + name);

        return (String) reponse.get("id");
    }

    public static synchronized JSONObject getSkinInfo (String UUID) throws Exception {
        JSONObject response = (JSONObject) peticionHttp("https://sessionserver.mojang.com/session/minecraft/profile/" + UUID + "?unsigned=false");

        return (JSONObject) ((JSONArray) response.get("properties")).get(0);
    }
}