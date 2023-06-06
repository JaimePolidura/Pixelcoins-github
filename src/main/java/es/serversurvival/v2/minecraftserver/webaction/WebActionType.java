package es.serversurvival.v2.minecraftserver.webaction;

import es.serversurvival.v2.minecraftserver.deudas.emitir.EmitirDeudaWebActionRequestBody;
import es.serversurvival.v2.minecraftserver.deudas.prestar.PrestarWebActionRequestBody;
import es.serversurvival.v2.minecraftserver.empresas.contratar.ContratarEmpleadoWebActionRequestBody;
import es.serversurvival.v2.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.Getter;

public enum WebActionType {
    EMPRESAS_CONTRATAR("contratar", ContratarEmpleadoWebActionRequestBody.class),
    DEUDAS_EMITIR("emitir", EmitirDeudaWebActionRequestBody.class),
    DEUDAS_PRESTAR("prestar", PrestarWebActionRequestBody.class);

    @Getter private final String urlFrontend;
    @Getter private final Class<? extends WebActionRequestBody> requestBodyClass;

    WebActionType(String urlFrontend, Class<? extends WebActionRequestBody> requestBody) {
        this.urlFrontend = urlFrontend;
        this.requestBodyClass = requestBody;
    }
}
