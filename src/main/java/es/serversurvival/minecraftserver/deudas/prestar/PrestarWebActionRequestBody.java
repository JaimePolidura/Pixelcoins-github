package es.serversurvival.minecraftserver.deudas.prestar;

import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class PrestarWebActionRequestBody extends WebActionRequestBody {
    @Getter private final String nombreDelJugadorAPrestar;
    @Getter private final double pixelcoins;
    @Getter private final double interes;
    @Getter private final int numeroCuotasTotales;
    @Getter private final long periodoPagoCuotaEnDias;

    public PrestarConfirmacionMenu.PrestarConfirmacionMenuState toPrestarConfirmacionMenuState(UUID acredorJugadorId) {
        return new PrestarConfirmacionMenu.PrestarConfirmacionMenuState(acredorJugadorId, pixelcoins, interes, numeroCuotasTotales, periodoPagoCuotaEnDias * 24 * 60 * 60 * 1000);
    }
}
