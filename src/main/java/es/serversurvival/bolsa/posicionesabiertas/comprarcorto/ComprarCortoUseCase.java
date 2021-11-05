package es.serversurvival.bolsa.posicionesabiertas.comprarcorto;

import es.serversurvival.Pixelcoin;
import es.serversurvival.bolsa.llamadasapi.mysql.TipoActivo;
import es.serversurvival.bolsa.posicionesabiertas.mysql.PosicionAbierta;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.shared.utils.Funciones;

public final class ComprarCortoUseCase implements AllMySQLTablesInstances {
    public static final ComprarCortoUseCase INSTANCE = new ComprarCortoUseCase();

    private ComprarCortoUseCase () {}

    public void comprarPosicionCorto (PosicionAbierta posicionAComprar, int cantidad, String playername) {
        int idPosiconAbierta = posicionAComprar.getId();
        double precioPorAccion = llamadasApiMySQL.getLlamadaAPI(posicionAComprar.getNombre_activo()).getPrecio();

        String ticker = posicionAComprar.getNombre_activo();
        int nAccionesTotlaesEnCartera = posicionAComprar.getCantidad();
        double precioApertura = posicionAComprar.getPrecio_apertura();
        String fechaApertura = posicionAComprar.getFecha_apertura();
        double rentabilidad = Funciones.redondeoDecimales(Funciones.diferenciaPorcntual(precioPorAccion, precioApertura), 3);

        if (cantidad == nAccionesTotlaesEnCartera)
            posicionesAbiertasMySQL.borrarPosicionAbierta(idPosiconAbierta);
        else
            posicionesAbiertasMySQL.setCantidad(idPosiconAbierta, nAccionesTotlaesEnCartera - cantidad);

        String nombreValor = llamadasApiMySQL.getLlamadaAPI(ticker).getNombre_activo();

        Pixelcoin.publish(new PosicionCompraCortoEvento(playername, ticker, nombreValor, precioApertura, fechaApertura, precioPorAccion, cantidad, rentabilidad, TipoActivo.ACCIONES));
    }
}
