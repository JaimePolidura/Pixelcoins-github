package es.serversurvival.v2.pixelcoins.empresas.editarempleado;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
public final class EditarEmpleadoParametros {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
    @Getter private final UUID empleadoIdEdtiar;
    @Getter private final String nuevaDescripccion;
    @Getter private final double nuevoSueldo;
    @Getter private final long nuevoPeriodoPago;
}
