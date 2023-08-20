package es.serversurvival.minecraftserver.webaction;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.minecraftserver.webaction.token.WebActionTokenService;
import es.serversurvival.pixelcoins.config._shared.application.Configuration;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import lombok.AllArgsConstructor;

import java.util.UUID;

import static es.serversurvival._shared.ConfigurationVariables.*;

@Service
@AllArgsConstructor
public final class WebActionUrlGenerator {
    private final WebActionTokenService webActionTokenService;
    private final Configuration configuration;

    public String generate(WebActionType actionType, UUID jugadorId) {
        String token = webActionTokenService.generate(actionType, jugadorId);
        int frontendPort = configuration.getInt(ConfigurationKey.WEB_ACTIONS_FRONTEND_SERVER_PORT);

        return frontendPort == 80 ?
                String.format("http://%s/webaction?token=%s", configuration.get(ConfigurationKey.WEB_ACTIONS_SERVER_IP), token) :
                String.format("http://%s:%s/webaction?token=%s", configuration.get(ConfigurationKey.WEB_ACTIONS_SERVER_IP), configuration.get(ConfigurationKey.WEB_ACTIONS_FRONTEND_SERVER_PORT), token);
    }
}
