package es.serversurvival.v2.pixelcoins.empresas._shared.accionistas;

import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

public final class OfertaAccionMercadoJugador extends Oferta {
    @Getter private final UUID accionistaJugadorId;
    @Getter private final UUID empresaId;

    public OfertaAccionMercadoJugador(UUID ofertaId, UUID vendedorId, LocalDateTime fechaSubida, int cantidad, double precio,
                                      String objeto, TipoOferta tipoOferta, UUID accionistaJugadorId, UUID empresaId) {
        super(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta);
        this.accionistaJugadorId = accionistaJugadorId;
        this.empresaId = empresaId;
    }

    public static OfertaAccionMercadoJugadorBuiler builder() {
        return new OfertaAccionMercadoJugadorBuiler();
    }

    public static class OfertaAccionMercadoJugadorBuiler extends Oferta.AbstractOfertaBuilder<OfertaAccionMercadoJugadorBuiler> {
        private UUID accionistaJugadorId;
        private UUID empresaId;

        public OfertaAccionMercadoJugadorBuiler empresaId(UUID empresaId) {
            this.empresaId = empresaId;
            return this;
        }

        public OfertaAccionMercadoJugadorBuiler accionistaJugadorId(UUID accionistaJugadorId) {
            this.accionistaJugadorId = accionistaJugadorId;
            return this;
        }

        @Override
        public Oferta build() {
            return new OfertaAccionMercadoJugador(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta, accionistaJugadorId, empresaId);
        }
    }
}
