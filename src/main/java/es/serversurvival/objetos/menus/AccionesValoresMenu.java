package es.serversurvival.objetos.menus;

import es.serversurvival.main.Pixelcoin;
import es.serversurvival.objetos.apiHttp.IEXCloud_API;
import es.serversurvival.objetos.mySQL.PosicionesAbiertas;
import es.serversurvival.objetos.solicitudes.ComprarBolsaSolicitud;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class AccionesValoresMenu extends Menu implements Clickleable{
    private final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "   Escoge para invertir";
    private HashMap<String, String> pag1 = new HashMap<>();
    private HashMap<String, String> pag2 = new HashMap<>();
    private List<Pagina> paginas = new ArrayList<>();
    private Pagina currentPag;
    private Player player;

    public AccionesValoresMenu(Player player){
        this.player = player;
        constuirPag1();
        construirPag2();

        paginas.add(new Pagina(1, pag1));
        paginas.add(new Pagina(2, pag2));

        this.currentPag = paginas.get(0);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public String titulo() {
        return titulo;
    }

    @Override
    public void openMenu() {
        player.openInventory(currentPag.getInventory());
        activeMenus.add(this);
    }

    @Override
    public void closeMenu() {
        activeMenus.remove(this);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        if(itemStack == null || itemStack.getType().toString().equalsIgnoreCase("AIR")){
            return;
        }
        if (itemStack.getType().toString().equalsIgnoreCase("PAPER")) {
            return;
        }
        if(itemStack.getType().toString().equalsIgnoreCase("GREEN_WOOL")){
            int index = this.currentPag.getIndice();
            this.currentPag = paginas.get(index);
            openMenu();

            return;
        }
        if(itemStack.getType().toString().equalsIgnoreCase("RED_WOOL")){
            int index = this.currentPag.getIndice();
            this.currentPag = paginas.get(index - 2);
            openMenu();

            return;
        }

        List<String> lore = itemStack.getItemMeta().getLore();
        String precioLore = lore.get(1);
        if (precioLore.equalsIgnoreCase(ChatColor.RED + "Cargando...")) {
            return;
        }
        double precio = Double.parseDouble(lore.get(1).split(" ")[1]);
        String ticker = lore.get(0).split(" ")[1];

        closeMenu();
        ComprarBolsaSolicitud comprarAccionSolicitud1 = new ComprarBolsaSolicitud(ticker, PosicionesAbiertas.TIPOS.ACCIONES.toString(), "acciones", player.getName(), precio);
        comprarAccionSolicitud1.enviarSolicitud();
    }

    private void construirPag2(){
        pag2.put("SNAP","Snapchat");
        pag2.put("NKE", "Nike");
        pag2.put("VZ", "Verizon");
        pag2.put("TM","Toyota");
        pag2.put("TRIP", "Trip advisor");
        pag2.put("TMUS","T-Movile");
        pag2.put("QCOM","Qualcomm");
        pag2.put("PM","Philip Morris");
        pag2.put("NDAQ","NASDAQ");
        pag2.put("MSI","Motorola");
        pag2.put("MA","Mastercard");
        pag2.put("JPM","JP Morgan");
        pag2.put("HMC","Honda");
        pag2.put("GME","GameStop");
        pag2.put("RACE","Ferrari");
        pag2.put("FCAU","Fiat");
        pag2.put("TEF","Telefonica");
        pag2.put("REPYY","Repsol");
        pag2.put("BKNG","Booking holdings");
        pag2.put("AAL","American airlines");
        pag2.put("GHC","Whasington post");
        pag2.put("APA","Apache");
        pag2.put("CTSH","Cognizant");
        pag2.put("PINS","Pinerest");
        pag2.put("ZM","Zoom");
        pag2.put("CVX","Chevron");
        pag2.put("CSCO","Cisco");
        pag2.put("PEP","Pepsi");
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

    private class Pagina{
        private int indice;
        private Inventory inventory;
        private Map<String, String> acciones;

        public Pagina (int indice, Map<String, String> acciones){
            this.indice = indice;
            this.acciones = acciones;
        }

        public int getIndice() {
            return indice;
        }

        public Inventory getInventory(){
            if(inventory == null){
                inventory = this.createPagina();
            }
            return inventory;
        }

        private Inventory createPagina() {
            inventory = Bukkit.createInventory(null, 54, titulo);

            ItemStack anadir = new ItemStack(Material.BOOK);
            ItemMeta itemMeta = anadir.getItemMeta();
            ArrayList<String> lore = new ArrayList<>();

            for (Map.Entry<String, String> entry : acciones.entrySet()) {
                itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + entry.getValue());
                lore.add(ChatColor.GOLD + "Ticker: " + entry.getKey());
                lore.add(ChatColor.RED + "Cargando...");
                itemMeta.setLore(lore);
                anadir.setItemMeta(itemMeta);
                inventory.addItem(anadir);
                lore.clear();
            }

            ItemStack nextPage;
            ItemMeta itemMetaNextPage;
            if(indice == paginas.size()){
                nextPage = new ItemStack(Material.RED_WOOL);
                itemMetaNextPage = nextPage.getItemMeta();
                itemMetaNextPage.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "...Pagina anterior");
                nextPage.setItemMeta(itemMetaNextPage);
            }else{
                nextPage = new ItemStack(Material.GREEN_WOOL);
                itemMetaNextPage = nextPage.getItemMeta();
                itemMetaNextPage.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "Siguiente pagina...");
                nextPage.setItemMeta(itemMetaNextPage);
            }
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
            player.openInventory(inventory);

            Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
                List<String> precio;
                int pos = 1;
                String ticker;

                for (ItemStack actual : inventory.getContents()) {
                    if (pos == 53 || actual == null || actual.getType().toString().equalsIgnoreCase("AIR")) {
                        break;
                    }
                    ItemMeta actualMeta = actual.getItemMeta();
                    precio = actualMeta.getLore();
                    precio.remove(1);

                    ticker = precio.get(0).split(" ")[1];
                    try {
                        double precioAccion = IEXCloud_API.getOnlyPrice(ticker);

                        precio.add(1, ChatColor.GOLD + "Precio/Accion:" + ChatColor.GREEN + " " + precioAccion + " $");

                        actualMeta.setLore(precio);
                        actual.setItemMeta(actualMeta);

                        precio.clear();
                        pos++;
                    } catch (Exception e) {
                        player.sendMessage(ChatColor.DARK_RED + "No hagas spam del comando");
                    }
                }
            }, 0L);
            return inventory;
        }
    }
}