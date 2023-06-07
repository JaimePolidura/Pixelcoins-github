package es.serversurvival.pixelcoins.deudas._shared;

import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class OfertaDeudaMercado extends Oferta {
    public OfertaDeudaMercado(UUID ofertaId, UUID vendedorId, LocalDateTime fechaSubida, int cantidad, double precio, String objeto, TipoOferta tipoOferta) {
        super(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta);
    }

    public boolean esMercadoPrimario() {
        return getTipoOferta() == TipoOferta.DEUDA_MERCADO_PRIMARIO;
    }
}
