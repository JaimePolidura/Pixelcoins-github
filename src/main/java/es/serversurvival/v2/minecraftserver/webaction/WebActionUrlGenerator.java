package es.serversurvival.v2.minecraftserver.webaction;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.minecraftserver.webaction.server.HttpServer;
import es.serversurvival.v2.minecraftserver.webaction.token.WebActionTokenService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class WebActionUrlGenerator {
    private final WebActionTokenService webActionTokenService;

    public String generator(WebActionType actionType, UUID jugadorId) {
        String token = webActionTokenService.generate(actionType, jugadorId);

        return String.format("http://%s:%s/%s?token=%s", HttpServer.HTTP_WEB_ACTION_SERVER_IP, HttpServer.HTTP_WEB_ACTION_SERVER_PORT,
                actionType.getUrlFrontend(), token);
    }
}
