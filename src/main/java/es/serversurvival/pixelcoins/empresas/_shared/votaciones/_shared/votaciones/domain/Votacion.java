package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

@AllArgsConstructor
public abstract class Votacion {
    public static final long DEFAULT_VOTACION_TIME_OUT = 24 * 60 * 60 * 1000;
    public static final double DEFAULT_PORCENTAJE_ACCIONES_TOTALES_VOTACION = 0.8;

    @Getter private UUID votacionId;
    @Getter private UUID empresaId;
    @Getter private TipoVotacion tipo;
    @Getter private EstadoVotacion estado;
    @Getter private UUID iniciadoPorJugadorId;
    @Getter private LocalDateTime fechaFinalizacion;
    @Getter private String descripccion;
    @Getter private LocalDateTime fechaInicio;

    public Votacion finalizar(EstadoVotacion resultado) {
        this.estado = resultado;
        this.fechaFinalizacion = LocalDateTime.now();

        return this;
    }

    public boolean estaAbierta() {
        return this.estado == EstadoVotacion.ABIERTA;
    }

    public static Comparator<Votacion> sortByPrioridad() {
        return Comparator.<Votacion>comparingInt(a -> a.getEstado().getShowPriority())
                .thenComparing(Votacion::getFechaInicio);
    }

    public static abstract class AbstractVotacionBuilder<T extends AbstractVotacionBuilder> {
        protected UUID votacionId;
        protected UUID empresaId;
        protected TipoVotacion tipo;
        protected EstadoVotacion estado;
        protected LocalDateTime fechaFinalizacion;
        protected String descripccion;
        protected LocalDateTime fechaInicio;
        protected UUID iniciadoPorJugadorId;

        public AbstractVotacionBuilder() {
            this.votacionId = UUID.randomUUID();
            this.fechaInicio = LocalDateTime.now();
            this.estado = EstadoVotacion.ABIERTA;
            this.descripccion = "";
        }

        public abstract Votacion build();

        public T descripccion(String descripccion){
            this.descripccion = descripccion;
            return (T) this;
        }

        public T iniciadoPorJugadorId(UUID jugadorId) {
            this.iniciadoPorJugadorId = jugadorId;
            return (T) this;
        }

        public T tipo(TipoVotacion tipo){
            this.tipo = tipo;
            return (T) this;
        }

        public T empresaId(UUID empresaId) {
            this.empresaId = empresaId;
            return (T) this;
        }
    }
}
