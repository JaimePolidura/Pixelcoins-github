package es.serversurvival.menus.menus;

import es.serversurvival.main.Pixelcoin;
import es.serversurvival.apiHttp.IEXCloud_API;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.MateriasPrimasInventoryFactory;
import es.serversurvival.menus.menus.confirmaciones.ComprarBolsaConfirmacion;
import es.serversurvival.mySQL.enums.TipoValor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MateriasPrimasMenu extends Menu implements Clickable, PostLoading {
    public final static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "   Escoge para invertir";
    private Inventory inventory;
    private Player player;

    public MateriasPrimasMenu (Player player) {
        this.player = player;
        this.inventory = InventoryCreator.createInventoryMenu(new MateriasPrimasInventoryFactory(), player.getName());

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
        if(itemStack == null || !itemStack.getType().toString().equalsIgnoreCase("COAL")){
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

        closeMenu();
        String alias;

        if(simbolo.equalsIgnoreCase("DCOILBRENTEU")){
            alias = "barriles";
        }else if(simbolo.equalsIgnoreCase("DHHNGSP")){
            alias = "galones";
        }else{
            alias = "galones";
        }

        ComprarBolsaConfirmacion confirmacion = new ComprarBolsaConfirmacion(simbolo, nombreValor, TipoValor.MATERIAS_PRIMAS.toString(), alias, player.getName(), precio);
        confirmacion.openMenu();
    }

    @Override
    public void postLoad() {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            int pos = 0;

            for (ItemStack actual : inventory.getContents()) {
                if (pos == 4 || actual == null || actual.getType().toString().equalsIgnoreCase("AIR")) {
                    break;
                }
                pos++;

                ItemMeta actualMeta = actual.getItemMeta();
                 List<String> precio = actualMeta.getLore();
                precio.remove(1);

                String ticker = precio.get(0).split(" ")[1];
                try {
                    double preioMat = IEXCloud_API.getPrecioMateriaPrima(ticker);

                    precio.add(1, ChatColor.GOLD + "Precio/Unidad:" + ChatColor.GREEN + " " + preioMat + " $");

                    actualMeta.setLore(precio);
                    actual.setItemMeta(actualMeta);

                } catch (Exception e) {
                    player.sendMessage(ChatColor.DARK_RED + "No hagas spam del comando");
                }
            }
        }, 0L);
    }
}
