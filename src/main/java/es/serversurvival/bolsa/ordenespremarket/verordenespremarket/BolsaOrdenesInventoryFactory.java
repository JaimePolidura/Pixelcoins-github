package es.serversurvival.bolsa.ordenespremarket.verordenespremarket;

import es.jaimetruman.ItemBuilder;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.ordenespremarket._shared.application.OrdenesPremarketService;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.OrdenPremarket;
import es.serversurvival._shared.menus.inventory.InventoryFactory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BolsaOrdenesInventoryFactory extends InventoryFactory {
    private final OrdenesPremarketService ordenesPremarketService;

    public BolsaOrdenesInventoryFactory(){
        this.ordenesPremarketService = DependecyContainer.get(OrdenesPremarketService.class);
    }

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
        List<OrdenPremarket> ordenes = ordenesPremarketService.findByJugador(jugador);
        List<ItemStack> items = new ArrayList<>();

        for(OrdenPremarket orden : ordenes){
            items.add(buildItemOrden(orden));
        }

        return items;
    }

    private ItemStack buildItemOrden (OrdenPremarket orden) {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "CLICK PARA CANCELAR";
        List<String> lore = new ArrayList<>();
        lore.add("  ");
        lore.add(ChatColor.GOLD + "Ticker: " + orden.getNombreActivo());
        lore.add(ChatColor.GOLD + "Cantidad: " + orden.getCantidad());
        lore.add(ChatColor.GOLD + "Operacion: " + orden.getTipoAccion().toString().split("_")[1].toLowerCase());
        lore.add(ChatColor.GOLD + "Tipo: " + orden.getTipoAccion().toString().split("_")[0].toLowerCase());
        lore.add("  ");
        lore.add("" + orden.getOrderPremarketId());

        return ItemBuilder.of(Material.NAME_TAG).title(displayName).lore(lore).build();
    }

    private ItemStack buildInfo () {
        String displayName = ChatColor.GOLD + "" + ChatColor.BOLD + "INFO";
        List<String> lore = new ArrayList<>();
        lore.add("El mercado de acciones no siempre esta abierto");
        lore.add("Su horario son los dias entre semana 15:30 - 22:30");
        lore.add("Cuando compras una accion fuera de ese horario, se");
        lore.add("añade una orden de compra/venta. Cuando el mercado");
        lore.add("abre se ejecuta");

        return ItemBuilder.of(Material.PAPER).title(displayName).lore(lore).build();
    }
}
