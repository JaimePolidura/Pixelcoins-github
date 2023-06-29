package es.serversurvival.pixelcoins.empresas._shared.accionistas;

import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
public final class OfertaAccionMercadoJugador extends OfertaAccionMercado {
    public OfertaAccionMercadoJugador(UUID ofertaId, UUID vendedorId, LocalDateTime fechaSubida, int cantidad, double precio,
                                      String objeto, TipoOferta tipoOferta, UUID empresaId) {
        super(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta, empresaId);
    }

    public static OfertaAccionMercadoJugadorBuiler builder() {
        return new OfertaAccionMercadoJugadorBuiler();
    }

    public static class OfertaAccionMercadoJugadorBuiler extends Oferta.AbstractOfertaBuilder<OfertaAccionMercadoJugadorBuiler> {
        private UUID vendedorAccionesId;
        private UUID empresaId;

        public OfertaAccionMercadoJugadorBuiler vendedorAccionesId(UUID vendedorAccionesId) {
            this.vendedorAccionesId = vendedorAccionesId;
            return this;
        }

        public OfertaAccionMercadoJugadorBuiler empresaId(UUID empresaId) {
            this.empresaId = empresaId;
            return this;
        }

        @Override
        public Oferta build() {
            return new OfertaAccionMercadoJugador(ofertaId, vendedorId, fechaSubida, cantidad, precio, vendedorAccionesId.toString(), TipoOferta.ACCIONES_SERVER_JUGADOR, empresaId);
        }
    }
}
