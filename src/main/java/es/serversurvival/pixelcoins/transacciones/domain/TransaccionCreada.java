package es.serversurvival.pixelcoins.transacciones.domain;

import es.serversurvival._shared.eventospixelcoins.InvocaAUnReto;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.retos._shared.retos.application.RetoMapping;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
public final class TransaccionCreada extends PixelcoinsEvento implements InvocaAUnReto {
    @Getter private final Transaccion transaccion;

    @Override
    public Map<UUID, List<RetoMapping>> retosByJugadorId() {
        Map<UUID, List<RetoMapping>> retos = new HashMap<>();

        if(transaccion.hasPagador()){
            retos.put(transaccion.getPagadorId(), List.of(RetoMapping.JUGADORES_PATRIMONIO));
        }
        if(transaccion.hasPagado()){
            retos.put(transaccion.getPagadoId(), List.of(RetoMapping.JUGADORES_PATRIMONIO));
        }

        return retos;
    }
}
