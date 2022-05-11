package es.serversurvival.bolsa.ordenespremarket._shared.domain;

import es.serversurvival._shared.mysql.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class OrdenPremarket implements TablaObjeto {
    @Getter private final UUID orderPremarketId;
    @Getter private final String jugador;
    @Getter private final String nombreActivo;
    @Getter private final int cantidad;
    @Getter private final TipoAccion tipoAccion;
    @Getter private final UUID idPosicionabierta;

    public OrdenPremarket withJugador(String jugador){
        return new OrdenPremarket(
                orderPremarketId, jugador, nombreActivo, cantidad, tipoAccion, idPosicionabierta
        );
    }
}
