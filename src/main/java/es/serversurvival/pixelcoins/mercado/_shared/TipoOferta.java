package es.serversurvival.pixelcoins.mercado._shared;

import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercadoSecundario;
import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercadoPrimario;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoJugador;
import es.serversurvival.pixelcoins.empresas._shared.accionistas.OfertaAccionMercadoEmision;
import es.serversurvival.pixelcoins.empresas.ipo.OfertaAccionMercadoIPO;
import es.serversurvival.pixelcoins.tienda._shared.OfertaTiendaItemMinecraft;
import es.serversurvival.pixelcoins.transacciones.domain.TipoTransaccion;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TipoOferta {
    ACCIONES_SERVER_EMISION(TipoTransaccion.EMPRESAS_ACCIONES_COMPRA_EMISION, true, OfertaAccionMercadoEmision.class),
    ACCIONES_SERVER_JUGADOR(TipoTransaccion.EMPRESAS_ACCIONES_COMPRA_JUGADOR, true, OfertaAccionMercadoJugador.class),
    ACCIONES_SERVER_IPO(TipoTransaccion.EMPRESAS_ACCIONES_COMPRA_IPO, true, OfertaAccionMercadoIPO.class),
    DEUDA_MERCADO_SECUNDARIO(TipoTransaccion.DEUDAS_MERCADO_SECUNDARIO_COMPRA, false, OfertaDeudaMercadoSecundario.class),
    DEUDA_MERCADO_PRIMARIO(TipoTransaccion.DEUDAS_MERCADO_PRIMARIO_COMPRA, true, OfertaDeudaMercadoPrimario.class),
    TIENDA_ITEM_MINECRAFT(TipoTransaccion.TIENDA_ITEM_MINECRAFT_COMPRA, true, OfertaTiendaItemMinecraft.class);

    @Getter private final TipoTransaccion tipoTransaccion;
    @Getter private final boolean elObjetoPuedeEstarRepetido;
    @Getter private final Class<? extends Oferta> ofertaClass;
}
