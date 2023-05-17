package es.serversurvival.v2.pixelcoins.empresas._shared.empleados;

import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.v2.pixelcoins.empresas.contratar.ContratarEmpleadoUseCaseParametros;
import es.serversurvival.v2.pixelcoins.empresas.editarempleado.EditarEmpleadoUseCaseParametros;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
public final class Empleado {
    @Getter private final UUID empleadoId;
    @Getter private final UUID empleadoJugadorId;
    @Getter private final UUID empresaId;
    @Getter private final String descripccion;

    @Getter private final double sueldo;
    @Getter private final long periodoPagoMs;
    @Getter private final LocalDateTime fechaUltimoPago;

    @Getter private final LocalDateTime fechaContratacion;
    @Getter private final boolean estaContratado;
    @Getter private final LocalDateTime fechaDespido;
    @Getter private final String causaDespido;

    public Empleado editar(EditarEmpleadoUseCaseParametros parametros) {
        return new Empleado(empleadoId, empleadoJugadorId, empresaId, parametros.getNuevaDescripccion(), parametros.getNuevoSueldo(),
                parametros.getNuevoPeriodoPago(), parametros.getNuevoPeriodoPago() == periodoPagoMs ? LocalDateTime.now() : fechaUltimoPago ,
                fechaContratacion, estaContratado, fechaDespido, causaDespido);
    }

    public Empleado despedir(String causaDespido) {
        return new Empleado(empleadoId, empleadoJugadorId, empresaId, descripccion, sueldo, periodoPagoMs, fechaUltimoPago,
                fechaContratacion, false, LocalDateTime.now(), causaDespido);
    }

    public static Empleado fromContratarParametros(ContratarEmpleadoUseCaseParametros parametros) {
        LocalDateTime fechaAhora = LocalDateTime.now();

        return Empleado.builder()
                .empleadoId(UUID.randomUUID())
                .empleadoJugadorId(parametros.getJugadorIdAContratar())
                .empresaId(parametros.getEmpresaId())
                .descripccion(parametros.getDescripccion())
                .sueldo(parametros.getSueldo())
                .periodoPagoMs(parametros.getPeriodoPagoMs())
                .fechaUltimoPago(fechaAhora)
                .fechaContratacion(fechaAhora)
                .estaContratado(true)
                .build();
    }

    public static Empleado fromDirectorEmpresa(Empresa empresa) {
        return Empleado.builder()
                .empleadoId(UUID.randomUUID())
                .empleadoJugadorId(empresa.getDirectorJugadorId())
                .empresaId(empresa.getEmpresaId())
                .descripccion("Director")
                .fechaContratacion(empresa.getFechaCreacion())
                .fechaUltimoPago(empresa.getFechaCreacion())
                .estaContratado(true)
                .build();
    }
}
