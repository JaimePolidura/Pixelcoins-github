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
        String inNombre = event.getView().getTitle().toString();
        Player p = (Player) event.getWhoClicked();

        if (inNombre.equalsIgnoreCase(ChatColor.DARK_RED + "" + ChatColor.BOLD + "            Tienda")) {
            Funciones f = new Funciones();
            int slotsLibres = f.espaciosLibres(p.getInventory());

            if (slotsLibres != 0) {
                ItemStack i = event.getCurrentItem();
                String dn = i.getItemMeta().getDisplayName();
                int id = Integer.parseInt(i.getItemMeta().getLore().get(2));

                if (dn.equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "CLICK PARA RETIRAR")) {
                    Ofertas o = new Ofertas();
                    o.conectar();
                    o.retirarOferta(p, id);
                    this.refresh();
                    o.desconectar();
                } else if (dn.equalsIgnoreCase(ChatColor.AQUA + "" + ChatColor.BOLD + "CLICK PARA COMPRAR")) {
                    Transacciones t = new Transacciones();
                    t.conectar();
                    t.realizarVenta(p.getName(), id, p);
                    this.refresh();
                    t.desconectar();
                }
                event.setCancelled(true);

            } else {
                p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario llenos :v");
                event.setCancelled(true);
            }
        }
    }

    private Inventory buildInv(){
        inventory = Bukkit.createInventory(null, 54, titulo);
        ArrayList<String> lore = new ArrayList<>();
        String vendedor = "";
        double precio = 0;
        int id;
        ItemStack item = null;
        ItemMeta im = null;

        Ofertas o = new Ofertas();
        o.conectar();
        List<Oferta> ofertas = o.getTodasOfertas();


        for (Oferta oferta : ofertas) {
            lore.clear();
            vendedor = oferta.getNombre();
            precio = oferta.getPrecio();
            id = oferta.getId_oferta();

            item = o.getItemOferta(id);
            im = item.getItemMeta();

            lore.add(ChatColor.GOLD + "Precio: " + ChatColor.GREEN + formatea.format(precio) + " PC");
            lore.add(ChatColor.GOLD + "Venderdor: " + vendedor);
            lore.add("" + id);

            im.setLore(lore);

            if (vendedor.equalsIgnoreCase(player.getName())) {
                im.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "CLICK PARA RETIRAR");
            } else {
                im.setDisplayName(ChatColor.AQUA + "" + ChatColor.BOLD + "CLICK PARA COMPRAR");
            }
            item.setItemMeta(im);

            inventory.addItem(item);
        }
        return inventory;
    }

    @Override
    public void openMenu() {
        buildInv();
        player.openInventory(inventory);
        Menu.activeMenus.add(this);
    }

    @Override
    public void closeMenu() {
        Menu.activeMenus.remove(this);
    }

    @Override
    public void refresh() {
        Inventory newInv = buildInv();
        HashSet<Menu> copia = new HashSet<>();
        copia.addAll(Menu.activeMenus);

        copia.forEach(menu -> {
            if(menu instanceof OfertasMenu){
                refreshSinglePlayer(inventory, (OfertasMenu) menu);
            }
        });
    }

    private void refreshSinglePlayer(Inventory inventory, OfertasMenu ofertaMenu){
        ofertaMenu.getPlayer().openInventory(inventory);
        Menu.activeMenus.add(ofertaMenu);
    }
}
