package es.serversurvival.empresas.pagarsueldostask;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class ErrorPagandoSueldo extends PixelcoinsEvento {
    @Getter private final String empleado;
    @Getter private final String empresa;
    @Getter private final String razon;
}

