package es.serversurvival.pixelcoins.transacciones.domain;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class TransaccionCreada extends PixelcoinsEvento {
    @Getter private final Transaccion transaccion;
}
