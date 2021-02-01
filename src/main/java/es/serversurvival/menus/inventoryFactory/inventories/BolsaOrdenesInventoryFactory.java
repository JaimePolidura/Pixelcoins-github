package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.mySQL.tablasObjetos.OrdenPreMarket;
import es.serversurvival.util.MinecraftUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BolsaOrdenesInventoryFactory extends InventoryFactory {
    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_RED + "" + ChatColor.BOLD + "         TUS ORDENES");

        inventory.setItem(0, buildInfo());

        List<ItemStack> itemsOrdenes = buildItemsOrdenes(jugador);
        for(int i = 0; i < itemsOrdenes.size(); i++){
            inventory.setItem(i + 9, itemsOrdenes.get(i));
        }

        return inventory;
    }

    private List<ItemStack> buildItemsOrdenes (String jugador) {
        List<OrdenPreMarket> ordenes = ordenesMySQL.getOrdenes(jugador);
        List<ItemStack> items = new ArrayList<>();

        for(OrdenPreMarket orden : ordenes){
            items.add(buildItemOrden(orden));
        }

        return items;
    }

    private ItemStack buildItemOrden (OrdenPreMarket orden) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA CANCELAR";
        Material material = Material.NAME_TAG;
        List<String> lore = new ArrayList<>();
        lore.add("  ");
        lore.add(ChatColor.GOLD + "Ticker: " + orden.getNombre_activo());
        lore.add(ChatColor.GOLD + "Cantidad: " + orden.getCantidad());
        lore.add(ChatColor.GOLD + "Operacion: " + orden.getAccion_orden().toString().split("_")[1].toLowerCase());
        lore.add(ChatColor.GOLD + "Tipo: " + orden.getAccion_orden().toString().split("_")[0].toLowerCase());
        lore.add("  ");
        lore.add("" + orden.getId());

        return MinecraftUtils.loreDisplayName(material, displayName, lore);
    }

    private ItemStack buildInfo () {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "INFO";
        List<String> lore = new ArrayList<>();
        lore.add("El mercado de acciones no siempre esta abierto");
        lore.add("Su horario son los dias entre semana 15:30 - 22:30");
        lore.add("Cuando compras una accion fuera de ese horario, se");
        lore.add("añade una orden de compra/venta. Cuando el mercado");
        lore.add("abre se ejecuta");

        return MinecraftUtils.loreDisplayName(Material.PAPER, displayName, lore);
    }
}
