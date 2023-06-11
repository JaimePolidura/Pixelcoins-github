package es.serversurvival.minecraftserver.webaction;

import es.serversurvival.minecraftserver.deudas.emitir.EmitirDeudaWebActionRequestBody;
import es.serversurvival.minecraftserver.deudas.prestar.PrestarWebActionRequestBody;
import es.serversurvival.minecraftserver.empresas.contratar.ContratarEmpleadoWebActionRequestBody;
import es.serversurvival.minecraftserver.empresas.proponerdirector.ProponerDirectorWebActionRequestBody;
import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.Getter;

public enum WebActionType {
    EMPRESAS_PROPONER_DIRECTOR("Proponer director", ProponerDirectorWebActionRequestBody.class),
    EMPRESAS_CONTRATAR("Contratar", ContratarEmpleadoWebActionRequestBody.class),
    DEUDAS_EMITIR("Emitir deuda", EmitirDeudaWebActionRequestBody.class),
    DEUDAS_PRESTAR("Prestar", PrestarWebActionRequestBody.class);
    
    @Getter private final String nombre;
    @Getter private final Class<? extends WebActionRequestBody> requestBodyClass;

    WebActionType(String nombre, Class<? extends WebActionRequestBody> requestBody) {
        this.nombre = nombre;
        this.requestBodyClass = requestBody;
    }
}
