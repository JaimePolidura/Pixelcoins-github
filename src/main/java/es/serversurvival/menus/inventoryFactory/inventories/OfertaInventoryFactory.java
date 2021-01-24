package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.menus.menus.Paginated;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.Ofertas;
import es.serversurvival.mySQL.tablasObjetos.Oferta;
import es.serversurvival.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class OfertaInventoryFactory extends InventoryFactory {
    public final static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "            Tienda";
    private List<ItemStack> itemExcessInventory = new ArrayList<>();

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 54, titulo);

        MySQL.conectar();
        List<Oferta> ofertas = ofertasMySQL.getTodasOfertas();

        for(int i = 0; i < ofertas.size(); i++){
            Oferta oferta = ofertas.get(i);

            ItemStack itemStackAInsertar = ofertasMySQL.getItemOferta(oferta);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Precio: " + ChatColor.GREEN + formatea.format(oferta.getPrecio()) + " PC");
            lore.add(ChatColor.GOLD + "Venderdor: " + oferta.getJugador());
            lore.add("" + oferta.getId());

            String displayName;
            if(oferta.getJugador().equalsIgnoreCase(jugador)){
                displayName = Ofertas.NOMBRE_ITEM_RETIRAR;
            }else{
                displayName = Ofertas.NOMBRE_ITEM_COMPRAR;
            }

            ItemBuilder.setLoreAndDisplayName(itemStackAInsertar, lore, displayName);

            if(i < 52){
                inventory.addItem(itemStackAInsertar);
            }else{
                itemExcessInventory.add(itemStackAInsertar);
            }
        }

        MySQL.desconectar();

        inventory.setItem(53, buildItemInfo());


        if(itemExcessInventory.size() > 0){
            inventory.setItem(52, buildItemFordward());
        }

        return inventory;
    }

    public Inventory buildInventoryExecess () {
        Inventory inventory = Bukkit.createInventory(null, 54, titulo);

        List<ItemStack> copyOfItemExcessList = new ArrayList<>(itemExcessInventory);
        itemExcessInventory.clear();

        boolean addFordwardItem = false;

        for(int i = 0; i < copyOfItemExcessList.size(); i++){
            if(i < 51){
                inventory.addItem(copyOfItemExcessList.get(i));
            }else{
                itemExcessInventory.add(copyOfItemExcessList.get(i));
                addFordwardItem = true;
            }
        }

        inventory.setItem(53, buildItemInfo());
        if(addFordwardItem){
            inventory.setItem(51, buildItemBack());
            inventory.setItem(52, buildItemFordward());
        }else{
            inventory.setItem(52, buildItemGoBack());
        }

        return inventory;
    }

    private ItemStack buildItemInfo () {
        List<String> lore = new ArrayList<>();
        lore.add("Para vender un objeto: 1) selecciona");
        lore.add("el item con la mano y 2) pon el comando:");
        lore.add("/vender <precio>");

        return ItemBuilder.loreDisplayName(Material.PAPER, ChatColor.GOLD + "" + ChatColor.BOLD + "INFO", lore);
    }

    private ItemStack buildItemBack () {
        return ItemBuilder.displayname(Material.RED_WOOL, Paginated.ITEM_NAME_GOBACK);
    }

    private ItemStack buildItemFordward () {
        return ItemBuilder.displayname(Material.GREEN_WOOL, Paginated.ITEM_NAME_GOFORDWARD);
    }
}
