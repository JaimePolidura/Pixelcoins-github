package es.serversurvival.v2.pixelcoins.empresas.sacar;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.EmpresasService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class PixelcoinsSacadasEmpresa extends PixelcoinsEvento {
    @Getter private final UUID empresaId;
    @Getter private final double pixelcoins;
}
