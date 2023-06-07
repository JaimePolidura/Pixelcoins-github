package es.serversurvival.pixelcoins.bolsa.cerrar;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class PosicionBolsaCerrada extends PixelcoinsEvento {
    @Getter private final UUID posicionCerradaId;
    @Getter private final UUID jugadorId;
}
