package es.serversurvival.bolsa.ordenespremarket.ejecutarordenes;

import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrdenPremarket;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class OrdenNoEjecutadoEvento extends PixelcoinsEvento {
    @Getter private final String jugador;
    @Getter private final OrdenPremarket orden;
}
