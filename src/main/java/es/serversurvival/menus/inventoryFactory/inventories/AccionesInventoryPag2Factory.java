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

public class AccionesInventoryPag2Factory extends InventoryFactory {
    private Map<String, String> pag2 = new HashMap<>();

    @Override
    protected Inventory buildInventory(String player) {
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

        for (Map.Entry<String, String> entry : pag2.entrySet()) {
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

        ItemStack nextPage = new ItemStack(Material.RED_WOOL);
        ItemMeta itemMetaNextPage = nextPage.getItemMeta();
        itemMetaNextPage.setDisplayName(Paginated.ITEM_NAME_GOBACK);
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
        pag2.put("TSM", "Taiwan semiconductor manufacturing ");
        pag2.put("JNJ", "Johnson&Johnson");
        pag2.put("MCO", "Moodys corporation");
        pag2.put("FNMA", "Fannie Mae");
        pag2.put("AZN", "AstraZeneca");
        pag2.put("SAP", "SAP");
        pag2.put("LOGI", "Logitech");
    }
}
