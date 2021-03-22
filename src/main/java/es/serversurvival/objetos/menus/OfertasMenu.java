package es.serversurvival.objetos.menus;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Ofertas;
import es.serversurvival.objetos.mySQL.Transacciones;
import es.serversurvival.objetos.mySQL.tablasObjetos.Oferta;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.*;

public class OfertasMenu extends Menu implements Clickleable, Refreshcable {
    private Transacciones transaccionesMySQL = new Transacciones();
    private Ofertas ofertasMySQL = new Ofertas();
    private Inventory inventory;
    private static DecimalFormat formatea = new DecimalFormat("###,###.##");
    private Player player;
    private String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "            Tienda";

    public OfertasMenu(Player player){
        this.player = player;
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
    public void onInventoryClick(InventoryClickEvent event) {
        Player p = (Player) event.getWhoClicked();
        int slotsLibres = Funciones.espaciosLibres(p.getInventory());

        if (slotsLibres != 0) {
            ItemStack itemClicked = event.getCurrentItem();
            String itemClickckedDisplayName = itemClicked.getItemMeta().getDisplayName();
            int id = Integer.parseInt(itemClicked.getItemMeta().getLore().get(2));

            if (itemClickckedDisplayName.equalsIgnoreCase(Ofertas.NOMBRE_ITEM_RETIRAR)) {
                ofertasMySQL.conectar();
                ofertasMySQL.retirarOferta(p, id);
                ofertasMySQL.desconectar();

                refresh();
            } else if (itemClickckedDisplayName.equalsIgnoreCase(Ofertas.NOMBRE_ITEM_COMPRAR)) {
                transaccionesMySQL.conectar();
                transaccionesMySQL.realizarVenta(p.getName(), id, p);
                transaccionesMySQL.desconectar();

                refresh();
            }
            event.setCancelled(true);

        } else {
            p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario lleno :v");
        }
    }

    private Inventory buildInv(){
        inventory = Bukkit.createInventory(null, 54, titulo);

        ofertasMySQL.conectar();
        List<Oferta> ofertas = ofertasMySQL.getTodasOfertas();

        ofertas.forEach((oferta) -> {
            ItemStack itemStackAInsertar = ofertasMySQL.getItemOferta(oferta.getId_oferta());
            ItemMeta itemMetaItemStackAInsertar = itemStackAInsertar.getItemMeta();
            List<String> lore = new ArrayList<>();

            lore.add(ChatColor.GOLD + "Precio: " + ChatColor.GREEN + formatea.format(oferta.getPrecio()) + " PC");
            lore.add(ChatColor.GOLD + "Venderdor: " + oferta.getNombre());
            lore.add("" + oferta.getId_oferta());

            if(oferta.getNombre().equalsIgnoreCase(player.getName())){
                itemMetaItemStackAInsertar.setDisplayName(Ofertas.NOMBRE_ITEM_RETIRAR);
            }else{
                itemMetaItemStackAInsertar.setDisplayName(Ofertas.NOMBRE_ITEM_COMPRAR);
            }

            itemMetaItemStackAInsertar.setLore(lore);
            itemStackAInsertar.setItemMeta(itemMetaItemStackAInsertar);
            inventory.addItem(itemStackAInsertar);
        });
        ofertasMySQL.desconectar();

        return inventory;
    }

    @Override
    public void openMenu() {
        buildInv();
        player.openInventory(inventory);
        activeMenus.add(this);
    }

    @Override
    public void closeMenu() {
        activeMenus.remove(this);
    }

    @Override
    public void refresh() {
        Inventory newInv = buildInv();
        HashSet<Menu> copia = new HashSet<>();
        copia.addAll(activeMenus);

        copia.forEach(menu -> {
            if (menu instanceof OfertasMenu) {
                refreshSinglePlayer(inventory, (OfertasMenu) menu);
            }
        });
    }

    private void refreshSinglePlayer(Inventory inventory, OfertasMenu ofertaMenu){
        ofertaMenu.getPlayer().openInventory(inventory);
        activeMenus.add(ofertaMenu);
    }
}
