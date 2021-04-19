package es.serversurvival.mySQL.eventos;

import es.jaime.Event;
import es.serversurvival.mySQL.enums.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class TransaccionEvent extends Event {
    @Getter private final String comprador;
    @Getter private final String vendedor;
    @Getter private final double cantidad;
    @Getter private final String objeto;
    @Getter private final TipoTransaccion tipoTransaccion;
}
