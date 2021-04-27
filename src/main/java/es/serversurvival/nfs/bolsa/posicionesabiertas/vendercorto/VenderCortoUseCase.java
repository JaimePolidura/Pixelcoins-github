package es.serversurvival.nfs.bolsa.posicionesabiertas.vendercorto;

import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.eventos.bolsa.PosicionVentaCortoEvento;
import es.serversurvival.nfs.Pixelcoin;

import static es.serversurvival.nfs.bolsa.posicionescerradas.mysql.TipoPosicion.*;
import static es.serversurvival.nfs.bolsa.llamadasapi.mysql.TipoActivo.*;
import static es.serversurvival.nfs.bolsa.posicionesabiertas.mysql.PosicionesAbiertas.*;
import static es.serversurvival.nfs.utils.Funciones.redondeoDecimales;
import static es.serversurvival.nfs.utils.Funciones.reducirPorcentaje;

public final class VenderCortoUseCase implements AllMySQLTablesInstances {
    public static final VenderCortoUseCase INSTANCE = new VenderCortoUseCase();

    private VenderCortoUseCase () {}

    public void venderEnCortoBolsa (String playerName, String ticker, String nombreValor, int cantidad, double precioPorAccion) {
        double valorTotal = precioPorAccion * cantidad;
        double comision = redondeoDecimales(reducirPorcentaje(valorTotal, 100 - PORCENTAJE_CORTO), 2);

        posicionesAbiertasMySQL.nuevaPosicion(playerName, ACCIONES, ticker, cantidad, precioPorAccion, CORTO);

        Pixelcoin.publish(new PosicionVentaCortoEvento(playerName, precioPorAccion, cantidad, comision, ticker, ACCIONES, nombreValor));
    }
}
