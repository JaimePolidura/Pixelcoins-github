package es.serversurvival.minecraftserver.empresas.contratar;

import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ContratarEmpleadoWebActionRequestBody extends WebActionRequestBody {
    @Getter private final String nombreJugadorAContratar;
    @Getter private final String nombreDeLaEmpresa;
    @Getter private final String descripccion;
    @Getter private final double sueldo;
    @Getter private final long periodoPagoEnSegundos;
}
