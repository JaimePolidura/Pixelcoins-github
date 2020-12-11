package es.serversurvival.menus.menus;

import es.serversurvival.apiHttp.IEXCloud_API;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.AccionesInventoryPag1Factory;
import es.serversurvival.menus.inventoryFactory.inventories.AccionesInventoryPag2Factory;
import es.serversurvival.menus.menus.confirmaciones.ComprarBolsaConfirmacion;
import es.serversurvival.mySQL.enums.TipoValor;
import es.serversurvival.util.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccionesMenu extends Menu implements Clickable, Paginated, PostLoading{
    public static final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "   Escoge para invertir";
    private static ExecutorService pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private Player player;
    private Inventory inventory;
    private int currentIndex;
    private List<Page> pages;
    
    public AccionesMenu(Player player) {
        this.player = player;
        this.currentIndex = 0;

        this.pages = new ArrayList<>();
        Inventory inventoryPag1 = InventoryCreator.createInventoryMenu(new AccionesInventoryPag1Factory(), player.getName());
        Inventory inventoryPag2 = InventoryCreator.createInventoryMenu(new AccionesInventoryPag2Factory(), player.getName());
        pages.add(new Page(0, inventoryPag1));
        pages.add(new Page(1, inventoryPag2));

        this.inventory = inventoryPag1;
        postLoad();
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        if(!Funciones.cuincideNombre(itemStack.getType().toString(), "BOOK")){
            return;
        }

        String nombreValor = itemStack.getItemMeta().getDisplayName().substring(4);
        List<String> lore = itemStack.getItemMeta().getLore();
        String precioLore = lore.get(1);
        if (precioLore.equalsIgnoreCase(ChatColor.RED + "Cargando...")) {
            return;
        }
        double precio = Double.parseDouble(lore.get(1).split(" ")[1].substring(2));
        String ticker = lore.get(0).split(" ")[1];

        closeMenu();
        ComprarBolsaConfirmacion confirmacion = new ComprarBolsaConfirmacion(ticker, nombreValor, TipoValor.ACCIONES.toString(), "acciones", player.getName(), precio);
        confirmacion.openMenu();
    }

    @Override
    public void goFordward() {
        this.inventory = pages.get(1).inventory;
        this.currentIndex = 1;
        openMenu();
        postLoad();
    }

    @Override
    public void goBack() {
        this.inventory = pages.get(0).inventory;
        this.currentIndex = 0;
        openMenu();
    }

    @Override
    public void postLoad() {
        for(int i = 0; i < inventory.getContents().length; i++){
            ItemStack actual = inventory.getContents()[i];
            if(i == 52 || actual == null || Funciones.esDeTipoItem(actual, "AIR")){
                break;
            }

            ItemMeta actualMeta = actual.getItemMeta();
            List<String> precio = actualMeta.getLore();
            if(precio == null || precio.get(1) == null || !precio.get(1).equalsIgnoreCase(ChatColor.RED + "Cargando...")){
                continue;
            }

            pool.submit(() -> {
                precio.remove(1);

                String ticker = precio.get(0).split(" ")[1];
                try {
                    double precioAccion = IEXCloud_API.getOnlyPrice(ticker);

                    precio.add(1, ChatColor.GOLD + "Precio/Accion:" + ChatColor.GREEN + " " + formatea.format(precioAccion)  + " PC");

                    actualMeta.setLore(precio);
                    actual.setItemMeta(actualMeta);
                } catch (Exception e) {
                    player.sendMessage(ChatColor.DARK_RED + "No hagas spam del comando");
                }
            });
        }
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
    public int getCurrentIndex() {
        return currentIndex;
    }

    @Override
    public List<Page> getPages() {
        return pages;
    }

    @Override
    public String getNameItemGoBack() {
        return ITEM_NAME_GOBACK;
    }

    @Override
    public String getNameItemGoFordward() {
        return ITEM_NAME_GOFORDWARD;
    }
}
