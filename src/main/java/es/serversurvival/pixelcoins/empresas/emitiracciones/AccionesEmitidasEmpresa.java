package es.serversurvival.pixelcoins.empresas.emitiracciones;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class AccionesEmitidasEmpresa extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final UUID jugadorId;
    @Getter private final UUID empresaId;
    @Getter private final int nAccionesAEmitir;
    @Getter private final double precioPorAccion;


    @Override
    public Map<UUID, List<RetoMapping>> retosByJugadorId() {
        return Map.of(
                jugadorId, List.of(RetoMapping.EMPRESAS_BOLSA_EMITIR)
        );
    }
}
