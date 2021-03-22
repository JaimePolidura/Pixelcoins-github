package es.serversurvival.eventos;

import es.serversurvival.objetos.Empresas;
import es.serversurvival.objetos.Ofertas;
import es.serversurvival.objetos.Transacciones;
import es.serversurvival.main.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerInventoryClick implements Listener {

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent e) {
        String inNombre = e.getView().getTitle().toString();

        if (inNombre.equalsIgnoreCase(ChatColor.DARK_RED + "" + ChatColor.BOLD + "            Tienda")) {
            Funciones f = new Funciones();
            Player p = (Player) e.getWhoClicked();
            int slotsLibres = f.espaciosLibres(p.getInventory());

            if (slotsLibres != 0) {
                //Si clickas a un espacio libre salta error -> lo solucionamos con el try
                try {
                    ItemStack i = e.getCurrentItem();
                    String dn = i.getItemMeta().getDisplayName();
                    int id = Integer.parseInt(i.getItemMeta().getLore().get(2));

                    //Comprobar si lo que has clickeado es tuyo o de otro jugador
                    if (dn.equalsIgnoreCase(ChatColor.RED + "" + ChatColor.BOLD + "CLICK PARA RETIRAR")) {
                        Ofertas o = new Ofertas();
                        o.conectar();
                        o.retirarOferta(p, id);
                        o.desconectar();
                    } else if (dn.equalsIgnoreCase(ChatColor.AQUA + "" + ChatColor.BOLD + "CLICK PARA COMPRAR")) {
                        Transacciones t = new Transacciones();
                        t.conectar();
                        t.realizarVenta(p.getName(), id, p);
                        t.desconectar();
                    }
                    e.setCancelled(true);

                    //si no es int la id, el evento se cancela sino si eres el propietario te da el objeto y si no te da el objeto...

                } catch (Exception e2) {
                    e.setCancelled(true);
                }

            } else {
                p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario llenos :v");
                e.setCancelled(true);
            }
        }

        if (inNombre.equalsIgnoreCase(ChatColor.DARK_RED + "" + ChatColor.BOLD + "          Empresas")) {
            try {
                Empresas em = new Empresas();
                em.conectar();
                em.mostrarEmpresas((Player) e.getWhoClicked());
                em.desconectar();
            } catch (Exception ex) {

            }
            e.setCancelled(true);
        }
    }
}
