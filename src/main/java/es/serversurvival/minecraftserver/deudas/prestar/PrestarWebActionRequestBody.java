package es.serversurvival.minecraftserver.deudas.prestar;

import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver.webaction.messages.WebActionFormParam;
import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public final class PrestarWebActionRequestBody extends WebActionRequestBody {
    @Getter
    @WebActionFormParam(showPriory = 10)
    private String nombreDelJugadorPrestar;

    @Getter
    @WebActionFormParam(desc = "Pixelcoins totales a prestar", showPriory = 9)
    private double pixelcoins;

    @Getter
    @WebActionFormParam(desc = "Interes de la deuda, expresado en %", showPriory = 8)
    private double interes;

    @Getter
    @WebActionFormParam(desc = "Cada cuantos dias el jugador te tendra que pagar", showPriory = 7)
    private int periodoPagoCuotaEnDias;

    @Getter
    @WebActionFormParam(desc = "Numero veces totales que el jugador tendra que pagarte", showPriory = 6)
    private int numeroCuotasTotales;

    public PrestarConfirmacionMenu.PrestarConfirmacionMenuState toPrestarConfirmacionMenuState(UUID acredorJugadorId) {
        return new PrestarConfirmacionMenu.PrestarConfirmacionMenuState(acredorJugadorId, pixelcoins, interes / 100, numeroCuotasTotales, Funciones.diasToMillis(periodoPagoCuotaEnDias));
    }
}
