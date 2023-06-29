package es.serversurvival.pixelcoins.empresas.ipo;

import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercado;
import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
public final class OfertaAccionMercadoIPO extends OfertaAccionMercado {
    public OfertaAccionMercadoIPO(UUID ofertaId, UUID vendedorId, LocalDateTime fechaSubida, int cantidad, double precio,
                                  String objeto, TipoOferta tipoOferta, UUID empresaId) {
        super(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta, empresaId);
    }

    public static OfertaAccionMercadoIPO.OfertaAccionMercadoIPOBuilder builder() {
        return new OfertaAccionMercadoIPO.OfertaAccionMercadoIPOBuilder();
    }

    public static class OfertaAccionMercadoIPOBuilder extends AbstractOfertaBuilder<OfertaAccionMercadoIPOBuilder> {
        private UUID accionesFundadorId;
        private UUID empresaId;

        public OfertaAccionMercadoIPOBuilder accionesFundadorId(UUID accionesFundadorId) {
            this.accionesFundadorId = accionesFundadorId;
            return this;
        }

        public OfertaAccionMercadoIPOBuilder empresaId(UUID empresaId) {
            this.empresaId = empresaId;
            return this;
        }

        @Override
        public Oferta build() {
            return new OfertaAccionMercadoIPO(ofertaId, empresaId, fechaSubida, cantidad, precio, accionesFundadorId.toString(), TipoOferta.ACCIONES_SERVER_IPO, empresaId);
        }
    }

}
