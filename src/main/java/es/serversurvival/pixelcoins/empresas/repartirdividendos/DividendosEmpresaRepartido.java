package es.serversurvival.pixelcoins.empresas.repartirdividendos;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class DividendosEmpresaRepartido extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
    @Getter private final double dividendoPorAccion;

    @Override
    public Map<UUID, RetoMapping> retosByJugadorId() {
        return Map.of(
                jugadorId, RetoMapping.EMPRESAS_BOLSA_REPARTIR_DIVIDENDOS
        );
    }
}
