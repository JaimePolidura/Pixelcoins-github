package es.serversurvival.v2.pixelcoins.deudas.comprar;

import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;

import java.time.LocalDateTime;
import java.util.UUID;

public final class OfertaDeuda extends Oferta {
    public OfertaDeuda(UUID ofertaId, UUID vendedorId, LocalDateTime fechaSubida, int cantidad, double precio, String objeto, TipoOferta tipoOferta) {
        super(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta);
    }

    public static OfertaDeudaBuidler builder() {
        return new OfertaDeudaBuidler();
    }

    public static class OfertaDeudaBuidler extends AbstractOfertaBuilder {
        @Override
        public Oferta build() {
            return new OfertaDeuda(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta);
        }
    }

}
