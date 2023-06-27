package es.serversurvival.minecraftserver.deudas.prestar;

import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver.webaction.messages.WebActionRequestBody;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public final class PrestarWebActionRequestBody extends WebActionRequestBody {
    @Getter private String nombreDelJugadorPrestar;
    @Getter private int periodoPagoCuotaEnDias;
    @Getter private int numeroCuotasTotales;
    @Getter private double pixelcoins;
    @Getter private double interes;

    public PrestarConfirmacionMenu.PrestarConfirmacionMenuState toPrestarConfirmacionMenuState(UUID acredorJugadorId) {
        return new PrestarConfirmacionMenu.PrestarConfirmacionMenuState(acredorJugadorId, pixelcoins, interes / 100, numeroCuotasTotales, Funciones.diasToMillis(periodoPagoCuotaEnDias));
    }
}
