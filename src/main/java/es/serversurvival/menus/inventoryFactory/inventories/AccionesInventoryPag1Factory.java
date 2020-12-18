package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.menus.menus.AccionesMenu;
import es.serversurvival.menus.menus.Paginated;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.tablasObjetos.LlamadaApi;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccionesInventoryPag1Factory extends InventoryFactory {
    private HashMap<String, String> pag1 = new HashMap<>();

    @Override
    protected Inventory buildInventory(String jugador) {
        constuirPag1();

        return buildInv();
    }

    private Inventory buildInv() {
        Inventory inventory = Bukkit.createInventory(null, 54, AccionesMenu.titulo);

        ItemStack anadir = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = anadir.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        MySQL.conectar();
        Map<String, LlamadaApi> acciones = llamadasApiMySQL.getMapOfAllLlamadasApi();
        MySQL.desconectar();

        for (Map.Entry<String, String> entry : pag1.entrySet()) {
            LlamadaApi accion = acciones.get(entry.getKey());
            if(accion == null){
                itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + entry.getValue());
                lore.add(ChatColor.GOLD + "Ticker: " + entry.getKey());
                lore.add(ChatColor.RED + "Cargando...");
            }else{
                itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + accion.getNombre_activo());
                lore.add(ChatColor.GOLD + "Ticker: " + entry.getKey());
                lore.add(ChatColor.GOLD + "Precio/Accion: " + ChatColor.GREEN + formatea.format(accion.getPrecio()) + " PC");
            }
            itemMeta.setLore(lore);
            anadir.setItemMeta(itemMeta);
            inventory.addItem(anadir);
            lore.clear();
        }

        ItemStack nextPage = new ItemStack(Material.GREEN_WOOL);
        ItemMeta itemMetaNextPage = nextPage.getItemMeta();
        itemMetaNextPage.setDisplayName(Paginated.ITEM_NAME_GOFORDWARD);
        nextPage.setItemMeta(itemMetaNextPage);

        inventory.setItem(53, nextPage);

        ItemStack papel = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = papel.getItemMeta();
        ArrayList<String> info = new ArrayList<>();
        info.add("Para invertir en estas acciones: /bolsa invertir <ticker> <nº acciones>");
        info.add("                  ");
        info.add("Estas acciones son ejemplos con las que se puede comprar, ");
        info.add("si quieres comprar otra accion que no este en la lista adelante");
        info.add(" solo que necesitas encontrar el ticker en internet, ");
        info.add("se puede encontrar en cualquier pagina como por ejemplo");
        info.add("es.investing.com. Hay que aclarar que hay acciones que se puede ");
        info.add("y otras no la mayoria que se pueden son americanas.");
        infoMeta.setLore(info);
        infoMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "INFO");
        papel.setItemMeta(infoMeta);
        inventory.setItem(52, papel);

        return inventory;
    }

    private void constuirPag1() {
        pag1.put("ATV", "Activision");
        pag1.put("ADBE", "Adobe");
        pag1.put("GOOG", "Google");
        pag1.put("AMZN", "Amazon");
        pag1.put("AMD", "AMD");
        pag1.put("AAPL", "Apple");
        pag1.put("T", "AT&T");
        pag1.put("AXP", "American Express");
        pag1.put("BAC", "Bank of America");
        pag1.put("BRK.B", "Berkshire Hathaway");
        pag1.put("BA", "Boeing");
        pag1.put("CAJ", "Canon");
        pag1.put("KO", "Cocacola");
        pag1.put("DELL", "Dell");
        pag1.put("DISCA", "Discovery");
        pag1.put("DBX", "Dropbox");
        pag1.put("ETFC", "Ebay");
        pag1.put("FDX", "FedEx");
        pag1.put("FL", "FootLocker");
        pag1.put("F", "Ford");
        pag1.put("GE", "General electric");
        pag1.put("GPRO", "Gopro");
        pag1.put("HPQ", "HP");
        pag1.put("IBM", "IBM");
        pag1.put("INTC", "Intel");
        pag1.put("KHC", "Heinz");
        pag1.put("MCD", "MacDonalds");
        pag1.put("MSFT", "Microsoft");
        pag1.put("NFLX", "Netflix");
        pag1.put("ORCL", "Oracle");
        pag1.put("PYPL", "PayPal");
        pag1.put("YUM", "YUM (Tacobell, KFC y PizzaHut)");
        pag1.put("SNE", "Sony");
        pag1.put("LUV", "South West");
        pag1.put("SPOT", "Spotify");
        pag1.put("SBUX", "StartBucks");
        pag1.put("TSLA", "Tesla");
        pag1.put("TWTR", "Twitter");
        pag1.put("UBER", "Uber");
        pag1.put("V", "Visa");
        pag1.put("BABA", "Alibaba");
        pag1.put("DAL", "Delta");
        pag1.put("FB", "Facebook");
        pag1.put("SAN", "Santander!!");
        pag1.put("NVDA", "Nvdia");
        pag1.put("HLT", "Hilton");
        pag1.put("WM", "Disney");
        pag1.put("WMT", "Wallmart");
        pag1.put("RCL", "RCruises");
        pag1.put("XRX", "Xerox");
        pag1.put("WFC", "Wells Fargo");
        pag1.put("SHOP", "Shopify");
    }
}
