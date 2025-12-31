package es.serversurvival.pixelcoins.empresas.adquirir.proponer;

import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.EstadoVotacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.TipoVotacion;
import es.serversurvival.pixelcoins.empresas._shared.votaciones._shared.votaciones.domain.Votacion;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class AceptarOfertaCompraEmpresaVotacion extends Votacion {
    private final UUID compradorId;
    private final boolean esJugador;
    private final double precioTotal;

    public AceptarOfertaCompraEmpresaVotacion(UUID votacionId, UUID empresaId, TipoVotacion tipo, EstadoVotacion estado,
                                              UUID iniciadoPorJugadorId, LocalDateTime fechaFinalizacion, String descripcion,
                                              LocalDateTime fechaInicio, UUID compradorId, boolean esJugador, double precioTotal) {
        super(votacionId, empresaId, tipo, estado, iniciadoPorJugadorId, fechaFinalizacion, descripcion, fechaInicio);
        this.compradorId = compradorId;
        this.esJugador = esJugador;
        this.precioTotal = precioTotal;
    }

    public static AceptarOfertaCompraEmpresaVotacion.AceptarOfertaCompraEmpresaVotacionBuilder builder() {
        return new AceptarOfertaCompraEmpresaVotacion.AceptarOfertaCompraEmpresaVotacionBuilder();
    }

    public static final class AceptarOfertaCompraEmpresaVotacionBuilder extends Votacion.AbstractVotacionBuilder<AceptarOfertaCompraEmpresaVotacion.AceptarOfertaCompraEmpresaVotacionBuilder> {
        private UUID compradorId;
        private boolean esJugador;
        private double precioTotal;

        public AceptarOfertaCompraEmpresaVotacionBuilder precioTotal(double precioTotal) {
            this.precioTotal = precioTotal;
            return this;
        }

        public AceptarOfertaCompraEmpresaVotacionBuilder elCompradorEsUnJugador(boolean elCompradorEsUnJugador) {
            this.esJugador = elCompradorEsUnJugador;
            return this;
        }

        public AceptarOfertaCompraEmpresaVotacionBuilder compradorId(UUID compradorId) {
            this.compradorId = compradorId;
            return this;
        }

        @Override
        public AceptarOfertaCompraEmpresaVotacion build() {
            return new AceptarOfertaCompraEmpresaVotacion(votacionId, empresaId, tipo, estado, iniciadoPorJugadorId, fechaFinalizacion,
                    descripcion, fechaInicio, compradorId, esJugador, precioTotal);
        }
    }
}
