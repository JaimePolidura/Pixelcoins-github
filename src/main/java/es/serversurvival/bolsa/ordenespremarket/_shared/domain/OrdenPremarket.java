package es.serversurvival.bolsa.ordenespremarket._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class OrdenPremarket extends Aggregate {
    @Getter private final UUID orderPremarketId;
    @Getter private final String jugador;
    @Getter private final String nombreActivo;
    @Getter private final int cantidad;
    @Getter private final TipoAccion tipoAccion;
    @Getter private final UUID posicionAbiertaId;

    public OrdenPremarket withJugador(String jugador){
        return new OrdenPremarket(
                orderPremarketId, jugador, nombreActivo, cantidad, tipoAccion, posicionAbiertaId
        );
    }
}
