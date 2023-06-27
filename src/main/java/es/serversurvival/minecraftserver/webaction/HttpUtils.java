package es.serversurvival.minecraftserver.webaction;

import com.sun.net.httpserver.HttpExchange;

import java.net.URI;
import java.util.Optional;

public final class HttpUtils {
    public static Optional<String> getQueryParam(HttpExchange httpExchange, String keyParam) {
        URI uri = httpExchange.getRequestURI();
        String query = uri.getQuery();
        String[] params = query.split("&");

        for (String param : params) {
            String[] keyValue = param.split("=");
            String key = keyValue[0];
            String value = keyValue[1];

            if(key.equalsIgnoreCase(keyParam)){
                return Optional.of(value);
            }
        }

        return Optional.empty();
    }
}
