package es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain;

import es.serversurvival._shared.utils.Funciones;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public abstract class Votacion {
    @Getter private UUID votacionId;
    @Getter private UUID empresaId;
    @Getter private TipoVotacion tipo;
    @Getter private EstadoVotacion estado;
    @Getter private UUID iniciadoPorJugadorId;
    @Getter private LocalDateTime fechaFinalizacion;
    @Getter private String descripcion;
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
                .thenComparing(Votacion::getFechaInicio)
                .reversed();
    }

    public static abstract class AbstractVotacionBuilder<T extends AbstractVotacionBuilder> {
        protected UUID votacionId;
        protected UUID empresaId;
        protected TipoVotacion tipo;
        protected EstadoVotacion estado;
        protected LocalDateTime fechaFinalizacion;
        protected String descripcion;
        protected LocalDateTime fechaInicio;
        protected UUID iniciadoPorJugadorId;

        public AbstractVotacionBuilder() {
            this.votacionId = UUID.randomUUID();
            this.fechaInicio = LocalDateTime.now();
            this.estado = EstadoVotacion.ABIERTA;
            this.descripcion = "";
            this.fechaFinalizacion = Funciones.NULL_LOCALDATETIME;
        }

        public abstract Votacion build();

        public T descripccion(String descripccion){
            this.descripcion = descripccion;
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
