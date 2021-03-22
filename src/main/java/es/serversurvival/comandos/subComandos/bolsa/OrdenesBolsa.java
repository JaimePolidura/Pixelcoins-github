package es.serversurvival.comandos.subComandos.bolsa;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.menus.menus.BolsaOrdenesMenu;
import es.serversurvival.menus.menus.Clickable;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

@Command(name = "bolsa ordenes")
public class OrdenesBolsa implements CommandRunner {
    @Override
    public void execute(CommandSender player, String[] args) {
        MySQL.conectar();
        BolsaOrdenesMenu menu = new BolsaOrdenesMenu((Player) player);
        MySQL.desconectar();
    }
}
