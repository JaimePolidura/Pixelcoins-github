package es.serversurvival.bolsa.ordenespremarket.ejecutarordenes;

import es.serversurvival.bolsa.ordenespremarket.mysql.OrdenPreMarket;
import es.serversurvival.shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class OrdenNoEjecutadoEvento extends PixelcoinsEvento {
    @Getter private final String jugador;
    @Getter private final OrdenPreMarket orden;
}
