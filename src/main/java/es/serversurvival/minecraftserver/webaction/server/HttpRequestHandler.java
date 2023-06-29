package es.serversurvival.minecraftserver.webaction.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import es.dependencyinjector.dependencies.annotations.Service;
import es.jaime.javaddd.domain.exceptions.DomainException;
import es.jaime.javaddd.domain.exceptions.ResourceNotFound;
import es.serversurvival.minecraftserver.webaction.*;
import es.serversurvival.minecraftserver.webaction.requesthandlers.WebActionExecutor;
import es.serversurvival.minecraftserver.webaction.requesthandlers.WebActionDataSender;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.minecraftserver.webaction.token.WebActionTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public final class HttpRequestHandler implements HttpHandler {
    private final WebActionDataSender webActionDataSender;
    private final WebActionExecutor webActionExecutor;

    private final WebActionTokenService webActionTokenService;
    private final JugadoresService jugadoresService;

    @Override
    public void handle(HttpExchange client) throws IOException {
        try{
            tryHandleWebActionRequest(client);
        }catch (WebActionException | DomainException e) {
            send(client, e.getMessage(), 400);
        }catch (ExpiredJwtException e) {
            send(client, "Vuelva a generar el link", 400);
        }catch (Exception e) {
            send(client, e.getMessage(), 500);
            e.printStackTrace();
        }
    }

    private void tryHandleWebActionRequest(HttpExchange client) throws IOException, WebActionException {
        String token = HttpUtils.getQueryParam(client, "token")
                .orElseThrow(() -> new ResourceNotFound("Formato invalido"));
        tokenEsValido(token);

        WebActionType actionType = webActionTokenService.getWebActionTypeFromToken(token);;
        UUID jugadorId = webActionTokenService.getJugadorIdFromToken(token);
        elJugadorExisteYActionTypeTambien(actionType, jugadorId);

        if(client.getRequestMethod().equals("POST")){
            webActionExecutor.execute(jugadorId, actionType, client);
            send(client, "", 200);
        }else{
            webActionDataSender.sendWebActionData(client, actionType);
        }
    }

    private void elJugadorExisteYActionTypeTambien(WebActionType actionType, UUID jugadorId) throws WebActionException {
        if(jugadoresService.findById(jugadorId).isEmpty() || actionType == null)
            throw new WebActionException("Datos invalidos");
    }

    private void tokenEsValido(String token) throws WebActionException {
        if(webActionTokenService.isNotExpired(token))
            throw new WebActionException("Sesion expirada, pruebe de nuevo a generar el link");
    }

    private void send(HttpExchange client, String response, int statusCode) throws IOException {
        client.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        client.sendResponseHeaders(statusCode, response.length());
        OutputStream outputStream = client.getResponseBody();
        outputStream.write(response.getBytes());
        outputStream.flush();
        outputStream.close();
    }
}
