package es.serversurvival.pixelcoins.retos._shared.retos.application;

import es.serversurvival.pixelcoins.bolsa.cerrar.BolsaCerrarRentabilidadRetoProgresivoService;
import es.serversurvival.pixelcoins.deudas.pagarcuotas.CuotaPagadaRetoProgresivoService;
import es.serversurvival.pixelcoins.deudas.prestar.DeudasPrestarAcredorRetoProgresivoService;
import es.serversurvival.pixelcoins.empresas.pagar.PagarEmpresaPagadoRetoProgresivoService;
import es.serversurvival.pixelcoins.empresas.pagar.PagarEmpresaPagadorRetoProgresivoService;
import es.serversurvival.pixelcoins.jugadores.patrimonio.JugadoresPatriomnioRetoProgresivoService;
import es.serversurvival.pixelcoins.retos._shared.retos.domain.RetoProgresivoService;
import es.serversurvival.pixelcoins.tienda.comprar.VenderTiendaVolumenRetoProgresivoService;
import lombok.Getter;

public enum RetoMapping {
    JUGADORES_PAGO_PAGADOR(null), //ok
    JUGADORES_VENDER_JUGADOR_COMPRADOR(null), //ok
    JUGADORES_VENDER_JUGADOR_VENDEDOR(null), //ok
    JUGADORES_CAMBIO_INGRESAR_DIAMANTE(null), //ok
    JUGADORES_CAMBIO_INGRESAR_CUARZO(null), //ok
    JUGADORES_CAMBIO_INGRESAR_LAPISLAZULI(null), //ok
    JUGADORES_PATRIMONIO(JugadoresPatriomnioRetoProgresivoService.class),

    EMPRESAS_CREAR(null), //ok
        EMPRESAS_PAGAR_PAGADOR(PagarEmpresaPagadorRetoProgresivoService.class),
        EMPRESAS_PAGAR_PAGADO(PagarEmpresaPagadoRetoProgresivoService.class),
        EMPRESAS_CONTRATAR(null), //ok
            EMPRESAS_CONTRATAR_PAGADOR_SUELDO(null),//ok
        EMPRESAS_CONTRATADO(null),//ok
            EMPRESAS_CONTRATADO_PAGADO_SUELDO(null),//ok
        EMPRESAS_DEPOSITAR(null), //ok
        EMPRESAS_BOLSA_IPO(null), //ok
            EMPRESAS_BOLSA_EMITIR(null), //ok
            EMPRESAS_BOLSA_REPARTIR_DIVIDENDOS(null), //ok
            EMPRESAS_BOLSA_RECAUDAR_IPO(null), //ok
            EMPRESAS_BOLSA_RECAUDAR_EMISION(null), //ok
        EMPRESAS_ACCIONISTAS_COMPRAR(null), //ok
            EMPRESAS_ACCIONISTAS_RECIBIR_DIVIDENDO(null), //ok
            EMPRESAS_ACCIONISTAS_INICIAR_VOTACION(null), //ok
            EMPRESAS_ACCIONISTAS_VOTAR(null), //ok
            EMPRESAS_ACCIONISTAS_VENTA(null), //ok

    DEUDAS_PRESTAR_ACREDOR(DeudasPrestarAcredorRetoProgresivoService.class), //ok
    DEUDAS_PRESTAR_DEUDOR(null), //ok
    DEUDAS_PRESTAR_COBRO_CUOTAS(CuotaPagadaRetoProgresivoService.class), //ok
    DEUDAS_COMPRAR(null), //ok
    DEUDAS_VENDER(null), //ok
    DEUDAS_PAGADA_ENTERA_SIN_NINPAGOS(null), //ok

    BOLSA_ABRIR_LARGO(null),//ok
    BOLSA_ABRIR_CORTO(null), //ok
    BOLSA_CERRAR_RENTABILIDAD(BolsaCerrarRentabilidadRetoProgresivoService.class),//ok

    TIENDA_VENDER_VOLUMEN(VenderTiendaVolumenRetoProgresivoService.class), //ok
    TIENDA_VENDER_CANTIDAD(VenderTiendaVolumenRetoProgresivoService.class) //ok
    ;

    @Getter private final Class<? extends RetoProgresivoService> retoProgresivoService;

    RetoMapping(Class<? extends RetoProgresivoService> retoProgresivoService) {
        this.retoProgresivoService = retoProgresivoService;
    }
}
