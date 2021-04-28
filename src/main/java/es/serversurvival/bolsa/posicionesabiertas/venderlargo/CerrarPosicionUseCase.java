package es.serversurvival.bolsa.posicionesabiertas.venderlargo;

import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.Pixelcoin;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionAbierta;

import static es.serversurvival.utils.Funciones.diferenciaPorcntual;
import static es.serversurvival.utils.Funciones.redondeoDecimales;

public final class CerrarPosicionUseCase implements AllMySQLTablesInstances {
    public static final CerrarPosicionUseCase INSTANCE = new CerrarPosicionUseCase();

    private CerrarPosicionUseCase () {}
    
    public void venderPosicion(PosicionAbierta posicionAVender, int cantidad, String nombreJugador) {
        int idPosiconAbierta = posicionAVender.getId();
        double precioPorAccion = llamadasApiMySQL.getLlamadaAPI(posicionAVender.getNombre_activo()).getPrecio();

        String ticker = posicionAVender.getNombre_activo();
        int nAccionesTotlaesEnCartera = posicionAVender.getCantidad();
        double precioApertura = posicionAVender.getPrecio_apertura();
        String fechaApertura = posicionAVender.getFecha_apertura();
        double rentabilidad = redondeoDecimales(diferenciaPorcntual(precioApertura, precioPorAccion), 3);

        if (cantidad == nAccionesTotlaesEnCartera)
            posicionesAbiertasMySQL.borrarPosicionAbierta(idPosiconAbierta);
        else
            posicionesAbiertasMySQL.setCantidad(idPosiconAbierta, nAccionesTotlaesEnCartera - cantidad);

        String nombreValor = llamadasApiMySQL.getLlamadaAPI(ticker).getNombre_activo();

        Pixelcoin.publish(new PosicionVentaLargoEvento(nombreJugador, ticker, nombreValor, precioApertura, fechaApertura, precioPorAccion, cantidad, rentabilidad, posicionAVender.getTipo_activo()));
    }
}
