package es.serversurvival.v2.pixelcoins.empresas.sacar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class SacarPixelcoinsEmpresaUseCaseParametros {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
    @Getter private final double pixelcoins;
}
