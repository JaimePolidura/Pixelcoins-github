package es.serversurvival.nfs.deudas.ver;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.nfs.shared.menus.inventory.InventoryFactory;
import es.serversurvival.nfs.deudas.mysql.Deuda;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DeudasInventoryFactory extends InventoryFactory {

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 54, DeudasMenu.tiulo);

        ItemStack info = buildItemInfo();
        ItemStack back = buildItemGoBack();
        List<ItemStack> itemsDeudas = buildItemsDeudas(jugador);

        inventory.setItem(0, info);
        inventory.setItem(53, back);

        for(int i = 0; i < itemsDeudas.size(); i++){
            if(i == 53) break;

            inventory.setItem(i + 9, itemsDeudas.get(i));
        }

        return inventory;
    }

    private List<ItemStack> buildItemsDeudas (String jugador) {
        List<ItemStack> itemsDeLasDeudas = new ArrayList<>();

        List<Deuda> deudasJugadorDebe = deudasMySQL.getDeudasDeudor(jugador);
        List<Deuda> deudasJugadorDeben = deudasMySQL.getDeudasAcredor(jugador);

        deudasJugadorDebe.forEach(deuda -> itemsDeLasDeudas.add(buildItemFromDeudaDeudor(deuda)));
        deudasJugadorDeben.forEach(deuda -> itemsDeLasDeudas.add(buildItemFromDeudaAcredor(deuda)));

        return itemsDeLasDeudas;
    }

    private ItemStack buildItemFromDeudaDeudor (Deuda deuda) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA PAGAR LA DEUDA";
        List<String> lore = new ArrayList<>();
        lore.add("   ");
        lore.add(ChatColor.GOLD + "Debes a: " + deuda.getAcredor());
        lore.add(ChatColor.GOLD + "Te queda de pagar: " + ChatColor.GREEN +  formatea.format(deuda.getPixelcoins_restantes()) + " PC" );
        lore.add(ChatColor.GOLD + "Vence en: " + deuda.getTiempo_restante() + " dias");
        lore.add(ChatColor.GOLD + "ID: " + deuda.getId());

        return ItemBuilder.of(Material.RED_BANNER).lore(lore).title(displayName).build();
    }

    private ItemStack buildItemFromDeudaAcredor (Deuda deuda) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA CANCELAR LA DEUDA";
        List<String> lore = new ArrayList<>();
        lore.add("   ");
        lore.add(ChatColor.GOLD + "Te debe: " + deuda.getDeudor());
        lore.add(ChatColor.GOLD + "Le queda de pagar: " + ChatColor.GREEN +  formatea.format(deuda.getPixelcoins_restantes()) + " PC" );
        lore.add(ChatColor.GOLD + "Vence en: " + deuda.getTiempo_restante() + " dias");
        lore.add(ChatColor.GOLD + "ID: " + deuda.getId());

        return ItemBuilder.of(Material.GREEN_BANNER).lore(lore).title(displayName).build();
    }

    private ItemStack buildItemInfo () {
        String displayName =  ChatColor.GOLD + "" + ChatColor.BOLD + "INFO";
        List<String> lore = new ArrayList<>();
        lore.add("Para condecer prestamos has de hacer");
        lore.add("/deudas prestar <jugador> <dinero> <dias> [interes]");
        lore.add("<jugador> -> al jugador que vas a endeudar");
        lore.add("<dinero> -> total de pixelcoins  a prestar");
        lore.add("<dias> -> dias en el que vencera el prestamo");
        lore.add("   cuando acabe de pagar la deuda el jugador");
        lore.add("[interes] -> porcentaje añadido a la cantidad");
        lore.add("de pixelcoins prestadas que te tendra que devolver");
        lore.add("en un plazo de tiempo puesto en <dias>.");
        lore.add("No es obligatorio, por defecto es 0");
        lore.add("   ");
        lore.add("Mas info en /deudas ayuda o en la web");
        lore.add("http://serversurvival.ddns.net/perfil");

        return ItemBuilder.of(Material.PAPER).lore(lore).title(displayName).build();
    }
}
