package es.serversurvival.pixelcoins.deudas.prestar;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.retos._shared.retos.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class PixelcoinsPrestadasEvento extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final Deuda deuda;

    @Override
    public Map<UUID, RetoMapping> retosByJugadorId() {
        return Map.of(
                deuda.getAcredorJugadorId(), RetoMapping.DEUDAS_PRESTAR_ACREDOR,
                deuda.getDeudorJugadorId(), RetoMapping.DEUDAS_PRESTAR_DEUDOR
        );
    }
}
