package es.serversurvival.minecraftserver.deudas.prestar;

import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class PrestarWebActionRequestBody extends WebActionRequestBody {
    @Getter private final String deudorJugadorNombre;
    @Getter private final double nominal;
    @Getter private final double interes;
    @Getter private final int numeroCuotasTotales;
    @Getter private final long periodoPagoCuota;

    public PrestarConfirmacionMenu.PrestarConfirmacionMenuState toPrestarConfirmacionMenuState(UUID acredorJugadorId) {
        return new PrestarConfirmacionMenu.PrestarConfirmacionMenuState(acredorJugadorId, nominal, interes, numeroCuotasTotales, periodoPagoCuota);
    }
}
