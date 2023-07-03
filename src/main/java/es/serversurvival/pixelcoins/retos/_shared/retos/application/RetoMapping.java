package es.serversurvival.pixelcoins.retos._shared.retos.application;

import es.serversurvival.pixelcoins.deudas.pagarcuotas.CuotaPagadaRetoProgresivoService;
import es.serversurvival.pixelcoins.deudas.prestar.DeudasPrestarAcredorRetoProgresivoService;
import es.serversurvival.pixelcoins.empresas.pagar.PagarEmpresaPagadoRetoProgresivoService;
import es.serversurvival.pixelcoins.empresas.pagar.PagarEmpresaPagadorRetoProgresivoService;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetoProgresivoService;
import es.serversurvival.pixelcoins.tienda.comprar.VenderTiendaRetoProgresivoService;
import lombok.Getter;

public enum RetoMapping {
    JUGADORES_PAGO_PAGADOR(1, null),
    JUGADORES_PAGO_PAGADO(2, null),
    JUGADORES_VENDER_JUGADOR_COMPRADOR(3, null),
    JUGADORES_VENDER_JUGADOR_VENDEDOR(4, null),
    JUGADORES_CAMBIO_INGRESAR_DIAMANTE(5, null),
    JUGADORES_CAMBIO_INGRESAR_CUARZO(6, null),
    JUGADORES_CAMBIO_INGRESAR_LAPISLAZULI(7, null),

    EMPRESAS_CREAR(8, null),
        EMPRESAS_PAGAR_PAGADOR(0, PagarEmpresaPagadorRetoProgresivoService.class),
        EMPRESAS_PAGAR_PAGADO(0, PagarEmpresaPagadoRetoProgresivoService.class),
        EMPRESAS_CONTRATAR(9, null),
            EMPRESAS_CONTRATAR_PAGADOR_SUELDO(11, null),
        EMPRESAS_CONTRATADO(10, null),
            EMPRESAS_CONTRATADO_PAGADO_SUELDO(11, null),
        EMPRESAS_DEPOSITAR(11, null),
        EMPRESAS_BOLSA_IPO(12, null),
            EMPRESAS_BOLSA_EMITIR(13, null),
            EMPRESAS_BOLSA_REPARTIR_DIVIDENDOS(14, null),
            EMPRESAS_BOLSA_RECAUDAR_IPO(15, null),
            EMPRESAS_BOLSA_RECAUDAR_EMISION(15, null),
        EMPRESAS_ACCIONISTAS_COMPRAR(17, null),
        EMPRESAS_ACCIONISTAS_INICIAR_VOTACION(18, null),
        EMPRESAS_ACCIONISTAS_VOTAR(19, null),
        EMPRESAS_ACCIONISTAS_VENTA(20, null),

    DEUDAS_PRESTAR_ACREDOR(0, DeudasPrestarAcredorRetoProgresivoService.class),
    DEUDAS_PRESTAR_DEUDOR(0, null),
    DEUDAS_PRESTAR_COBRO_CUOTAS(0, CuotaPagadaRetoProgresivoService.class),
    DEUDAS_COMPRAR(0, null),
    DEUDAS_VENDER(0, null),
    DEUDAS_PAGADA_ENTERA_SIN_NINPAGOS(0, null),

    BOLSA_ABRIR_LARGO(0, null),
    BOLSA_ABRIR_CORTO(0, null),
    BOLSA_CERRAR_RENTABILIDAD(0, null),

    TIENDA_VENDER_PIXELCOINS(0, VenderTiendaRetoProgresivoService.class)
    ;

    @Getter private final Class<? extends RetoProgresivoService> retoProgresivoService;
    @Getter private final int retoId;

    RetoMapping(int retoId, Class<? extends RetoProgresivoService> retoProgresivoService) {
        this.retoProgresivoService = retoProgresivoService;
        this.retoId = retoId;
    }
}
