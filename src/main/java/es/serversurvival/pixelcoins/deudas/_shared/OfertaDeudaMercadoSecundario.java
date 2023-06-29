package es.serversurvival.pixelcoins.deudas._shared;

import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
public final class OfertaDeudaMercadoSecundario extends OfertaDeudaMercado {
    public OfertaDeudaMercadoSecundario(UUID ofertaId, UUID vendedorId, LocalDateTime fechaSubida, int cantidad, double precio, String objeto, TipoOferta tipoOferta) {
        super(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta);
    }

    public static OfertaDeudaMercadoSecundarioBuilder builder() {
        return new OfertaDeudaMercadoSecundarioBuilder();
    }

    public static class OfertaDeudaMercadoSecundarioBuilder extends AbstractOfertaBuilder<OfertaDeudaMercadoSecundarioBuilder> {
        private UUID deudaId;

        public OfertaDeudaMercadoSecundarioBuilder deudaId(UUID deudaId) {
            this.deudaId = deudaId;
            return this;
        }

        @Override
        public Oferta build() {
            return new OfertaDeudaMercadoSecundario(ofertaId, vendedorId, fechaSubida, cantidad, precio, deudaId.toString(), TipoOferta.DEUDA_MERCADO_SECUNDARIO);
        }
    }
}
