package es.serversurvival.minecraftserver.webaction.requesthandlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import es.dependencyinjector.dependencies.DependenciesRepository;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.minecraftserver.webaction.WebActionException;
import es.serversurvival.minecraftserver.webaction.WebActionHandler;
import es.serversurvival.minecraftserver.webaction.WebActionType;
import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.AllArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public final class WebActionExecutor {
    private final DependenciesRepository dependenciesRepository;
    private final ObjectMapper objectMapper;

    public void execute(UUID jugadorId, WebActionType actionType, HttpExchange client) throws IOException, WebActionException {
        WebActionHandler webActionHandler = webActionHandlerRegistrado(actionType);
        WebActionRequestBody requestBody = getWebActionRequestBody(client, actionType);
        webActionHandler.handle(jugadorId, requestBody);
    }

    private WebActionHandler webActionHandlerRegistrado(WebActionType actionType) throws WebActionException {
        Optional<WebActionHandler> webActionHandler = dependenciesRepository.filterByImplementsInterfaceWithGeneric(WebActionHandler.class,
                actionType.getRequestBodyClass());

        if(webActionHandler.isEmpty())
            throw new WebActionException("Operacion no encontrada");

        return webActionHandler.get();
    }

    private WebActionRequestBody getWebActionRequestBody(HttpExchange client, WebActionType actionType) throws IOException {
        InputStream inputStream = client.getRequestBody();
        byte[] jsonRaw = inputStream.readAllBytes();

        return objectMapper.readValue(jsonRaw, actionType.getRequestBodyClass());
    }
}
