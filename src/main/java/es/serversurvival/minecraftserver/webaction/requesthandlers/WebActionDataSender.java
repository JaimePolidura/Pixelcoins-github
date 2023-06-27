package es.serversurvival.minecraftserver.webaction.requesthandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.minecraftserver.webaction.WebActionException;
import es.serversurvival.minecraftserver.webaction.WebActionGetDataResponse;
import es.serversurvival.minecraftserver.webaction.WebActionType;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;

@Service
@AllArgsConstructor
public final class WebActionDataSender {
    private final ObjectMapper objectMapper;

    public void sendWebActionData(HttpExchange client, WebActionType webActionType) throws IOException {
        WebActionGetDataResponse response = WebActionGetDataResponse.fromWebAction(webActionType);
        String jsonResponse = objectMapper.writeValueAsString(response);

        client.getResponseHeaders().add("Access-Control-Allow-Origin", "*");

        client.sendResponseHeaders(200, jsonResponse.length());
        OutputStream outputStream =  client.getResponseBody();
        outputStream.write(jsonResponse.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
