package es.serversurvival.pixelcoins.empresas.repartirdividendos;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class TodosDividendosEmpresaRepartidos extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
    @Getter private final double dividendoPorAccion;

    @Override
    public Map<UUID, List<RetoMapping>> retosByJugadorId() {
        return Map.of(
                jugadorId, List.of(RetoMapping.EMPRESAS_BOLSA_REPARTIR_DIVIDENDOS)
        );
    }
}
