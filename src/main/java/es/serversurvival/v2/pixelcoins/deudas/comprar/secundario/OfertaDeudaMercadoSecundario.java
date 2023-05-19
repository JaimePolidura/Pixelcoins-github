package es.serversurvival.v2.pixelcoins.deudas.comprar.secundario;

import es.serversurvival.v2.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.v2.pixelcoins.mercado._shared.TipoOferta;

import java.time.LocalDateTime;
import java.util.UUID;

public final class OfertaDeudaMercadoSecundario extends Oferta {
    public OfertaDeudaMercadoSecundario(UUID ofertaId, UUID vendedorId, LocalDateTime fechaSubida, int cantidad, double precio, String objeto, TipoOferta tipoOferta) {
        super(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta);
    }

    public static OfertaDeudaMercadoSecundarioBuilder builder() {
        return new OfertaDeudaMercadoSecundarioBuilder();
    }

    public static class OfertaDeudaMercadoSecundarioBuilder extends AbstractOfertaBuilder {
        @Override
        public Oferta build() {
            return new OfertaDeudaMercadoSecundario(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta);
        }
    }
}
