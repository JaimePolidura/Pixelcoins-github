package es.serversurvival.pixelcoins.deudas.ponerventa;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class PonerVentaDeudaParametros implements ParametrosUseCase {
    @Getter private final UUID jugadorId;
    @Getter private final UUID deudaId;
    @Getter private final double precio;
}
