package es.serversurvival.bolsa.other.vervalores.acciones;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.bolsa.other._shared.llamadasapi.mysql.LlamadaApi;
import es.serversurvival._shared.menus.inventory.InventoryFactory;
import es.serversurvival._shared.menus.Paginated;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccionesInventoryFactory extends InventoryFactory {
    private final HashMap<String, String> accionesPagina1 = new HashMap<>();
    private final HashMap<String, String> accionesPagina2 = new HashMap<>();

    public AccionesInventoryFactory() {
        buildPagina1();
        buildPagina2();
    }

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = buildInventory(accionesPagina1);
        inventory.setItem(53, buidItemNextPage());

        return inventory;
    }

    public Inventory buildInventoryPag2 () {
        Inventory inventory = buildInventory(accionesPagina2);
        inventory.setItem(53, buildItemBackPage());

        return inventory;
    }

    private Inventory buildInventory (Map<String, String> acciones) {
        Inventory inventory = Bukkit.createInventory(null, 54, AccionesMenu.titulo);

        List<ItemStack> itemsAcciones = buildItemsAcciones(acciones);
        ItemStack info = buildItemInfo();

        inventory.setItem(52, info);
        for(int i = 0; i < itemsAcciones.size(); i++) {inventory.setItem(i, itemsAcciones.get(i));}

        return inventory;
    }

    private ItemStack buidItemNextPage () {
        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(Paginated.ITEM_NAME_GOFORDWARD)
                .build();
    }

    private ItemStack buildItemBackPage () {
        return ItemBuilder.of(Material.RED_WOOL)
                .title(Paginated.ITEM_NAME_GOBACK)
                .build();
    }

    private List<ItemStack> buildItemsAcciones (Map<String, String> accionesPaginaActual) {
        List<ItemStack> itemStackList = new ArrayList<>();

        Map<String, LlamadaApi> acciones = llamadasApiMySQL.getMapOfAllLlamadasApi();

        for (Map.Entry<String, String> entry : accionesPaginaActual.entrySet()) {
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Ticker: " + entry.getKey());

            LlamadaApi accion = acciones.get(entry.getKey());
            String displayName = "";
            if(accion == null){
                displayName = ChatColor.GOLD + "" + ChatColor.BOLD + entry.getValue();
                lore.add(ChatColor.RED + "Cargando...");
            }else{
                displayName = ChatColor.GOLD + "" + ChatColor.BOLD + accion.getNombre_activo();
                lore.add(ChatColor.GOLD + "Precio/Accion:" + ChatColor.GREEN + " " + formatea.format(accion.getPrecio())  + " PC");
            }

            ItemStack accionItem = ItemBuilder.of(Material.BOOK).title(displayName).lore(lore).build();

            itemStackList.add(accionItem);
        }

        return itemStackList;
    }

    private ItemStack buildItemInfo () {
        List<String> infolore = new ArrayList<>();
        infolore.add("Para invertir en estas acciones: /bolsa invertir <ticker> <nº acciones>");
        infolore.add("                  ");
        infolore.add("Estas acciones son ejemplos con las que se puede comprar, ");
        infolore.add("si quieres comprar otra accion que no este en la lista adelante");
        infolore.add(" solo que necesitas encontrar el ticker en internet, ");
        infolore.add("se puede encontrar en cualquier pagina como por ejemplo");
        infolore.add("es.investing.com. Hay que aclarar que hay acciones que se puede ");
        infolore.add("y otras no la mayoria que se pueden son americanas.");

        String displayname = ChatColor.AQUA + "" + ChatColor.BOLD + "INFO";

        return ItemBuilder.of(Material.PAPER).title(displayname).lore(infolore).build();
    }

    public void buildPagina1 () {
        accionesPagina1.put("ATVI", "Activision");
        accionesPagina1.put("ADBE", "Adobe");
        accionesPagina1.put("GOOG", "Google");
        accionesPagina1.put("AMZN", "Amazon");
        accionesPagina1.put("AMD", "AMD");
        accionesPagina1.put("AAPL", "Apple");
        accionesPagina1.put("T", "AT&T");
        accionesPagina1.put("AXP", "American Express");
        accionesPagina1.put("BAC", "Bank of America");
        accionesPagina1.put("BRK.B", "Berkshire Hathaway");
        accionesPagina1.put("BA", "Boeing");
        accionesPagina1.put("CAJ", "Canon");
        accionesPagina1.put("KO", "Cocacola");
        accionesPagina1.put("DELL", "Dell");
        accionesPagina1.put("DISCA", "Discovery");
        accionesPagina1.put("DBX", "Dropbox");
        accionesPagina1.put("ETFC", "Ebay");
        accionesPagina1.put("FDX", "FedEx");
        accionesPagina1.put("FL", "FootLocker");
        accionesPagina1.put("F", "Ford");
        accionesPagina1.put("GE", "General electric");
        accionesPagina1.put("GPRO", "Gopro");
        accionesPagina1.put("HPQ", "HP");
        accionesPagina1.put("IBM", "IBM");
        accionesPagina1.put("INTC", "Intel");
        accionesPagina1.put("KHC", "Heinz");
        accionesPagina1.put("MCD", "MacDonalds");
        accionesPagina1.put("MSFT", "Microsoft");
        accionesPagina1.put("NFLX", "Netflix");
        accionesPagina1.put("ORCL", "Oracle");
        accionesPagina1.put("PYPL", "PayPal");
        accionesPagina1.put("YUM", "YUM (Tacobell, KFC y PizzaHut)");
        accionesPagina1.put("SNE", "Sony");
        accionesPagina1.put("LUV", "South West");
        accionesPagina1.put("SPOT", "Spotify");
        accionesPagina1.put("SBUX", "StartBucks");
        accionesPagina1.put("TSLA", "Tesla");
        accionesPagina1.put("TWTR", "Twitter");
        accionesPagina1.put("UBER", "Uber");
        accionesPagina1.put("V", "Visa");
        accionesPagina1.put("BABA", "Alibaba");
        accionesPagina1.put("DAL", "Delta");
        accionesPagina1.put("FB", "Facebook");
        accionesPagina1.put("SAN", "Santander!!");
        accionesPagina1.put("NVDA", "Nvdia");
        accionesPagina1.put("HLT", "Hilton");
        accionesPagina1.put("WM", "Disney");
        accionesPagina1.put("WMT", "Wallmart");
        accionesPagina1.put("RCL", "RCruises");
        accionesPagina1.put("XRX", "Xerox");
        accionesPagina1.put("WFC", "Wells Fargo");
        accionesPagina1.put("SHOP", "Shopify");
    }

    private void buildPagina2 () {
        accionesPagina2.put("SNAP","Snapchat");
        accionesPagina2.put("NKE", "Nike");
        accionesPagina2.put("VZ", "Verizon");
        accionesPagina2.put("TM","Toyota");
        accionesPagina2.put("TRIP", "Trip advisor");
        accionesPagina2.put("TMUS","T-Movile");
        accionesPagina2.put("QCOM","Qualcomm");
        accionesPagina2.put("PM","Philip Morris");
        accionesPagina2.put("NDAQ","NASDAQ");
        accionesPagina2.put("MSI","Motorola");
        accionesPagina2.put("MA","Mastercard");
        accionesPagina2.put("JPM","JP Morgan");
        accionesPagina2.put("HMC","Honda");
        accionesPagina2.put("GME","GameStop");
        accionesPagina2.put("RACE","Ferrari");
        accionesPagina2.put("FCAU","Fiat");
        accionesPagina2.put("TEF","Telefonica");
        accionesPagina2.put("REPYY","Repsol");
        accionesPagina2.put("BKNG","Booking holdings");
        accionesPagina2.put("AAL","American airlines");
        accionesPagina2.put("GHC","Whasington post");
        accionesPagina2.put("APA","Apache");
        accionesPagina2.put("CTSH","Cognizant");
        accionesPagina2.put("PINS","Pinerest");
        accionesPagina2.put("ZM","Zoom");
        accionesPagina2.put("CVX","Chevron");
        accionesPagina2.put("CSCO","Cisco");
        accionesPagina2.put("PEP","Pepsi");
        accionesPagina2.put("TSM", "Taiwan semiconductor manufacturing ");
        accionesPagina2.put("JNJ", "Johnson&Johnson");
        accionesPagina2.put("MCO", "Moodys corporation");
        accionesPagina2.put("FNMA", "Fannie Mae");
        accionesPagina2.put("AZN", "AstraZeneca");
        accionesPagina2.put("SAP", "SAP");
        accionesPagina2.put("LOGI", "Logitech");
    }
}
