package es.serversurvival.deudas.prestar;

import es.serversurvival.Pixelcoin;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.shared.utils.Funciones;

public final class PrestarUseCase implements AllMySQLTablesInstances {
    public static final PrestarUseCase INSTANCE = new PrestarUseCase();

    private PrestarUseCase () {}

    public void prestar (String acreador, String deudor, int pixelcoins, int interes, int dias) {
        int pixelcoinsMasInteres = Funciones.aumentarPorcentaje(pixelcoins, interes);

        deudasMySQL.nuevaDeuda(deudor, acreador, pixelcoinsMasInteres, dias, interes);

        Pixelcoin.publish(new PixelcoinsPrestadasEvento(acreador, deudor, pixelcoins, interes, dias));
    }
}
