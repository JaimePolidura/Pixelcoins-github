package es.serversurvival.v2.minecraftserver.webaction;

import es.serversurvival.v2.minecraftserver.deudas.prestar.PrestarWebActionRequestBody;
import es.serversurvival.v2.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.Getter;

public enum WebActionType {
    DEUDAS_PRESTAR("prestar", PrestarWebActionRequestBody.class);

    @Getter private final String urlFrontend;
    @Getter private final Class<? extends WebActionRequestBody> requestBodyClass;

    WebActionType(String urlFrontend, Class<? extends PrestarWebActionRequestBody> requestBody) {
        this.urlFrontend = urlFrontend;
        this.requestBodyClass = requestBody;
    }
}
