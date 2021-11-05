package es.serversurvival.deudas.cancelar;

import es.serversurvival.Pixelcoin;
import es.serversurvival.deudas._shared.mysql.Deuda;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import org.bukkit.entity.Player;


public final class CancelarDeudaUseCase implements AllMySQLTablesInstances {
    public static final CancelarDeudaUseCase INSTANCE = new CancelarDeudaUseCase();

    private CancelarDeudaUseCase () {}

    public Deuda cancelarDeuda(Player player, int id) {
        Deuda deudaACancelar = deudasMySQL.getDeuda(id);
        String nombreDeudor = deudaACancelar.getDeudor();

        int pixelcoinsDeuda = deudaACancelar.getPixelcoins_restantes();

        deudasMySQL.borrarDeuda(id);

        Pixelcoin.publish(new DeudaCanceladaEvento(player.getName(), nombreDeudor, pixelcoinsDeuda));

        return deudaACancelar;
    }
}
