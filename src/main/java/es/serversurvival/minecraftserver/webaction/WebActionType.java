package es.serversurvival.minecraftserver.webaction;

import es.serversurvival.minecraftserver.deudas.emitir.EmitirDeudaWebActionRequestBody;
import es.serversurvival.minecraftserver.deudas.prestar.PrestarWebActionRequestBody;
import es.serversurvival.minecraftserver.empresas.contratar.ContratarEmpleadoWebActionRequestBody;
import es.serversurvival.minecraftserver.empresas.proponerdirector.ProponerDirectorWebActionRequestBody;
import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.Getter;

public enum WebActionType {
    EMPRESAS_PROPONER_DIRECTOR("proponerdirector", ProponerDirectorWebActionRequestBody.class),
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
