package es.serversurvival.transacciones.mySQL;

import es.serversurvival._shared.mysql.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Transaccion implements TablaObjeto {
    @Getter private final int id;
    @Getter private final String fecha;
    @Getter private final String comprador;
    @Getter private final String vendedor;
    @Getter private final int cantidad;
    @Getter private final String objeto;
    @Getter private final TipoTransaccion tipo;
}
