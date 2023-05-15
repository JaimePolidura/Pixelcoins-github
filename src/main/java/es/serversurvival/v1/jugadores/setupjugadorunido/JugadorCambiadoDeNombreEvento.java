package es.serversurvival.v1.jugadores.setupjugadorunido;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class JugadorCambiadoDeNombreEvento extends PixelcoinsEvento {
    @Getter private final String antiguoNombre;
    @Getter private final String nuevoNombre;
}
