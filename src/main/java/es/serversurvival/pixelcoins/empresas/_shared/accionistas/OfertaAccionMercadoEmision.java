package es.serversurvival.pixelcoins.empresas._shared.accionistas;

import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
public final class OfertaAccionMercadoEmision extends OfertaAccionMercado {
    public OfertaAccionMercadoEmision(UUID ofertaId, UUID vendedorId, LocalDateTime fechaSubida, int cantidad, double precio,
                                      String objeto, TipoOferta tipoOferta, UUID empresaId) {
        super(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta, empresaId);
    }

    public static OfertaAccionMercadoEmision.OfertaAccionMercadoEmisionBuiler builder() {
        return new OfertaAccionMercadoEmisionBuiler();
    }

    public static class OfertaAccionMercadoEmisionBuiler extends AbstractOfertaBuilder<OfertaAccionMercadoEmisionBuiler> {
        private UUID empresaId;

        public OfertaAccionMercadoEmisionBuiler empresaId(UUID empresaId) {
            this.empresaId = empresaId;
            return this;
        }

        @Override
        public Oferta build() {
            return new OfertaAccionMercadoEmision(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta, empresaId);
        }
    }
}
