package es.serversurvival.pixelcoins.empresas.sacar;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class PixelcoinsSacadasEmpresa extends PixelcoinsEvento {
    @Getter private final UUID empresaId;
    @Getter private final double pixelcoins;
}
