package es.serversurvival.pixelcoins.empresas._shared.empleados.domain;

import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.pixelcoins.empresas.cambiardirector.CambiarDirectorVotacion;
import es.serversurvival.pixelcoins.empresas.contratar.ContratarEmpleadoParametros;
import es.serversurvival.pixelcoins.empresas.editarempleado.EditarEmpleadoParametros;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class Empleado {
    @Getter private UUID empleadoId;
    @Getter private UUID empleadoJugadorId;
    @Getter private UUID empresaId;
    @Getter private String descripccion;

    @Getter private double sueldo;
    @Getter private long periodoPagoMs;
    @Getter private LocalDateTime fechaUltimoPago;

    @Getter private LocalDateTime fechaContratacion;
    @Getter private boolean estaContratado;
    @Getter private LocalDateTime fechaDespido;
    @Getter private String causaDespido;

    public int periodoSueldoToDias() {
        return (int) (periodoPagoMs / (1000 * 60 * 60 * 24));
    }

    public Empleado marcarSueldoPagado() {
        this.fechaUltimoPago = fechaUltimoPago.plusNanos(periodoPagoMs * 1_000_000);
        return this;
    }

    public Empleado editar(EditarEmpleadoParametros parametros) {
        return new Empleado(empleadoId, empleadoJugadorId, empresaId, parametros.getNuevaDescripccion(), parametros.getNuevoSueldo(),
                parametros.getNuevoPeriodoPagoMs(), parametros.getNuevoPeriodoPagoMs() == periodoPagoMs ? LocalDateTime.now() : fechaUltimoPago ,
                fechaContratacion, estaContratado, fechaDespido, causaDespido);
    }

    public Empleado despedir(String causaDespido) {
        return new Empleado(empleadoId, empleadoJugadorId, empresaId, descripccion, sueldo, periodoPagoMs, fechaUltimoPago,
                fechaContratacion, false, LocalDateTime.now(), causaDespido);
    }

    public static Empleado fromVotacionCambiarDirector(CambiarDirectorVotacion votacion) {
        LocalDateTime fechaAhora = LocalDateTime.now();

        return Empleado.builder()
                .empleadoId(UUID.randomUUID())
                .empleadoJugadorId(votacion.getNuevoDirectorJugadorId())
                .empresaId(votacion.getEmpresaId())
                .descripccion("Director")
                .sueldo(votacion.getSueldo())
                .periodoPagoMs(votacion.getPeriodoPagoMs())
                .fechaUltimoPago(fechaAhora)
                .fechaContratacion(fechaAhora)
                .estaContratado(true)
                .fechaDespido(Funciones.NULL_LOCALDATETIME)
                .build();
    }

    public static Empleado fromContratarParametros(ContratarEmpleadoParametros parametros) {
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
                .fechaDespido(Funciones.NULL_LOCALDATETIME)
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
                .fechaDespido(Funciones.NULL_LOCALDATETIME)
                .estaContratado(true)
                .build();
    }
}
