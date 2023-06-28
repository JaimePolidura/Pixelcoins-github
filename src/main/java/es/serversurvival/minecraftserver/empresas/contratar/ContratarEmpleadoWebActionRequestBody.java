package es.serversurvival.minecraftserver.empresas.contratar;

import es.serversurvival.minecraftserver.webaction.messages.WebActionFormParam;
import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ContratarEmpleadoWebActionRequestBody extends WebActionRequestBody {
    @Getter
    @WebActionFormParam(showPriory = 10, desc = "Nombre de la empresa donde vas a contratar el jugador")
    private String nombreDeLaEmpresa;

    @Getter
    @WebActionFormParam(showPriory = 9, desc = "Nombre del jugador a contratar. El jugador tiene que estar conectado al servidor")
    private String nombreJugadorContratar;

    @Getter
    @WebActionFormParam(showPriory = 8, desc = "Descripccion del puesto de trabajo")
    private String descripccion;

    @Getter
    @WebActionFormParam(showPriory = 8, desc = "Sueldo en PC que va a cobrar")
    private double sueldo;

    @Getter
    @WebActionFormParam(showPriory = 7, desc = "Cada cuantos dias va a cobrar el sueldo")
    private int periodoPagoEnDias;
}
