package es.serversurvival.bolsa.other.venderlargo;

import es.serversurvival.bolsa.posicionesabiertas._shared.newformat.domain.PosicionAbierta;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.Pixelcoin;

import static es.serversurvival._shared.utils.Funciones.diferenciaPorcntual;
import static es.serversurvival._shared.utils.Funciones.redondeoDecimales;

public final class VenderLargoUseCase implements AllMySQLTablesInstances {
    public static final VenderLargoUseCase INSTANCE = new VenderLargoUseCase();

    private VenderLargoUseCase() {}
    
    public void venderPosicion(PosicionAbierta posicionAVender, int cantidad, String nombreJugador) {
        int idPosiconAbierta = posicionAVender.getId();
        double precioPorAccion = llamadasApiMySQL.getLlamadaAPI(posicionAVender.getNombreActivo()).getPrecio();

        String ticker = posicionAVender.getNombreActivo();
        int nAccionesTotlaesEnCartera = posicionAVender.getCantidad();
        double precioApertura = posicionAVender.getPrecioApertura();
        String fechaApertura = posicionAVender.getFechaApertura();
        double rentabilidad = redondeoDecimales(diferenciaPorcntual(precioApertura, precioPorAccion), 3);

        if (cantidad == nAccionesTotlaesEnCartera)
            posicionesAbiertasMySQL.borrarPosicionAbierta(idPosiconAbierta);
        else
            posicionesAbiertasMySQL.setCantidad(idPosiconAbierta, nAccionesTotlaesEnCartera - cantidad);

        String nombreValor = llamadasApiMySQL.getLlamadaAPI(ticker).getNombre_activo();

        Pixelcoin.publish(new PosicionVentaLargoEvento(nombreJugador, ticker, nombreValor, precioApertura, fechaApertura, precioPorAccion, cantidad, rentabilidad, posicionAVender.getTipoActivo()));
    }
}
