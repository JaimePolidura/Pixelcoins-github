package es.serversurvival.menus.menus.confirmaciones;

import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.menus.menus.BolsaCarteraMenu;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.PosicionesAbiertas;
import es.serversurvival.mySQL.Transacciones;
import es.serversurvival.mySQL.tablasObjetos.PosicionAbierta;
import es.serversurvival.mySQL.enums.POSICION;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.text.DecimalFormat;
import java.util.*;

public class VenderAccionesConfirmacion extends Menu implements Confirmacion{
    private Inventory inventory;
    private Player player;
    private POSICION tipoPosicion;
    private int id;

    public VenderAccionesConfirmacion (Player player, POSICION tipoPosicion, String accionesCompradas,
                                       String valorTotal, String beneficios, String rentabilidad, int id) {
        this.player = player;
        this.id = id;
        this.tipoPosicion = tipoPosicion;

        String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "     ¿Quieres vender?";
        String tituloItemVender = ChatColor.GREEN + "" + ChatColor.BOLD + "VENDER";
        List<String> loreVender;
        System.out.println(beneficios);
        if(beneficios.charAt(2) == '+'){
            loreVender = Arrays.asList(ChatColor.GOLD +  "Vender " + accionesCompradas + " acciones con unos beneficios de " + ChatColor.GREEN + beneficios + " PC", ChatColor.GOLD + "y una rentabilidad del " + ChatColor.GREEN + rentabilidad);
        }else{
            loreVender = Arrays.asList(ChatColor.GOLD +  "Vender " + accionesCompradas + " acciones con unas perdidas de " + ChatColor.RED + beneficios + " PC", ChatColor.GOLD + " y una rentabilidad del " + ChatColor.RED + rentabilidad);
        }
        String tituloItemCancelar = ChatColor.RED + "" + ChatColor.BOLD + "CANCELAR";
        List<String> loreCancelar = Arrays.asList("");

        this.inventory = InventoryCreator.createSolicitud(titulo, tituloItemVender, loreVender, tituloItemCancelar, loreCancelar);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void confirmar() {
        MySQL.conectar();

        PosicionAbierta posicionAVender = posicionesAbiertasMySQL.getPosicionAbierta(id);
        if(tipoPosicion == POSICION.LARGO){
            transaccionesMySQL.venderPosicion(posicionAVender, posicionAVender.getCantidad(), player);
        }else{
            transaccionesMySQL.comprarPosicionCorto(posicionAVender, posicionAVender.getCantidad(), player);
        }
        MySQL.desconectar();

        closeMenu();
    }

    @Override
    public void cancelar() {
        BolsaCarteraMenu menu = new BolsaCarteraMenu(player);
        menu.openMenu();
    }
}
