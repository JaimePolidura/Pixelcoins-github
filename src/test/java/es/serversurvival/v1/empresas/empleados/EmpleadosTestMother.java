package es.serversurvival.v1.empresas.empleados;

import es.serversurvival.v1.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.v1.empresas.empleados._shared.domain.TipoSueldo;

import java.util.Date;
import java.util.UUID;

public final class EmpleadosTestMother {
    public static Empleado createEmpleado(String nombre, String empresa){
        return new Empleado(UUID.randomUUID(), nombre, empresa, 1, "trabajdo", TipoSueldo.DIA, new Date().toString());
    }

    public static Empleado createEmpleado(String nombre, String empresa, double sueldo){
        return new Empleado(UUID.randomUUID(), nombre, empresa, sueldo, null, TipoSueldo.DIA, new Date().toString());
    }

    public static Empleado createEmpleado(String nombre, String empresa, double sueldo, TipoSueldo tipoSueldo, String lastPaga){
        return new Empleado(UUID.randomUUID(), nombre, empresa, sueldo, null, tipoSueldo, lastPaga);
    }
}
