package es.serversurvival.pixelcoins.bolsa.cerrar;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.bolsa._shared.premarket.application.AbrirOrdenPremarketCerrarParametros;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Builder
public final class CerrarPosicionParametros implements ParametrosUseCase {
    @Getter private final UUID jugadorId;
    @Getter private final int cantidad;
    @Getter private final UUID posicionAbiertaId;

    public AbrirOrdenPremarketCerrarParametros toAbrirOrdenPremarketCerrarParametros() {
        return new AbrirOrdenPremarketCerrarParametros(posicionAbiertaId, cantidad);
    }
}
