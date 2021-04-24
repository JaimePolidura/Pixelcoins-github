package es.serversurvival.legacy.mySQL.tablasObjetos;

import es.serversurvival.legacy.mySQL.enums.TipoSueldo;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Empleado implements TablaObjeto{
    @Getter private final int id;
    @Getter private final String jugador;
    @Getter private final String empresa;
    @Getter private final double sueldo;
    @Getter private final String cargo;
    @Getter private final TipoSueldo tipo_sueldo;
    @Getter private final String fecha_ultimapaga;
}
