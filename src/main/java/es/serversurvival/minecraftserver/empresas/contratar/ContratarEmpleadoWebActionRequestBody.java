package es.serversurvival.minecraftserver.empresas.contratar;

import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ContratarEmpleadoWebActionRequestBody extends WebActionRequestBody {
    @Getter private final String jugadorNombreAContratar;
    @Getter private final String empresa;
    @Getter private final String descripccion;
    @Getter private final double sueldo;
    @Getter private final long periodoPagoMs;
}
