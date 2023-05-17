package es.serversurvival.v2.pixelcoins.empresas.repartirdividendos;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class DividendosEmpresaRepartido extends PixelcoinsEvento {
    @Getter private final UUID empresaId;
    @Getter private final double dividendoPorAccion;
}
