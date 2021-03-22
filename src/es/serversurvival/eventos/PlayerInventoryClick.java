package es.serversurvival.eventos;

import es.serversurvival.comandos.comandos.TopComando;
import es.serversurvival.comandos.subComandos.bolsa.ValoresBolsaSubComando;
import es.serversurvival.objetos.mySQL.Empresas;
import es.serversurvival.objetos.mySQL.Ofertas;
import es.serversurvival.objetos.mySQL.Transacciones;
import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.solicitudes.ComprarAccionSolicitud;
import es.serversurvival.objetos.solicitudes.Solicitud;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlayerInventoryClick implements Listener {

    @EventHandler
    public void onPlayerInventoryClick(InventoryClickEvent e) throws Exception {
        String inNombre = e.getView().getTitle().toString();
        Player p = (Player) e.getWhoClicked();

        try {
            if (inNombre.equalsIgnoreCase(ChatColor.DARK_RED + "" + ChatColor.BOLD + "            Tienda")) {
                Funciones f = new Funciones();
                int slotsLibres = f.espaciosLibres(p.getInventory());

                if (slotsLibres != 0) {
                    ItemStack i = e.getCurrentItem();
                    String dn = i.getItemMeta().getDisplayName();
                    int id = Integer.parseInt(i.getItemMeta().getLore().get(2));

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

                } else {
                    p.sendMessage(ChatColor.DARK_RED + "Tienes el inventario llenos :v");
                    e.setCancelled(true);
                }
            }

            if (inNombre.equalsIgnoreCase(ChatColor.DARK_RED + "" + ChatColor.BOLD + "          Empresas")) {
                Empresas em = new Empresas();
                em.conectar();
                em.mostrarEmpresas((Player) e.getWhoClicked());
                em.desconectar();
                e.setCancelled(true);
            }

            if (inNombre.equalsIgnoreCase(TopComando.titulo)) {
                e.setCancelled(true);
            }

            try {
                String tituloSol = Solicitud.getByDestinatario(p.getName()).getTitulo();

                if (inNombre.equalsIgnoreCase(tituloSol)) {
                    Solicitud solicitud = Solicitud.getByDestinatario(p.getName());
                    String nombreItem = null;
                    try {
                        nombreItem = e.getCurrentItem().getType().toString();
                    } catch (NullPointerException ex) {
                        return;
                    }

                    switch (nombreItem) {
                        case "GREEN_WOOL":
                            solicitud.aceptar();
                            break;
                        case "RED_WOOL":
                            solicitud.cancelar();
                            break;
                        default:
                            if (solicitud instanceof ComprarAccionSolicitud && nombreItem != "AIR") {
                                ComprarAccionSolicitud comprarAccionSolicitud = (ComprarAccionSolicitud) solicitud;
                                comprarAccionSolicitud.updateNAcciones(e.getCurrentItem());
                                e.setCancelled(true);
                                return;
                            }
                    }
                    //e.setCancelled(true);
                }
            } catch (Exception exception) {
            }

            if (inNombre.equalsIgnoreCase(ValoresBolsaSubComando.titulo)) {
                e.setCancelled(true);
                ItemStack item = e.getCurrentItem();

                if (item.getType().toString().equalsIgnoreCase("PAPER")) {
                    return;
                }
                List<String> lore = item.getItemMeta().getLore();
                String precioLore = lore.get(1);
                if (precioLore.equalsIgnoreCase(ChatColor.RED + "Cargando...")) {
                    return;
                }
                double precio = Double.parseDouble(lore.get(1).split(" ")[1]);
                String ticker = lore.get(0).split(" ")[1];

                ComprarAccionSolicitud comprarAccionSolicitud = new ComprarAccionSolicitud(ticker, p.getName(), precio);
                comprarAccionSolicitud.enviarSolicitud();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            e.setCancelled(true);
        }
    }
}