package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.menus.menus.DeudasMenu;
import es.serversurvival.mySQL.Deudas;
import es.serversurvival.mySQL.tablasObjetos.Deuda;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class DeudasInventoryFactory extends InventoryFactory {

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 54, DeudasMenu.tiulo);

        ItemStack info = buildItemInfo();
        ItemStack back = super.buildItemGoBack();
        List<ItemStack> itemsDeudas = buildItemsDeudas(jugador);

        inventory.setItem(0, info);
        for(int i = 0; i < itemsDeudas.size(); i++){
            if(i == 53) break;

            inventory.setItem(i + 9, itemsDeudas.get(i));
        }
        inventory.setItem(53, back);

        return inventory;
    }

    private ItemStack buildItemInfo () {
        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();

        infoMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "INFO");

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

        infoMeta.setLore(lore);
        info.setItemMeta(infoMeta);

        return info;
    }

    private List<ItemStack> buildItemsDeudas (String jugador) {
        ArrayList<ItemStack> itemsDeLasDeudas = new ArrayList<>();

        deudasMySQL.conectar();
        List<Deuda> deudasJugadorDebe = deudasMySQL.getDeudasDeudor(jugador);
        List<Deuda> deudasJugadorDeben = deudasMySQL.getDeudasAcredor(jugador);
        deudasMySQL.desconectar();

        for (Deuda deuda : deudasJugadorDebe) {
            ItemStack itemDeuda = new ItemStack(Material.RED_BANNER);
            ItemMeta deudaMeta = itemDeuda.getItemMeta();

            deudaMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA PAGAR LA DEUDA");
            List<String> lore = new ArrayList<>();
            lore.add("   ");
            lore.add(ChatColor.GOLD + "Debes a: " + deuda.getAcredor());
            lore.add(ChatColor.GOLD + "Te queda de pagar: " + ChatColor.GREEN +  formatea.format(deuda.getPixelcoins()) + " PC" );
            lore.add(ChatColor.GOLD + "Vence en: " + deuda.getTiempo() + " dias");
            lore.add(ChatColor.GOLD + "ID: " + deuda.getId_deuda());
            deudaMeta.setLore(lore);
            itemDeuda.setItemMeta(deudaMeta);
            itemsDeLasDeudas.add(itemDeuda);
        }

        for (Deuda deuda : deudasJugadorDeben) {
            ItemStack itemDeuda = new ItemStack(Material.GREEN_BANNER);
            ItemMeta deudaMeta = itemDeuda.getItemMeta();

            deudaMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA CANCELAR LA DEUDA");
            List<String> lore = new ArrayList<>();
            lore.add("   ");
            lore.add(ChatColor.GOLD + "Te debe: " + deuda.getDeudor());
            lore.add(ChatColor.GOLD + "Le queda de pagar: " + ChatColor.GREEN +  formatea.format(deuda.getPixelcoins()) + " PC" );
            lore.add(ChatColor.GOLD + "Vence en: " + deuda.getTiempo() + " dias");
            lore.add(ChatColor.GOLD + "ID: " + deuda.getId_deuda());

            deudaMeta.setLore(lore);
            itemDeuda.setItemMeta(deudaMeta);
            itemsDeLasDeudas.add(itemDeuda);
        }

        return itemsDeLasDeudas;
    }
}