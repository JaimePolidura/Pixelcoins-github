package es.serversurvival.v2.pixelcoins.mercado._shared;

import es.serversurvival.v2.pixelcoins.deudas.comprar.secundario.OfertaDeudaMercadoSecundario;
import es.serversurvival.v2.pixelcoins.deudas.comprar.primario.OfertaDeudaMercadoPrimario;
import es.serversurvival.v2.pixelcoins.transacciones.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TipoOferta {
    DEUDA_MERCADO_SECUNDARIO(TipoTransaccion.DEUDAS_MERCADO_SECUNDARIO_COMPRA, true, OfertaDeudaMercadoSecundario.class),
    DEUDA_MERCADO_PRIMARIO(TipoTransaccion.DEUDAS_MERCADO_PRIMARIO_COMPRA, false, OfertaDeudaMercadoPrimario.class);

    @Getter private final TipoTransaccion tipoTransaccion;
    @Getter private final boolean objetoDeOfertaUnico;
    @Getter private final Class<? extends Oferta> ofertaClass;
}
