package es.serversurvival.nfs.deudas.pagar;

import es.serversurvival.nfs.Pixelcoin;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.nfs.deudas.mysql.Deuda;

public final class PagarDeudaUseCase implements AllMySQLTablesInstances {
    public static final PagarDeudaUseCase INSTANCE = new PagarDeudaUseCase();

    private PagarDeudaUseCase () {}

    //Devuelve la deuda pagada
    public Deuda pagarDeuda(String deudorNombre, int id) {
        Deuda deudaAPagar = deudasMySQL.getDeuda(id);
        int pixelcoinsDeuda = deudaAPagar.getPixelcoins_restantes();
        String acredorNombre = deudaAPagar.getAcredor();

        Jugadores.INSTANCE.realizarTransferenciaConEstadisticas(deudorNombre, acredorNombre, pixelcoinsDeuda);
        deudasMySQL.borrarDeuda(id);

        Pixelcoin.publish(new DeudaPagadaEvento(acredorNombre, deudorNombre, pixelcoinsDeuda));

        return deudaAPagar;
    }
}
