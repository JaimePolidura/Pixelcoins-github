package es.serversurvival.v2.pixelcoins.empresas._shared.accionistas;

import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

public final class OfertaAccionMercadoJugador extends OfertaAccionMercado {
    public OfertaAccionMercadoJugador(UUID ofertaId, UUID vendedorId, LocalDateTime fechaSubida, int cantidad, double precio,
                                      String objeto, TipoOferta tipoOferta, UUID empresaId) {
        super(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta, empresaId);
    }

    public static OfertaAccionMercadoJugadorBuiler builder() {
        return new OfertaAccionMercadoJugadorBuiler();
    }

    public static class OfertaAccionMercadoJugadorBuiler extends Oferta.AbstractOfertaBuilder<OfertaAccionMercadoJugadorBuiler> {
        private UUID empresaId;

        public OfertaAccionMercadoJugadorBuiler empresaId(UUID empresaId) {
            this.empresaId = empresaId;
            return this;
        }

        @Override
        public Oferta build() {
            return new OfertaAccionMercadoJugador(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta, empresaId);
        }
    }
}
