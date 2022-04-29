package es.serversurvival.jugadores._shared.mySQL;

import es.serversurvival._shared.mysql.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class Jugador implements TablaObjeto {
    @Getter private final UUID userId;
    @Getter private final String nombre;
    @Getter private final double pixelcoins;
    @Getter private final int nVentas;
    @Getter private final double ingresos;
    @Getter private final double gastos;
    @Getter private final int nInpagosDeuda;
    @Getter private final int nPagosDeuda;
    @Getter private final int numeroVerificacionCuenta;
}
