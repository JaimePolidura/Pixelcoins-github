package es.serversurvival.v2.pixelcoins.empresas._shared.accionistas;

import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;

import java.time.LocalDateTime;
import java.util.UUID;

public final class OfertaAccionMercadoEmision extends Oferta {
    public OfertaAccionMercadoEmision(UUID ofertaId, UUID vendedorId, LocalDateTime fechaSubida, int cantidad, double precio,
                                      String objeto, TipoOferta tipoOferta) {
        super(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta);
    }

    public static OfertaAccionMercadoEmision.OfertaAccionMercadoEmisionBuiler builder() {
        return new OfertaAccionMercadoEmisionBuiler();
    }

    public static class OfertaAccionMercadoEmisionBuiler extends Oferta.AbstractOfertaBuilder<OfertaAccionMercadoEmisionBuiler> {
        @Override
        public Oferta build() {
            return new OfertaAccionMercadoEmision(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta);
        }
    }
}
