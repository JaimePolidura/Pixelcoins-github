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
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class MateriasPrimasMenu extends Menu implements Clickleable{
    private final String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "   Escoge para invertir";
    private Player player;
    private HashMap<String, String> pag1 = new HashMap<>();
    private Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, titulo);

    public MateriasPrimasMenu (Player player){
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public String titulo() {
        return titulo;
    }

    @Override
    public void openMenu() {
        buildInv(pag1);
        player.openInventory(inventory);
        activeMenus.add(this);
    }

    @Override
    public void closeMenu() {
        activeMenus.remove(this);
    }

    private void buildInv(HashMap<String, String> mapa){
        buildPag1();

        ItemStack anadir = new ItemStack(Material.COAL);
        ItemMeta itemMeta = anadir.getItemMeta();
        ArrayList<String> lore = new ArrayList<>();

        //Añadimos los items de las acciones
        for (Map.Entry<String, String> entry : mapa.entrySet()) {
            itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + entry.getValue());
            lore.add(ChatColor.GOLD + "Simbolo: " + entry.getKey());
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
        info.add("Para invertir en estas materias primas clickea en cualquiera de ellas y elige la cantidad a comprar");
        info.add("                  ");
        info.add("AVISO: ¡Estas materias primas van con unos 15 minutos aproximadamente de retraso! ");
        infoMeta.setLore(info);
        infoMeta.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "INFO");
        papel.setItemMeta(infoMeta);
        inventory.setItem(4, papel);
        player.openInventory(inventory);

        Bukkit.getScheduler().scheduleAsyncDelayedTask(Pixelcoin.getInstance(), () -> {
            //Añadimos el precop de la accion
            String alias;
            List<String> precio = new ArrayList<>();
            int pos = 0;
            String ticker;

            for (ItemStack actual : inventory.getContents()) {
                if (pos == 4 || actual == null || actual.getType().toString().equalsIgnoreCase("AIR")) {
                    break;
                }
                pos++;

                ItemMeta actualMeta = actual.getItemMeta();
                precio = actualMeta.getLore();
                precio.remove(1);

                ticker = precio.get(0).split(" ")[1];
                try {
                    double preioMat = IEXCloud_API.getPrecioMateriaPrima(ticker);

                    precio.add(1, ChatColor.GOLD + "Precio/Unidad:" + ChatColor.GREEN + " " + preioMat + " $");

                    actualMeta.setLore(precio);
                    actual.setItemMeta(actualMeta);

                    precio.clear();
                } catch (Exception e) {
                    player.sendMessage(ChatColor.DARK_RED + "No hagas spam del comando");
                }
            }
        }, 0L);
    }

    private void buildPag1(){
        pag1.put("DCOILBRENTEU", "Petroleo (Brent)");
        pag1.put("DHHNGSP", "Gas natural");
        pag1.put("DJFUELUSGULF", "Queroseno (Combustible aviones)");
        pag1.put("GASDESW","Diesel");
    }

    @Override
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();
        if(itemStack == null || !itemStack.getType().toString().equalsIgnoreCase("COAL")){
            return;
        }

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
        ComprarBolsaSolicitud comprarAccionSolicitud1 = new ComprarBolsaSolicitud(simbolo, PosicionesAbiertas.TIPOS.MATERIAS_PRIMAS.toString(), alias, player.getName(), precio);
        comprarAccionSolicitud1.enviarSolicitud();
    }
}
