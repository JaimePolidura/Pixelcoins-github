package es.serversurvival.nfs.bolsa.posicionesabiertas.comprarcorto;

import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.eventos.bolsa.PosicionCompraCortoEvento;
import es.serversurvival.nfs.Pixelcoin;
import es.serversurvival.nfs.bolsa.llamadasapi.TipoActivo;
import es.serversurvival.nfs.bolsa.posicionesabiertas.mysql.PosicionAbierta;

import static es.serversurvival.nfs.utils.Funciones.diferenciaPorcntual;
import static es.serversurvival.nfs.utils.Funciones.redondeoDecimales;

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
        double rentabilidad = redondeoDecimales(diferenciaPorcntual(precioPorAccion, precioApertura), 3);

        if (cantidad == nAccionesTotlaesEnCartera)
            posicionesAbiertasMySQL.borrarPosicionAbierta(idPosiconAbierta);
        else
            posicionesAbiertasMySQL.setCantidad(idPosiconAbierta, nAccionesTotlaesEnCartera - cantidad);

        String nombreValor = llamadasApiMySQL.getLlamadaAPI(ticker).getNombre_activo();

        Pixelcoin.publish(new PosicionCompraCortoEvento(playername, ticker, nombreValor, precioApertura, fechaApertura, precioPorAccion, cantidad, rentabilidad, TipoActivo.ACCIONES));
    }
}
