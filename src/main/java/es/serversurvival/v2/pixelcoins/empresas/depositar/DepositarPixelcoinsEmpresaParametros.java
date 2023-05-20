package es.serversurvival.v2.pixelcoins.empresas.depositar;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class DepositarPixelcoinsEmpresaParametros {
    @Getter private final double pixelcoins;
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
}
