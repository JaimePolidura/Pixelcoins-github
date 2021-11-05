package es.serversurvival.deudas.pagarTodo;

import es.serversurvival.deudas._shared.mysql.Deuda;
import es.serversurvival.Pixelcoin;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class PagarDeudaCompletaUseCase implements AllMySQLTablesInstances {
    public static final PagarDeudaCompletaUseCase INSTANCE = new PagarDeudaCompletaUseCase();

    private PagarDeudaCompletaUseCase() {}

    public Deuda pagarDeuda(int id) {
        Deuda deudaAPagar = deudasMySQL.getDeuda(id);
        int pixelcoinsDeuda = deudaAPagar.getPixelcoins_restantes();
        String acredorNombre = deudaAPagar.getAcredor();

        deudasMySQL.borrarDeuda(id);

        Pixelcoin.publish(new DeudaPagadaCompletaEvento(acredorNombre, deudaAPagar.getDeudor(), pixelcoinsDeuda));

        return deudaAPagar;
    }
}
