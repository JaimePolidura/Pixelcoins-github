package es.serversurvival.pixelcoins.empresas.pagarsueldos;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class SueldoEmpresaPagado extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaDirectorJugadorId;
    @Getter private final UUID empresaId;
    @Getter private final double sueldo;

    @Override
    public Map<UUID, RetoMapping> retosByJugadorId() {
        return Map.of(
                empresaDirectorJugadorId, RetoMapping.EMPRESAS_CONTRATAR_PAGADOR_SUELDO,
                jugadorId, RetoMapping.EMPRESAS_CONTRATADO_PAGADO_SUELDO
        );
    }
}
