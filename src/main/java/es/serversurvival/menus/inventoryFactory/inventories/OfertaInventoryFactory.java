package es.serversurvival.menus.inventoryFactory.inventories;

import es.serversurvival.menus.inventoryFactory.InventoryFactory;
import es.serversurvival.menus.menus.Paginated;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.Ofertas;
import es.serversurvival.mySQL.Transacciones;
import es.serversurvival.mySQL.tablasObjetos.Oferta;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import sun.util.resources.cldr.bn.TimeZoneNames_bn;

import java.text.DecimalFormat;
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
        int posInv = 0;

        for (Oferta oferta : ofertas) {
            ItemStack itemStackAInsertar = ofertasMySQL.getItemOferta(oferta);
            ItemMeta itemMetaItemStackAInsertar = itemStackAInsertar.getItemMeta();
            List<String> lore = new ArrayList<>();

            lore.add(ChatColor.GOLD + "Precio: " + ChatColor.GREEN + formatea.format(oferta.getPrecio()) + " PC");
            lore.add(ChatColor.GOLD + "Venderdor: " + oferta.getNombre());
            lore.add("" + oferta.getId_oferta());

            if(oferta.getNombre().equalsIgnoreCase(jugador)){
                itemMetaItemStackAInsertar.setDisplayName(Ofertas.NOMBRE_ITEM_RETIRAR);
            }else{
                itemMetaItemStackAInsertar.setDisplayName(Ofertas.NOMBRE_ITEM_COMPRAR);
            }

            itemMetaItemStackAInsertar.setLore(lore);
            itemStackAInsertar.setItemMeta(itemMetaItemStackAInsertar);

            if(posInv < 52){
                inventory.addItem(itemStackAInsertar);
            }else{
                itemExcessInventory.add(itemStackAInsertar);
            }

            posInv++;
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
        ItemStack info = new ItemStack(Material.PAPER);
        ItemMeta infoMeta = info.getItemMeta();

        infoMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "INFO");
        List<String> lore = new ArrayList<>();
        lore.add("Para vender un objeto: 1) selecciona");
        lore.add("el item con la mano y 2) pon el comando:");
        lore.add("/vender <precio>");

        infoMeta.setLore(lore);
        info.setItemMeta(infoMeta);

        return info;
    }

    private ItemStack buildItemBack () {
        ItemStack back = new ItemStack(Material.RED_WOOL);
        ItemMeta backMeta = back.getItemMeta();
        backMeta.setDisplayName(Paginated.ITEM_NAME_GOBACK);
        back.setItemMeta(backMeta);

        return back;
    }

    private ItemStack buildItemFordward () {
        ItemStack fordward = new ItemStack(Material.GREEN_WOOL);
        ItemMeta forwardMeta = fordward.getItemMeta();
        forwardMeta.setDisplayName(Paginated.ITEM_NAME_GOFORDWARD);
        fordward.setItemMeta(forwardMeta);

        return fordward;
    }
}
