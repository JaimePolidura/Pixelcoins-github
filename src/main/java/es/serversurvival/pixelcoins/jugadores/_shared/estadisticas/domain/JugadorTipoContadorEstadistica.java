package es.serversurvival.pixelcoins.jugadores._shared.estadisticas.domain;

import lombok.Getter;

public enum JugadorTipoContadorEstadistica {
    N_DEUDA_PAGOS("nDeudaPagos"),
    N_DEUDA_INPAGOS("nDeudaInpagos"),
    TIENDA_VALOR_VENTAS("valorPixelcoinsVentasTienda"),
    TIENDA_VALOR_COMPRAS("valorPixelcoinsComprasTienda"),
    BOLSA_N_COMPRA_VENTAS("nCompraVentasBolsa");

    @Getter private final String nombreCampo;

    JugadorTipoContadorEstadistica(String nombreCampo) {
        this.nombreCampo = nombreCampo;
    }
}
