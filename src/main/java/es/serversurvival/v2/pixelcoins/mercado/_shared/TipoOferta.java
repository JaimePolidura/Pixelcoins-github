package es.serversurvival.v2.pixelcoins.mercado._shared;

import es.serversurvival.v2.pixelcoins.deudas._shared.OfertaDeudaMercadoSecundario;
import es.serversurvival.v2.pixelcoins.deudas._shared.OfertaDeudaMercadoPrimario;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoJugador;
import es.serversurvival.v2.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoEmision;
import es.serversurvival.v2.pixelcoins.tienda._shared.OfertaTiendaItemMinecraft;
import es.serversurvival.v2.pixelcoins.transacciones.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TipoOferta {
    ACCIONES_SERVER_EMISION(TipoTransaccion.EMPRESAS_ACCIONES_COMPRA_EMISION, false, OfertaAccionMercadoEmision.class),
    ACCIONES_SERVER_JUGADOR(TipoTransaccion.EMPRESAS_ACCIONES_COMPRA_JUGADOR, true, OfertaAccionMercadoJugador.class),
    DEUDA_MERCADO_SECUNDARIO(TipoTransaccion.DEUDAS_MERCADO_SECUNDARIO_COMPRA, true, OfertaDeudaMercadoSecundario.class),
    DEUDA_MERCADO_PRIMARIO(TipoTransaccion.DEUDAS_MERCADO_PRIMARIO_COMPRA, false, OfertaDeudaMercadoPrimario.class),
    TIENDA_ITEM_MINECRAFT(TipoTransaccion.TIENDA_ITEM_MINECRAFT_COMPRA, false, OfertaTiendaItemMinecraft.class);

    @Getter private final TipoTransaccion tipoTransaccion;
    @Getter private final boolean objetoDeOfertaUnico;
    @Getter private final Class<? extends Oferta> ofertaClass;
}
