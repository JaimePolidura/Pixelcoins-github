package es.serversurvival.pixelcoins.deudas.prestar;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.deudas._shared.domain.Deuda;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class PixelcoinsPrestadasEvento extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final Deuda deuda;

    @Override
    public Map<UUID, List<RetoMapping>> retosByJugadorId() {
        return Map.of(
                deuda.getAcredorJugadorId(), List.of(RetoMapping.DEUDAS_PRESTAR_ACREDOR),
                deuda.getDeudorJugadorId(), List.of(RetoMapping.DEUDAS_PRESTAR_DEUDOR)
        );
    }
}
