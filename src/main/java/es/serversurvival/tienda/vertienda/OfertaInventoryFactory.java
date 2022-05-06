package es.serversurvival.tienda.vertienda;

import es.jaimetruman.ItemBuilder;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.menus.inventory.InventoryFactory;
import es.serversurvival._shared.menus.Paginated;
import es.serversurvival._shared.utils.ItemsUtils;
import es.serversurvival.tienda._shared.application.TiendaService;
import es.serversurvival.tienda._shared.domain.TiendaObjeto;
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
    public final static String NOMBRE_ITEM_RETIRAR = ChatColor.RED + "" + ChatColor.BOLD + "CLICK PARA RETIRAR";
    public final static String NOMBRE_ITEM_COMPRAR = ChatColor.AQUA + "" + ChatColor.BOLD + "CLICK PARA COMPRAR";

    private final List<ItemStack> itemExcessInventory = new ArrayList<>();
    private final TiendaService tiendaService;

    public OfertaInventoryFactory(){
        this.tiendaService = DependecyContainer.get(TiendaService.class);
    }

    @Override
    protected Inventory buildInventory(String jugador) {
        Inventory inventory = Bukkit.createInventory(null, 54, titulo);

        List<TiendaObjeto> ofertas = this.tiendaService.findAll();

        for(int i = 0; i < ofertas.size(); i++){
            TiendaObjeto itemTienda = ofertas.get(i);

            ItemStack itemStackAInsertar = ItemsUtils.getItemStakcByTiendaObjeto(itemTienda);

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.GOLD + "Precio: " + ChatColor.GREEN + formatea.format(itemTienda.getPrecio()) + " PC");
            lore.add(ChatColor.GOLD + "Venderdor: " + itemTienda.getJugador());
            lore.add("" + itemTienda.getTiendaObjetoId());

            String displayName = itemTienda.getJugador().equalsIgnoreCase(jugador) ? NOMBRE_ITEM_RETIRAR : NOMBRE_ITEM_COMPRAR;

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
