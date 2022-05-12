package es.serversurvival.bolsa.other.comprarcorto;

import es.serversurvival.Pixelcoin;
import es.serversurvival.bolsa.posicionesabiertas._shared.newformat.domain.PosicionAbierta;
import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;

public final class ComprarCortoUseCase implements AllMySQLTablesInstances {
    public static final ComprarCortoUseCase INSTANCE = new ComprarCortoUseCase();

    private ComprarCortoUseCase () {}

    public void comprarPosicionCorto (PosicionAbierta posicionAComprar, int cantidad, String playername) {
        int idPosiconAbierta = posicionAComprar.getId();
        double precioPorAccion = llamadasApiMySQL.getLlamadaAPI(posicionAComprar.getNombreActivo()).getPrecio();

        String ticker = posicionAComprar.getNombreActivo();
        int nAccionesTotlaesEnCartera = posicionAComprar.getCantidad();
        double precioApertura = posicionAComprar.getPrecioApertura();
        String fechaApertura = posicionAComprar.getFechaApertura();
        double rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioPorAccion, precioApertura), 3);

        if (cantidad == nAccionesTotlaesEnCartera)
            posicionesAbiertasMySQL.borrarPosicionAbierta(idPosiconAbierta);
        else
            posicionesAbiertasMySQL.setCantidad(idPosiconAbierta, nAccionesTotlaesEnCartera - cantidad);

        String nombreValor = llamadasApiMySQL.getLlamadaAPI(ticker).getNombre_activo();

        Pixelcoin.publish(new PosicionCompraCortoEvento(playername, ticker, nombreValor, precioApertura, fechaApertura, precioPorAccion, cantidad, rentabilidad, TipoActivo.ACCIONES));
    }
}
