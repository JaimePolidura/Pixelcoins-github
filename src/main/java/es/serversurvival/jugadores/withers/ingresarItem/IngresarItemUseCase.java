package es.serversurvival.jugadores.withers.ingresarItem;

import es.serversurvival.Pixelcoin;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.jugadores.withers.CambioPixelcoins;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class IngresarItemUseCase implements AllMySQLTablesInstances {
    public void ingresarItem(ItemStack itemAIngresar, Player player) {
        Jugador jugador = jugadoresMySQL.getJugador(player.getName());

        int cantidad = itemAIngresar.getAmount();
        String nombreItem = itemAIngresar.getType().toString();
        double pixelcoinsAnadir = CambioPixelcoins.getCambioTotal(nombreItem, cantidad);
        double dineroActual = jugador.getPixelcoins();

        jugadoresMySQL.setPixelcoin(player.getName(), pixelcoinsAnadir + dineroActual);

        player.getInventory().clear(player.getInventory().getHeldItemSlot());

        Pixelcoin.publish(new ItemIngresadoEvento(jugador, pixelcoinsAnadir, nombreItem));
    }
}
