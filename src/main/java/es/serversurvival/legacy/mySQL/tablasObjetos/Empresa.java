package es.serversurvival.legacy.mySQL.tablasObjetos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Empresa implements TablaObjeto {
    @Getter private final int id;
    @Getter private final String nombre;
    @Getter private final String owner;
    @Getter private final double pixelcoins;
    @Getter private final double ingresos;
    @Getter private final double gastos;
    @Getter private final String icono;
    @Getter private final String descripcion;
    @Getter private final boolean cotizada;
}
