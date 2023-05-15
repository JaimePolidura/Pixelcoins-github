package es.serversurvival.v1.bolsa.ordenespremarket.abrirorden;

import es.serversurvival.v1.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class OrdenNoAbiertaEvento extends PixelcoinsEvento {
    @Getter private final String playerName;
    @Getter private final String ticker;
    @Getter private final int cantidad;
    @Getter private final TipoAccion tipoOrden;
    @Getter private final UUID id_posicionabierta;
}
