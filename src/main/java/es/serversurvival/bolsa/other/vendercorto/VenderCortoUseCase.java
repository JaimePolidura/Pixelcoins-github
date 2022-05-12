package es.serversurvival.bolsa.other.vendercorto;

import es.serversurvival.Pixelcoin;
import es.serversurvival.bolsa.posicionesabiertas.old.mysql.PosicionesAbiertas;
import es.serversurvival.bolsa.other._shared.posicionescerradas.mysql.TipoPosicion;
import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.TipoActivo;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;

public final class VenderCortoUseCase implements AllMySQLTablesInstances {
    public static final VenderCortoUseCase INSTANCE = new VenderCortoUseCase();

    private VenderCortoUseCase () {}

    public void venderEnCortoBolsa (String playerName, String ticker, String nombreValor, int cantidad, double precioPorAccion) {
        double valorTotal = precioPorAccion * cantidad;
        double comision = Funciones.redondeoDecimales(Funciones.reducirPorcentaje(valorTotal, 100 - PosicionesAbiertas.PORCENTAJE_CORTO), 2);

        posicionesAbiertasMySQL.nuevaPosicion(playerName, TipoActivo.ACCIONES, ticker, cantidad, precioPorAccion, TipoPosicion.CORTO);

        Pixelcoin.publish(new PosicionVentaCortoEvento(playerName, precioPorAccion, cantidad, comision, ticker, TipoActivo.ACCIONES, nombreValor));
    }
}
