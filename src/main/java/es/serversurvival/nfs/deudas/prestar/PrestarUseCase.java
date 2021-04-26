package es.serversurvival.nfs.deudas.prestar;

import es.serversurvival.nfs.Pixelcoin;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.nfs.utils.Funciones;

public final class PrestarUseCase implements AllMySQLTablesInstances {
    public static final PrestarUseCase INSTANCE = new PrestarUseCase();

    private PrestarUseCase () {}

    public void prestar (String acreador, String deudor, int pixelcoins, int interes, int dias) {
        int pixelcoinsMasInteres = Funciones.aumentarPorcentaje(pixelcoins, interes);

        Jugadores.INSTANCE.realizarTransferencia(acreador, deudor, pixelcoinsMasInteres);
        deudasMySQL.nuevaDeuda(deudor, acreador, pixelcoinsMasInteres, dias, interes);

        Pixelcoin.publish(new PixelcoinsPrestadasEvento(acreador, deudor, pixelcoins, interes, dias));
    }
}
