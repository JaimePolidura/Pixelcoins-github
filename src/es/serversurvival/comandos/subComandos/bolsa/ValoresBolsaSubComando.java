package es.serversurvival.comandos.subComandos.bolsa;

import es.serversurvival.main.Pixelcoin;
import es.serversurvival.objetos.apiHttp.IEXCloud_API;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValoresBolsaSubComando extends BolsaSubCommand {
    private final String SCNombre = "valores";
    private final String sintaxis = "/bolsa valores";
    private final String ayuda = "ver una lista de valores de bolsa de Estados Unidos";
    private HashMap<String, String> acciones = new HashMap<>();
    public static final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "  /bolsa invertir <ticker>";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        contruirMapaAcciones();
        Inventory inventory = Bukkit.createInventory(null, 54, titulo);

        ItemStack anadir = new ItemStack(Material.BOOK);
        ItemMeta itemMeta = anadir.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        //Añadimos los items de las acciones
        for (Map.Entry<String, String> entry : acciones.entrySet()) {
            itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + entry.getValue());
            lore.add(ChatColor.GOLD + "Ticker: " + entry.getKey());
            lore.add(ChatColor.RED + "Cargando...");
            itemMeta.setLore(lore);
            anadir.setItemMeta(itemMeta);
            inventory.addItem(anadir);
            lore.clear();
        }
        //Añadimos el item que tendra en la descipccion la informacion
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
        inventory.addItem(papel);
        player.openInventory(inventory);

        boolean kickear = false;
        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            //Añadimos el precop de la accion
            List<String> precio = new ArrayList<>();
            int pos = 1;
            String ticker;

            for (ItemStack actual : inventory.getContents()) {
                if (pos == 54) {
                    break;
                }

                ItemMeta actualMeta = actual.getItemMeta();
                precio = actualMeta.getLore();
                precio.remove(1);

                ticker = precio.get(0).split(" ")[1];
                HttpURLConnection con = null;
                try {
                    double precioAccion = IEXCloud_API.getOnlyPrice(ticker);

                    precio.add(1, ChatColor.GOLD + "Precio/Accion: " + ChatColor.GREEN + precioAccion + " $");

                    actualMeta.setLore(precio);
                    actual.setItemMeta(actualMeta);

                    precio.clear();
                    pos++;
                } catch (Exception e) {
                    player.sendMessage(ChatColor.DARK_RED + "Algo salio mal, hablar con el admin");
                }
            }
        }, 0L);
        player.openInventory(inventory);
    }

    public void contruirMapaAcciones() {
        acciones.put("ATV", "Activision");
        acciones.put("ADBE", "Adobe");
        acciones.put("GOOG", "Google");
        acciones.put("AMZN", "Amazon");
        acciones.put("AMD", "AMD");
        acciones.put("AAPL", "Apple");
        acciones.put("T", "AT&T");
        acciones.put("AXP", "American Express");
        acciones.put("BAC", "Bank of America");
        acciones.put("BRK.B", "Berkshire Hathaway");
        acciones.put("BA", "Boeing");
        acciones.put("CAJ", "Canon");
        acciones.put("KO", "Cocacola");
        acciones.put("DELL", "Dell");
        acciones.put("DISCA", "Discovery");
        acciones.put("DBX", "Dropbox");
        acciones.put("ETFC", "Ebay");
        acciones.put("FDX", "FedEx");
        acciones.put("FL", "FootLocker");
        acciones.put("F", "Ford");
        acciones.put("GE", "General electric");
        acciones.put("GM", "General motors");
        acciones.put("GPRO", "Gopro");
        acciones.put("HPQ", "HP");
        acciones.put("IBM", "IBM");
        acciones.put("INTC", "Intel");
        acciones.put("KHC", "Heinz");
        acciones.put("MCD", "MacDonalds");
        acciones.put("MSFT", "Microsoft");
        acciones.put("NFLX", "Netflix");
        acciones.put("ORCL", "Oracle");
        acciones.put("PYPL", "PayPal");
        acciones.put("YUM", "YUM (Tacobell, KFC y PizzaHut)");
        acciones.put("SNE", "Sony");
        acciones.put("LUV", "South West");
        acciones.put("SPOT", "Spotify");
        acciones.put("SBUX", "StartBucks");
        acciones.put("TSLA", "Tesla");
        acciones.put("TWTR", "Twitter");
        acciones.put("UBER", "Uber");
        acciones.put("V", "Visa");
        acciones.put("BABA", "Alibaba");
        acciones.put("DAL", "Delta");
        acciones.put("FB", "Facebook");
        acciones.put("SAN", "Santander!!");
        acciones.put("NVDA", "Nvdia");
        acciones.put("HLT", "Hilton");
        acciones.put("WM", "Disney");
        acciones.put("WMT", "Wallmart");
        acciones.put("RCL", "RCruises");
        acciones.put("XRX", "Xerox");
        acciones.put("WFC", "Wells Fargo");
        acciones.put("SHOP", "Shopify");
    }
}