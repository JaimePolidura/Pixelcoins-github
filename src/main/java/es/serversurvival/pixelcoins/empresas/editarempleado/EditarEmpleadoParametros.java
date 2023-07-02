package es.serversurvival.pixelcoins.empresas.editarempleado;

import es.serversurvival.pixelcoins._shared.usecases.ParametrosUseCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@AllArgsConstructor
public final class EditarEmpleadoParametros implements ParametrosUseCase {
    @Getter private final UUID empresaId;
    @Getter private final UUID jugadorId;
    @Getter private final UUID empleadoIdEdtiar;
    @Getter private final String nuevaDescripccion;
    @Getter private final double nuevoSueldo;
    @Getter private final long nuevoPeriodoPagoMs;
}
