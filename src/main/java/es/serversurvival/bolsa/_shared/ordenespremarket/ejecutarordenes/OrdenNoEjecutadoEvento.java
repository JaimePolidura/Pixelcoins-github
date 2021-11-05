package es.serversurvival.bolsa._shared.ordenespremarket.ejecutarordenes;

import es.serversurvival.bolsa._shared.ordenespremarket.mysql.OrdenPreMarket;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class OrdenNoEjecutadoEvento extends PixelcoinsEvento {
    @Getter private final String jugador;
    @Getter private final OrdenPreMarket orden;
}
