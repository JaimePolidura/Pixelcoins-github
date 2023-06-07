package es.serversurvival.minecraftserver.webaction.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.DomainException;
import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.minecraftserver.webaction.WebActionException;
import es.serversurvival.minecraftserver.webaction.WebActionHandler;
import es.serversurvival.minecraftserver.webaction.WebActionType;
import es.serversurvival.minecraftserver.webaction.token.WebActionTokenService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public final class HttpRequestHandler implements HttpHandler {
    private final DependenciesRepository dependenciesRepository;
    private final WebActionTokenService webActionTokenService;
    private final JugadoresService jugadoresService;
    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpExchange client) throws IOException {
        try{
            tryHandleWebActionRequest(client);
        }catch (WebActionException | DomainException e) {
            send(client, e.getMessage(), 400);
        }catch (Exception e) {
            send(client, e.getMessage(), 500);
            e.printStackTrace();
        }
    }

    private void tryHandleWebActionRequest(HttpExchange client) throws IOException, WebActionException {
        verboHttpEsPost(client);

        String token = client.getRequestHeaders().get("token").get(0);
        tokenEsValido(token);

        WebActionType actionType = webActionTokenService.getWebActionTypeFromToken(token);
        UUID jugadorId = webActionTokenService.getJugadorIdFromToken(token);
        elJugadorExisteYActionTypeTambien(actionType, jugadorId);

        WebActionHandler webActionHandler = webActionHandlerRegistrado(actionType);
        WebActionRequestBody requestBody = getWebActionRequestBody(client, actionType);
        webActionHandler.handle(jugadorId, requestBody);
    }

    private WebActionRequestBody getWebActionRequestBody(HttpExchange client, WebActionType actionType) throws IOException {
        InputStream inputStream = client.getRequestBody();
        byte[] jsonRaw = inputStream.readAllBytes();

        return objectMapper.readValue(jsonRaw, actionType.getRequestBodyClass());
    }

    private WebActionHandler webActionHandlerRegistrado(WebActionType actionType) throws WebActionException {
        Optional<WebActionHandler> webActionHandler = dependenciesRepository.filterByImplementsInterfaceWithGeneric(WebActionHandler.class,
                actionType.getRequestBodyClass());

        if(webActionHandler.isEmpty())
            throw new WebActionException("Operacion no encontrada");

        return webActionHandler.get();
    }

    private void elJugadorExisteYActionTypeTambien(WebActionType actionType, UUID jugadorId) throws WebActionException {
        if(jugadoresService.findById(jugadorId).isEmpty() || actionType == null)
            throw new WebActionException("Datos invalidos");
    }

    private void tokenEsValido(String token) throws WebActionException {
        if(webActionTokenService.isValid(token))
            throw new WebActionException("Sesion expirada, pruebe de nuevo a generar el link");
    }

    private static void verboHttpEsPost(HttpExchange client) throws WebActionException {
        if(!client.getRequestMethod().equals("POST"))
            throw new WebActionException("Method not allowed");
    }

    private void send(HttpExchange client, String response, int statusCode) throws IOException {
        client.sendResponseHeaders(statusCode, response.length());
        OutputStream outputStream = client.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.close();
    }
}
