package es.serversurvival.bolsa.venderlargo;

import es.serversurvival.bolsa._shared.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.Pixelcoin;

import static es.serversurvival.shared.utils.Funciones.diferenciaPorcntual;
import static es.serversurvival.shared.utils.Funciones.redondeoDecimales;

public final class VenderLargoUseCase implements AllMySQLTablesInstances {
    public static final VenderLargoUseCase INSTANCE = new VenderLargoUseCase();

    private VenderLargoUseCase() {}
    
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
