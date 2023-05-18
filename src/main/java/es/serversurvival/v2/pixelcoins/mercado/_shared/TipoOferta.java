package es.serversurvival.v2.pixelcoins.mercado._shared;

import es.serversurvival.v2.pixelcoins.deudas.comprar.OfertaDeuda;
import es.serversurvival.v2.pixelcoins.transacciones.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TipoOferta {
    DEUDA(TipoTransaccion.DEUDAS_MERCADO_COMPRA, OfertaDeuda.class);

    @Getter private final TipoTransaccion tipoTransaccion;
    @Getter private final Class<? extends Oferta> ofertaClass;
}
