package es.serversurvival.v2.pixelcoins.empresas.repartirdividendos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class RepartirDividendosUseCaseParametros {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
    @Getter private final double dividendoPorAccion;
}
