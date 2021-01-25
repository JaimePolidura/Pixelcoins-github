package es.serversurvival.mySQL.tablasObjetos;

import es.serversurvival.mySQL.Empleados;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class Empleado implements TablaObjeto{
    @Getter private final int id;
    @Getter private final String jugador;
    @Getter private final String empresa;
    @Getter private final double sueldo;
    @Getter private final String cargo;
    @Getter private final String tipo_sueldo;
    @Getter private final String fecha_ultimapaga;
}
