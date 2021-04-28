package es.serversurvival.deudas.pagar;

import es.serversurvival.deudas.mysql.Deuda;
import es.serversurvival.Pixelcoin;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

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
