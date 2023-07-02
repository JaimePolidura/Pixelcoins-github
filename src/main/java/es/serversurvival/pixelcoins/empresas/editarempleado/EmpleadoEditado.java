package es.serversurvival.pixelcoins.empresas.editarempleado;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EmpleadoEditado extends PixelcoinsEvento {
    @Getter private final UUID empresaId;
    @Getter private final Empleado empleado;
    @Getter private final String nuevaDescripccion;
    @Getter private final double nuevoSueldo;
    @Getter private final long nuevoPeriodoPago;
}
