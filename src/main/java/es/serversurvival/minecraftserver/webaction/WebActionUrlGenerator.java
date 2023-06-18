package es.serversurvival.minecraftserver.webaction;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.minecraftserver.webaction.token.WebActionTokenService;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static es.serversurvival._shared.ConfigurationVariables.*;

@Service
@AllArgsConstructor
public final class WebActionUrlGenerator {
    private final WebActionTokenService webActionTokenService;

    public String generate(WebActionType actionType, UUID jugadorId) {
        String token = webActionTokenService.generate(actionType, jugadorId);

        return String.format("http://%s:%s/webaction?token=%s", WEB_ACTIONS_SERVER_IP, WEB_ACTIONS_SERVER_PORT, token);
    }
}
