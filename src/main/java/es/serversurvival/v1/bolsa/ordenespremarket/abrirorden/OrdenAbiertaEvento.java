package es.serversurvival.v1.bolsa.ordenespremarket.abrirorden;

import es.serversurvival.v1.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class OrdenAbiertaEvento extends PixelcoinsEvento {
    @Getter private final String playerName;
    @Getter private final String ticker;
    @Getter private final int cantidad;
    @Getter private final TipoAccion tipoOrden;
    @Getter private final UUID idPosicionabierta;

    public static OrdenAbiertaEvento of(String playerName, String ticker, int cantidad, TipoAccion tipoAccion, UUID idPosicionabierta){
        return new OrdenAbiertaEvento(playerName, ticker, cantidad, tipoAccion, idPosicionabierta);
    }
}
