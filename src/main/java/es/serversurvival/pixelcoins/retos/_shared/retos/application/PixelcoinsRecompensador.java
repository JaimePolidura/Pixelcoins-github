package es.serversurvival.pixelcoins.retos._shared.retos.application;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.Reto;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.recompensas.RecompensadorReto;
import es.serversurvival.pixelcoins.transacciones.application.TransaccionesSaver;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import es.serversurvival.pixelcoins.transacciones.domain.Transaccion;
import lombok.AllArgsConstructor;

import java.util.UUID;

@Service
@AllArgsConstructor
public final class PixelcoinsRecompensador implements RecompensadorReto {
    private final TransaccionesSaver transaccionesSaver;

    @Override
    public void recompensar(UUID jugadorId, Reto reto) {
        transaccionesSaver.save(Transaccion.builder()
                        .pagadoId(jugadorId)
                        .objeto(reto.getRetoId())
                        .pixelcoins(reto.getRecompensaPixelcoins())
                        .tipo(TipoTransaccion.RETOS_RECOMPENSA_PIXELCOINS)
                .build());
    }
}
