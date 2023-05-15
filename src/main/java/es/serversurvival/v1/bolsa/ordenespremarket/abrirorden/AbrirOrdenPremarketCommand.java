package es.serversurvival.v1.bolsa.ordenespremarket.abrirorden;

import es.serversurvival.v1.bolsa.ordenespremarket._shared.domain.TipoAccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class AbrirOrdenPremarketCommand {
    @Getter private final String jugador;
    @Getter private final String ticker;
    @Getter private final int cantidad;
    @Getter private final TipoAccion tipoAccion;
    @Getter private final UUID posicionAbiertaId;

    public static AbrirOrdenPremarketCommand of(String jugaodr, String ticker, int cantidad,
                                                TipoAccion tipoAccion, UUID posicionAbiertaId){
        return new AbrirOrdenPremarketCommand(jugaodr, ticker, cantidad, tipoAccion, posicionAbiertaId);
    }
}
