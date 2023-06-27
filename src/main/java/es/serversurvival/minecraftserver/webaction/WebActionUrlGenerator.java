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

        System.out.println(token);

//        String url = WEB_ACTIONS_FRONTEND_SERVER_PORT == 80 ?
//                String.format("http://%s/webaction?action=%s&token=%s", WEB_ACTIONS_SERVER_IP, actionType, token) :
//                String.format("http://%s:%s/webaction?action=%s&token=%s", WEB_ACTIONS_SERVER_IP, WEB_ACTIONS_FRONTEND_SERVER_PORT, actionType.toString(), token);
//
//        String left = url.split("token")[0];
//        String right = url.split("token")[1];
//        right = "token" + right.replaceAll("\\.", "+");

        return WEB_ACTIONS_FRONTEND_SERVER_PORT == 80 ?
                String.format("http://%s/webaction?action=%s&token=%s", WEB_ACTIONS_SERVER_IP, actionType, token) :
                String.format("http://%s:%s/webaction?action=%s&token=%s", WEB_ACTIONS_SERVER_IP, WEB_ACTIONS_FRONTEND_SERVER_PORT, actionType.toString(), token);
    }
}
