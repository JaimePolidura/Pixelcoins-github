package es.serversurvival.menus.menus;

import es.serversurvival.mySQL.enums.VALORES;
import es.serversurvival.util.Funciones;
import es.serversurvival.main.Pixelcoin;
import es.serversurvival.apiHttp.IEXCloud_API;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.CriptomonedasInventoryFactory;
import es.serversurvival.menus.menus.confirmaciones.ComprarBolsaConfirmacion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CriptomonedasMenu extends Menu implements Clickable, PostLoading {
    public static final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "   Escoge para invertir";
    private Player player;
    private Inventory inventory;

    public CriptomonedasMenu (Player player) {
        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(new CriptomonedasInventoryFactory(), player.getName());

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
        ItemStack itemStack = event.getCurrentItem();
        if(itemStack == null || !Funciones.cuincideNombre(itemStack.getType().toString(), "GOLD_BLOCK")){
            return;
        }

        String nombreValor = itemStack.getItemMeta().getDisplayName().substring(4);
        List<String> lore = itemStack.getItemMeta().getLore();
        String precioLore = lore.get(1);
        if (precioLore.equalsIgnoreCase(ChatColor.RED + "Cargando...")) {
            return;
        }
        double precio = Double.parseDouble(lore.get(1).split(" ")[1]);
        String simbolo = lore.get(0).split(" ")[1];

        //closeMenu();

        ComprarBolsaConfirmacion confirmacion = new ComprarBolsaConfirmacion(simbolo, nombreValor, VALORES.CRIPTOMONEDAS.toString(), "monedas", player.getName(), precio);
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
                    double precioMoneda = IEXCloud_API.getPrecioCriptomoneda(ticker);

                    precio.add(1, ChatColor.GOLD + "Precio/Moneda:" + ChatColor.GREEN + " " + formatea.format(precioMoneda) + " $");

                    actualMeta.setLore(precio);
                    actual.setItemMeta(actualMeta);

                    precio.clear();
                } catch (Exception e) {
                    player.sendMessage(ChatColor.DARK_RED + "No hagas spam del comando");
                }
            }
        }, 0L);
    }
}