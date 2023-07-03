package es.serversurvival.pixelcoins.empresas.ipo;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class EmpresaIPORealizada extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
    @Getter private final int nAccionesAVender;
    @Getter private final double precioPorAccion;

    @Override
    public Map<UUID, RetoMapping> retosByJugadorId() {
        return Map.of(
                jugadorId, RetoMapping.EMPRESAS_BOLSA_IPO
        );
    }
}
