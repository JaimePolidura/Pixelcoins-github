package es.serversurvival.pixelcoins.empresas.pagar;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EmpresaPagada extends PixelcoinsEvento {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorPagadorId;
    @Getter private final double pixelcoins;
}
