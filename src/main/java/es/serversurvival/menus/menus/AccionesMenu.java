package es.serversurvival.menus.menus;

import es.serversurvival.apiHttp.IEXCloud_API;
import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.inventoryFactory.inventories.AccionesInventoryFactory;
import es.serversurvival.menus.menus.confirmaciones.ComprarBolsaConfirmacion;
import es.serversurvival.mySQL.enums.TipoActivo;
import es.serversurvival.util.Funciones;
import es.serversurvival.util.MinecraftUtils;
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

public class AccionesMenu extends Menu implements Clickable, Paginated, PostLoading {
    public static final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "   Escoge para invertir";
    private static ExecutorService pool = Executors.newSingleThreadExecutor();

    private Player player;
    private Inventory inventory;
    private int currentIndex;
    private List<Page> pages;
    
    public AccionesMenu(Player player) {
        this.player = player;
        this.currentIndex = 0;

        this.pages = new ArrayList<>();
        AccionesInventoryFactory inventoryFactory = new AccionesInventoryFactory();
        pages.add(new Page(0, InventoryCreator.createInventoryMenu(inventoryFactory, player.getName())));
        pages.add(new Page(1, inventoryFactory.buildInventoryPag2()));

        this.inventory = this.pages.get(0).inventory;

        postLoad();
        openMenu();
    }

    @Override
    public void onOherClick(InventoryClickEvent event) {
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

        double precio = Double.parseDouble(lore.get(1).split(" ")[1].split(",")[0]);
        String ticker = lore.get(0).split(" ")[1];

        closeMenu();
        ComprarBolsaConfirmacion confirmacion = new ComprarBolsaConfirmacion(ticker, nombreValor, TipoActivo.ACCIONES, "acciones", player, precio);
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

            asynchLoadPrice(precio, actual);
        }
    }

    private void asynchLoadPrice (List<String> lore, ItemStack item) {
        pool.submit(() -> {
            lore.remove(1);

            String ticker = lore.get(0).split(" ")[1];
            try {
                double precioAccion = IEXCloud_API.getOnlyPrice(ticker);
                lore.add(1, ChatColor.GOLD + "Precio/Accion:" + ChatColor.GREEN + " " + formatea.format(precioAccion)  + " PC");

                MinecraftUtils.setLore(item, lore);
            } catch (Exception e) {
                player.sendMessage(ChatColor.DARK_RED + "No hagas spam del comando");
            }
        });
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
