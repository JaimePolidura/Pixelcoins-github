package es.serversurvival.nfs.withers.ingresarItem;

import es.serversurvival.legacy.main.Pixelcoin;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.eventos.withers.ItemIngresadoEvento;
import es.serversurvival.legacy.mySQL.tablasObjetos.Jugador;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static es.serversurvival.legacy.mySQL.enums.CambioPixelcoins.getCambioTotal;

public final class IngresarItemUseCase implements AllMySQLTablesInstances {
    public void ingresarItem(ItemStack itemAIngresar, Player player) {
        Jugador jugador = jugadoresMySQL.getJugador(player.getName());

        int cantidad = itemAIngresar.getAmount();
        String nombreItem = itemAIngresar.getType().toString();
        double pixelcoinsAnadir = getCambioTotal(nombreItem, cantidad);
        double dineroActual = jugador.getPixelcoins();

        jugadoresMySQL.setPixelcoin(player.getName(), pixelcoinsAnadir + dineroActual);

        player.getInventory().clear(player.getInventory().getHeldItemSlot());

        Pixelcoin.publish(new ItemIngresadoEvento(jugador, pixelcoinsAnadir, nombreItem));
    }
}
