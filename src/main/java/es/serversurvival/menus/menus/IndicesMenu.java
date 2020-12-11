package es.serversurvival.menus.menus;


import es.serversurvival.apiHttp.FinancialModelingGrep;
import es.serversurvival.main.Pixelcoin;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.IndicesInventoryFactory;
import es.serversurvival.menus.menus.confirmaciones.ComprarBolsaConfirmacion;
import es.serversurvival.mySQL.enums.TipoValor;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.json.simple.JSONObject;

import java.util.List;

public class IndicesMenu extends Menu implements Clickable, PostLoading {
    private Player player;
    private Inventory inventory;

    public IndicesMenu (Player player) {
        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(new IndicesInventoryFactory(), player.getName());

        postLoad();
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack item = event.getCurrentItem();
        if(item == null || Funciones.noEsDeTipoItem(item, "WRITABLE_BOOK")){
            return;
        }

        String nombreValor = item.getItemMeta().getDisplayName().substring(4);
        List<String> lore = item.getItemMeta().getLore();
        String precioLore = lore.get(1);
        if (precioLore.equalsIgnoreCase(ChatColor.RED + "Cargando...")) {
            return;
        }
        double precio = Double.parseDouble(lore.get(1).split(" ")[1]);
        String simbolo = lore.get(0).split(" ")[1];

        ComprarBolsaConfirmacion confirmacion = new ComprarBolsaConfirmacion(simbolo, nombreValor, TipoValor.INDICES.toString(), "contratos", player.getName(), precio);
        confirmacion.openMenu();
    }

    @Override
    public void postLoad() {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            List<String> precio;
            int pos = 0;

            for (ItemStack actual : inventory.getContents()) {
                if (pos == 4 || actual == null || actual.getType().toString().equalsIgnoreCase("AIR")) {
                    break;
                }
                pos++;

                ItemMeta actualMeta = actual.getItemMeta();
                precio = actualMeta.getLore();
                precio.remove(1);

                String ticker = precio.get(0).split(" ")[1];
                try {
                    double precioMoneda = getPrecioIndice(ticker);

                    precio.add(1, ChatColor.GOLD + "Precio/Contrato:" + ChatColor.GREEN + " " + precioMoneda + " $");

                    actualMeta.setLore(precio);
                    actual.setItemMeta(actualMeta);

                    precio.clear();
                } catch (Exception e) {
                    player.sendMessage(ChatColor.DARK_RED + "No hagas spam del comando");
                }
            }
        }, 0L);
    }

    public double getPrecioIndice (String ticker) throws Exception {
        JSONObject jsonObject = (JSONObject) FinancialModelingGrep.getPrecioIndice("%5E" + ticker).get(0);
        return (double) jsonObject.get("price");
    }
}
