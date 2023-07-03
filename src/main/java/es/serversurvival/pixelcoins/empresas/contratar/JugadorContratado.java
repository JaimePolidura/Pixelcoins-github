package es.serversurvival.pixelcoins.empresas.contratar;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class JugadorContratado extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final UUID jugadorIdContratador;
    @Getter private final UUID jugadorIdContratado;
    @Getter private final UUID empresaId;

    @Override
    public Map<UUID, RetoMapping> retosByJugadorId() {
        return Map.of(
                jugadorIdContratador, RetoMapping.EMPRESAS_CONTRATAR,
                jugadorIdContratado, RetoMapping.EMPRESAS_CONTRATADO
        );
    }
}
