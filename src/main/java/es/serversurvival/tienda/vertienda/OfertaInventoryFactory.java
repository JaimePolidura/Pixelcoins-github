package es.serversurvival.tienda.vertienda;

import es.jaimetruman.ItemBuilder;
import es.serversurvival._shared.menus.inventory.InventoryFactory;
import es.serversurvival._shared.menus.Paginated;
import es.serversurvival.tienda._shared.mySQL.ofertas.Ofertas;
import es.serversurvival.tienda._shared.newformat.domain.TiendaObjeto;
import es.serversurvival._shared.utils.MinecraftUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OfertaInventoryFactory extends InventoryFactory {
    public final static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "            Tienda";
    private List<ItemStack> itemExcessInventory = new ArrayList<>();

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 54, titulo);

        List<TiendaObjeto> ofertas = ofertasMySQL.getTodasOfertas();

        for(int i = 0; i < ofertas.size(); i++){
            TiendaObjeto oferta = ofertas.get(i);

            ItemStack itemStackAInsertar = ofertasMySQL.getItemOferta(oferta);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Precio: " + ChatColor.GREEN + formatea.format(oferta.getPrecio()) + " PC");
            lore.add(ChatColor.GOLD + "Venderdor: " + oferta.getJugador());
            lore.add("" + oferta.getTiendaObjetoId());

            String displayName;
            if(oferta.getJugador().equalsIgnoreCase(jugador)){
                displayName = Ofertas.NOMBRE_ITEM_RETIRAR;
            }else{
                displayName = Ofertas.NOMBRE_ITEM_COMPRAR;
            }

            MinecraftUtils.setLoreAndDisplayName(itemStackAInsertar, lore, displayName);

            if(i < 52){
                inventory.addItem(itemStackAInsertar);
            }else{
                itemExcessInventory.add(itemStackAInsertar);
            }
        }

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
            ItemBuilder.of(Material.RED_WOOL).title(Paginated.ITEM_NAME_GOBACK).buildAddInventory(inventory, 51);
            ItemBuilder.of(Material.GREEN_WOOL).title(Paginated.ITEM_NAME_GOFORDWARD).buildAddInventory(inventory, 52);
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

        return ItemBuilder.of(Material.PAPER).title(ChatColor.GOLD + "" + ChatColor.BOLD + "INFO").lore(lore).build();
    }
}
