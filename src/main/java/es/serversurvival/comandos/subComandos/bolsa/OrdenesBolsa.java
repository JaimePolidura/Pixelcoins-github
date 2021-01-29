package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.menus.menus.BolsaOrdenesMenu;
import es.serversurvival.menus.menus.Clickable;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class OrdenesBolsa extends BolsaSubCommand {
    private final String sintaxis = "/bolsa ordenes";
    private final String ayuda = "ver todas las ordenes pendientes para ejecutar cuando el mercado abra";
    private final String SCNombre = "ordenes";

    @Override
    public String getSintaxis() {
        return sintaxis;
    }

    @Override
    public String getAyuda() {
        return ayuda;
    }

    @Override
    public String getSCNombre() {
        return SCNombre;
    }

    @Override
    public void execute(Player player, String[] args) {
        MySQL.conectar();
        BolsaOrdenesMenu menu = new BolsaOrdenesMenu(player);
        MySQL.desconectar();
    }
}
