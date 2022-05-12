package es.serversurvival.bolsa.posicionesabiertas.vendercorto;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.other._shared.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.bolsa.posicionesabiertas._shared.application.PosicionesAbiertasSerivce;
import es.serversurvival.jugadores._shared.application.JugadoresService;

public final class VenderCortoUseCase implements AllMySQLTablesInstances {
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;
    private final JugadoresService jugadoresService;

    public VenderCortoUseCase () {
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
        this.jugadoresService= DependecyContainer.get(JugadoresService.class);
    }

    public void venderEnCortoBolsa (String jugadorNombre, String ticker, String nombreValor, int cantidad, double precioPorAccion) {
        var jugador = this.jugadoresService.getByNombre(jugadorNombre);
        double valorTotal = precioPorAccion * cantidad;
        double comision = Funciones.redondeoDecimales(Funciones.reducirPorcentaje(valorTotal, 100 - PORCENTAJE_CORTO), 2);

        this.posicionesAbiertasSerivce.save(jugadorNombre, TipoActivo.ACCIONES, ticker, cantidad, precioPorAccion, TipoPosicion.CORTO);

        jugadoresService.save(jugador.decrementPixelcoinsBy(comision).incrementGastosBy(comision));

        Pixelcoin.publish(new PosicionVentaCortoEvento(jugadorNombre, precioPorAccion, cantidad, comision, ticker, TipoActivo.ACCIONES, nombreValor));
    }
}
