package es.serversurvival.bolsa.other.comprarcorto;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.posicionesabiertas._shared.newformat.application.PosicionesAbiertasSerivce;
import es.serversurvival.bolsa.posicionesabiertas._shared.newformat.domain.PosicionAbierta;
import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores._shared.domain.Jugador;

public final class ComprarCortoUseCase implements AllMySQLTablesInstances {
    private final JugadoresService jugadoresService;
    private final PosicionesAbiertasSerivce posicionesAbiertasSerivce;

    public ComprarCortoUseCase() {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.posicionesAbiertasSerivce = DependecyContainer.get(PosicionesAbiertasSerivce.class);
    }

    public void comprarPosicionCorto (PosicionAbierta posicionAComprar, int cantidad, String jugadorNombre) {
        double precioPorAccion = llamadasApiMySQL.getLlamadaAPI(posicionAComprar.getNombreActivo()).getPrecio();

        String ticker = posicionAComprar.getNombreActivo();
        String nombreValor = llamadasApiMySQL.getLlamadaAPI(ticker).getNombre_activo();
        Jugador jugador = this.jugadoresService.getByNombre(jugadorNombre);
        int nAccionesTotlaesEnCartera = posicionAComprar.getCantidad();
        double precioApertura = posicionAComprar.getPrecioApertura();
        double revalorizacionTotal = (posicionAComprar.getPrecioApertura() - precioPorAccion) * cantidad;
        String fechaApertura = posicionAComprar.getFechaApertura();
        double rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioPorAccion, precioApertura), 3);

        if (cantidad == nAccionesTotlaesEnCartera)
            posicionesAbiertasSerivce.deleteById(posicionAComprar.getPosicionAbiertaId());
        else
            posicionesAbiertasSerivce.save(posicionAComprar.withCantidad(nAccionesTotlaesEnCartera - cantidad));

        double pixelcoinsJugador = jugador.getPixelcoins();

        if(0 > pixelcoinsJugador + revalorizacionTotal)
            jugadoresService.save(jugador.incrementPixelcoinsBy(revalorizacionTotal).incrementGastosBy(revalorizacionTotal));
        else
            jugadoresService.save(jugador.incrementPixelcoinsBy(revalorizacionTotal).incrementIngresosBy(revalorizacionTotal));


        Pixelcoin.publish(new PosicionCompraCortoEvento(jugadorNombre, ticker, nombreValor, precioApertura, fechaApertura, precioPorAccion, cantidad, rentabilidad, TipoActivo.ACCIONES));
    }
}
