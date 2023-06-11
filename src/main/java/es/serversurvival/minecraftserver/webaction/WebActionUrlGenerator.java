package es.serversurvival.minecraftserver.webaction;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.Configuration;
import es.serversurvival.minecraftserver.webaction.token.WebActionTokenService;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class WebActionUrlGenerator {
    private final WebActionTokenService webActionTokenService;

    public String generate(WebActionType actionType, UUID jugadorId) {
        String token = webActionTokenService.generate(actionType, jugadorId);

        return String.format("http://%s:%s/%s?token=%s", Configuration.WEB_ACTIONS_SERVER_IP, Configuration.WEB_ACTIONS_SERVER_PORT,
                actionType.getUrlFrontend(), token);
    }
}
