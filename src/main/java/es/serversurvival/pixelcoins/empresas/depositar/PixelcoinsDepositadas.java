package es.serversurvival.pixelcoins.empresas.depositar;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class PixelcoinsDepositadas extends PixelcoinsEvento {
    @Getter private final UUID empresaId;
    @Getter private final double pixelcoins;
}
