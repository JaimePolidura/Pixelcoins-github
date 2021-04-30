package es.serversurvival.jugadores.setupjugadorunido;

import es.serversurvival.shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class JugadorCambiadoDeNombreEvento extends PixelcoinsEvento {
    @Getter private final String antiguoNombre;
    @Getter private final String nuevoNombre;
}
