package es.serversurvival.empresas.empleados._shared.domain;

import es.jaime.javaddd.domain.Aggregate;
import es.serversurvival._shared.mysql.TablaObjeto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class Empleado extends Aggregate implements TablaObjeto {
    @Getter private final UUID empleadoId;
    @Getter private final String nombre;
    @Getter private final String empresa;
    @Getter private final double sueldo;
    @Getter private final String cargo;
    @Getter private final TipoSueldo tipoSueldo;
    @Getter private final String fechaUltimaPaga;

    public Empleado withSueldo(double sueldo){
        return new Empleado(empleadoId, nombre, empresa, sueldo, cargo, tipoSueldo, fechaUltimaPaga);
    }

    public Empleado withCargo(String cargo){
        return new Empleado(empleadoId, nombre, empresa, sueldo, cargo, tipoSueldo, fechaUltimaPaga);
    }

    public Empleado withTipoSueldo(TipoSueldo tipoSueldo){
        return new Empleado(empleadoId, nombre, empresa, sueldo, cargo, tipoSueldo, fechaUltimaPaga);
    }

    public Empleado withFechaUltimaPaga(String fechaUltimaPaga){
        return new Empleado(empleadoId, nombre, empresa, sueldo, cargo, tipoSueldo, fechaUltimaPaga);
    }

    public Empleado withNombre(String nombre){
        return new Empleado(empleadoId, nombre, empresa, sueldo, cargo, tipoSueldo, fechaUltimaPaga);
    }
}
