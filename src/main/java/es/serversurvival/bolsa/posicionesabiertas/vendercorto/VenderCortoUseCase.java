package es.serversurvival.bolsa.posicionesabiertas.vendercorto;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.SupportedTipoActivo;
import es.serversurvival.bolsa.posicionescerradas._shared.domain.TipoPosicion;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.jugadores._shared.application.JugadoresService;

import static es.serversurvival._shared.utils.Funciones.*;
import static es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce.PORCENTAJE_CORTO;

public final class VenderCortoUseCase {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final JugadoresService jugadoresService;

    public VenderCortoUseCase () {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.jugadoresService= DependecyContainer.get(JugadoresService.class);
    }

    public void venderEnCortoBolsa (String jugadorNombre, String ticker, String nombreValor, int cantidad, double precioPorAccion) {
        var jugador = this.jugadoresService.getByNombre(jugadorNombre);
        double valorTotal = precioPorAccion * cantidad;
        double comision = redondeoDecimales(reducirPorcentaje(valorTotal, 100 - PORCENTAJE_CORTO), 2);

        this.posicionesAbiertasSerivce.save(jugadorNombre, SupportedTipoActivo.ACCIONES, ticker, cantidad, precioPorAccion, TipoPosicion.CORTO);

        jugadoresService.save(jugador.decrementPixelcoinsBy(comision).incrementGastosBy(comision));

        Pixelcoin.publish(new PosicionVentaCortoEvento(jugadorNombre, precioPorAccion, cantidad, comision, ticker, SupportedTipoActivo.ACCIONES, nombreValor));
    }
}
