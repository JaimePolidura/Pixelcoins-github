package es.serversurvival.pixelcoins.deudas._shared;

import es.serversurvival.pixelcoins.mercado._shared.Oferta;
import es.serversurvival.pixelcoins.mercado._shared.TipoOferta;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
public abstract class OfertaDeudaMercado extends Oferta {
    public OfertaDeudaMercado(UUID ofertaId, UUID vendedorId, LocalDateTime fechaSubida, int cantidad, double precio, String objeto, TipoOferta tipoOferta) {
        super(ofertaId, vendedorId, fechaSubida, cantidad, precio, objeto, tipoOferta);
    }

    public boolean esMercadoPrimario() {
        return getTipo() == TipoOferta.DEUDA_MERCADO_PRIMARIO;
    }
}
