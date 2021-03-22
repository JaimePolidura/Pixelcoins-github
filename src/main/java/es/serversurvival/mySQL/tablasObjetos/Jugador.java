package es.serversurvival.mySQL.tablasObjetos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Jugador implements TablaObjeto {
    @Getter private final String nombre;
    @Getter private final double pixelcoins;
    @Getter private final int nventas;
    @Getter private final double ingresos;
    @Getter private final double gastos;
    @Getter private final int ninpagos;
    @Getter private final int npagos;
    @Getter private final int numero_cuenta;
    @Getter private final String uuid;
}
