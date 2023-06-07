package es.serversurvival.pixelcoins.empresas.cambiardirector;

import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.EstadoVotacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.TipoVotacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.Votacion;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

public final class CambiarDirectorVotacion extends Votacion {
    @Getter private final UUID nuevoDirectorJugadorId;
    @Getter private final long periodoPagoMs;
    @Getter private final double sueldo;

    public CambiarDirectorVotacion(UUID votacionId, UUID empresaId, TipoVotacion tipo, EstadoVotacion estado, UUID iniciadoPorJugadorId,
                                   LocalDateTime fechaFinalizacion, String descripccion, LocalDateTime fechaInicio, UUID nuevoDirectorJugadorId,
                                   double sueldo, long periodoPagoMs) {
        super(votacionId, empresaId, tipo, estado, iniciadoPorJugadorId, fechaFinalizacion, descripccion, fechaInicio);
        this.nuevoDirectorJugadorId = nuevoDirectorJugadorId;
        this.sueldo = sueldo;
        this.periodoPagoMs = periodoPagoMs;
    }

    public static CambiarDirectorVotacionBuilder builder() {
        return new CambiarDirectorVotacionBuilder();
    }

    public static final class CambiarDirectorVotacionBuilder extends Votacion.AbstractVotacionBuilder<CambiarDirectorVotacionBuilder> {
        private UUID nuevoDirectorJugadorId;
        private double sueldo;
        private long periodoPagoMs;

        public CambiarDirectorVotacionBuilder sueldo(double sueldo) {
            this.sueldo = sueldo;
            return this;
        }

        public CambiarDirectorVotacionBuilder periodoPagoMs(long periodoPagoMs) {
            this.periodoPagoMs = periodoPagoMs;
            return this;
        }

        public CambiarDirectorVotacionBuilder nuevoDirectorJugadorId(UUID nuevoDirectorJugadorId) {
            this.nuevoDirectorJugadorId = nuevoDirectorJugadorId;
            return this;
        }

        @Override
        public CambiarDirectorVotacion build() {
            return new CambiarDirectorVotacion(votacionId, empresaId, tipo, estado, iniciadoPorJugadorId, fechaFinalizacion,
                    descripccion, fechaInicio, nuevoDirectorJugadorId, sueldo, periodoPagoMs);
        }
    }
}
