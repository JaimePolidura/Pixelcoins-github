package es.serversurvival.minecraftserver.empresas.contratar;

import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ContratarEmpleadoWebActionRequestBody extends WebActionRequestBody {
    @Getter private String nombreJugadoraContratar;
    @Getter private String nombreDeLaEmpresa;
    @Getter private String descripccion;
    @Getter private double sueldo;
    @Getter private long periodoPagoEnDias;
}
