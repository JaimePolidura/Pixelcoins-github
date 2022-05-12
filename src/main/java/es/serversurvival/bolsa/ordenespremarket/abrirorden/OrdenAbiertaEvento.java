package es.serversurvival.bolsa.ordenespremarket.abrirorden;

import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class OrdenAbiertaEvento extends PixelcoinsEvento {
    @Getter private final String playerName;
    @Getter private final String ticker;
    @Getter private final int cantidad;
    @Getter private final TipoAccion tipoOrden;
    @Getter private final UUID idPosicionabierta;
}
