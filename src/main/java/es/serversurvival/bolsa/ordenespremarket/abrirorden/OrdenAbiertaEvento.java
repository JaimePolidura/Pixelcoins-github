package es.serversurvival.bolsa.ordenespremarket.abrirorden;

import es.serversurvival.bolsa.ordenespremarket.mysql.AccionOrden;
import es.serversurvival.shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class OrdenAbiertaEvento extends PixelcoinsEvento {
    @Getter private final String playerName;
    @Getter private final String ticker;
    @Getter private final int cantidad;
    @Getter private final AccionOrden tipoOrden;
    @Getter private final int id_posicionabierta;
}
