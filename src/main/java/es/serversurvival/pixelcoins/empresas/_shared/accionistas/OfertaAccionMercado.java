package es.serversurvival.pixelcoins.empresas._shared.accionistas;

import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class OfertaAccionMercado extends Oferta {
    @Getter private final UUID empresaId;

    public OfertaAccionMercado(UUID ofertaId, UUID vendedorId, LocalDateTime fechaSubida, int cantidad, double precio,
                               String objeto, TipoOferta tipoOferta, UUID empresaId) {
        super(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta);
        this.empresaId = empresaId;
    }
}
