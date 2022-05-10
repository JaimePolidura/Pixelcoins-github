package es.serversurvival.jugadores.withers.sacarMaxItem;

import es.jaimetruman.ItemBuilder;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import es.serversurvival.jugadores.withers.CambioPixelcoins;
import es.serversurvival._shared.menus.inventory.InventoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SacarMaxItemInventoryFactory extends InventoryFactory {
    private final JugadoresService jugadoresService;

    public SacarMaxItemInventoryFactory (){
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 27, ChatColor.DARK_RED + "" + ChatColor.BOLD + "ELLIGE ITEM PARA SACR MAX");

        double dineroJugador = jugadoresService.getByNombre(jugador).getPixelcoins();

        inventory.setItem(4, buildItemInfo());
        inventory.setItem(10, buildItem(dineroJugador, "DIAMANTES", Material.DIAMOND_BLOCK, CambioPixelcoins.DIAMANTE));
        inventory.setItem(13, buildItem(dineroJugador, "LAPISLAZULI", Material.LAPIS_BLOCK, CambioPixelcoins.LAPISLAZULI));
        inventory.setItem(16, buildItem(dineroJugador, "CUARZO", Material.QUARTZ_BLOCK, CambioPixelcoins.CUARZO));

        return inventory;
    }

    private ItemStack buildItemInfo () {
        List<String> lore = new ArrayList<String>() {{
            add("Puedes convertir todas tus pixelcoins");
            add("en el mayor numero posible de diamantes");
            add("cuerzo o lapislazuli");
        }};

        return ItemBuilder.of(Material.PAPER).lore(lore).build();
    }

    private ItemStack buildItem (double pixelcoins, String item, Material material, int cambioPixelcoins) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "CLICK PARA SACAR MAXIMO DE " + item;
        List<String> lore = new ArrayList<String>() {{
            add(ChatColor.BLUE + "1 DE "+item+" -> " + ChatColor.GREEN + cambioPixelcoins + " PC");
            add("    ");
            add("Tus pixelcoins disponibles: " + ChatColor.GREEN + formatea.format(pixelcoins));
        }};

        return ItemBuilder.of(material).title(displayName).lore(lore).build();
    }
}
