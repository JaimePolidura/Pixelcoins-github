package es.serversurvival.nfs.bolsa.posicionesabiertas.venderlargo;

import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.eventos.bolsa.PosicionVentaLargoEvento;
import es.serversurvival.nfs.Pixelcoin;
import es.serversurvival.nfs.bolsa.posicionesabiertas.mysql.PosicionAbierta;

import static es.serversurvival.nfs.utils.Funciones.diferenciaPorcntual;
import static es.serversurvival.nfs.utils.Funciones.redondeoDecimales;

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
