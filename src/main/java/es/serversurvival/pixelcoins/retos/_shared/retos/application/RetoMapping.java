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
    JUGADORES_PAGO_PAGADOR(null), 
    JUGADORES_VENDER_JUGADOR_VENDEDOR(null), 
    JUGADORES_CAMBIO_INGRESAR_DIAMANTE(null), 
    JUGADORES_CAMBIO_INGRESAR_CUARZO(null), 
    JUGADORES_CAMBIO_INGRESAR_LAPISLAZULI(null), 
    JUGADORES_PATRIMONIO(JugadoresPatriomnioRetoProgresivoService.class),

    EMPRESAS_CREAR(null),
    EMPRESAS_PAGAR_PAGADOR(PagarEmpresaPagadorRetoProgresivoService.class),
    EMPRESAS_PAGAR_PAGADO(PagarEmpresaPagadoRetoProgresivoService.class),
    EMPRESAS_CONTRATAR(null),
    EMPRESAS_CONTRATAR_PAGADOR_SUELDO(null),
    EMPRESAS_CONTRATADO(null),
    EMPRESAS_CONTRATADO_PAGADO_SUELDO(null),
    EMPRESAS_DEPOSITAR(null),
    EMPRESAS_BOLSA_IPO(null),
    EMPRESAS_BOLSA_EMITIR(null),
    EMPRESAS_BOLSA_REPARTIR_DIVIDENDOS(null),
    EMPRESAS_BOLSA_RECAUDAR_IPO(null),
    EMPRESAS_BOLSA_RECAUDAR_EMISION(null),
    EMPRESAS_ACCIONISTAS_COMPRAR(null),
    EMPRESAS_ACCIONISTAS_RECIBIR_DIVIDENDO(null),
    EMPRESAS_ACCIONISTAS_INICIAR_VOTACION(null),
    EMPRESAS_ACCIONISTAS_VOTAR(null),
    EMPRESAS_ACCIONISTAS_VENTA(null),

    DEUDAS_PRESTAR_ACREDOR(DeudasPrestarAcredorRetoProgresivoService.class), 
    DEUDAS_PRESTAR_DEUDOR(null), 
    DEUDAS_PRESTAR_COBRO_CUOTAS(CuotaPagadaRetoProgresivoService.class), 
    DEUDAS_COMPRAR(null), 
    DEUDAS_VENDER(null), 
    DEUDAS_PAGADA_ENTERA_SIN_NINPAGOS(null), 

    BOLSA_ABRIR_LARGO(null),
    BOLSA_ABRIR_CORTO(null), 
    BOLSA_CERRAR_RENTABILIDAD(BolsaCerrarRentabilidadRetoProgresivoService.class),

    TIENDA_VENDER_VOLUMEN(VenderTiendaVolumenRetoProgresivoService.class), 
    TIENDA_VENDER_CANTIDAD(VenderTiendaVolumenRetoProgresivoService.class) 
    ;

    @Getter private final Class<? extends RetoProgresivoService> retoProgresivoService;

    RetoMapping(Class<? extends RetoProgresivoService> retoProgresivoService) {
        this.retoProgresivoService = retoProgresivoService;
    }
}
