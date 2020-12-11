package es.serversurvival.menus.menus.solicitudes;

import es.serversurvival.menus.Menu;
import es.serversurvival.menus.inventoryFactory.InventoryCreator;
import es.serversurvival.mySQL.MySQL;
import es.serversurvival.mySQL.enums.TipoTransaccion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

public class VenderJugadorSolicitud extends Menu implements Solicitud {
    private boolean isClicked = false;

    private Inventory inventory;
    private Player jugadorAVender;
    private Player jugadorVendedor;
    private ItemStack itemAVender;
    private int slotItemVender;
    private double precio;

    public VenderJugadorSolicitud (Player jugadorVendedor, Player jugadorAVender, ItemStack itemAVender, double precio) {
        this.jugadorVendedor = jugadorVendedor;
        this.jugadorAVender = jugadorAVender;
        this.itemAVender = itemAVender;
        this.precio = precio;
        this.inventory = buildInventory();
        this.slotItemVender = jugadorVendedor.getInventory().getHeldItemSlot();
    }

    private Inventory buildInventory () {
        String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "   OFERTA DE COMPRA";

        String nombreAceptar = ChatColor.GREEN + "" + ChatColor.BOLD + "COMPRAR";
        List<String> loreAceptar = Arrays.asList(ChatColor.GOLD + jugadorVendedor.getName() + " quiere venderte " + itemAVender.getAmount() + " de ", ChatColor.GOLD +
                itemAVender.getType().toString() + " a " + ChatColor.GREEN + formatea.format(precio) + " PC " + ChatColor.GOLD + " en total");

        String nomrbreCancelar = ChatColor.RED +  "" + ChatColor.BOLD + "RECHAZAR";
        List<String> loreCancelar = Arrays.asList(ChatColor.GOLD + "Cancelar la oferta de " + jugadorVendedor.getName());

        return InventoryCreator.createSolicitud(titulo, nombreAceptar, loreAceptar, nomrbreCancelar, loreCancelar);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public Player getPlayer() {
        return jugadorAVender;
    }

    @Override
    public String getDestinatario() {
        return jugadorAVender.getName();
    }

    @Override
    public void enviarSolicitud() {
        openMenu();

        jugadorVendedor.sendMessage(ChatColor.GOLD + "Has enviado la solicitud");
    }

    @Override
    public void aceptar() {
        isClicked = true;

        MySQL.conectar();
        transaccionesMySQL.realizarTransferencia(jugadorAVender.getName(), jugadorVendedor.getName(), precio, itemAVender.getType().toString(), TipoTransaccion.JUGADOR_VENDER, true);
        MySQL.desconectar();

        jugadorAVender.getInventory().addItem(itemAVender);
        jugadorVendedor.getInventory().clear(slotItemVender);

        jugadorVendedor.sendMessage(ChatColor.GOLD + jugadorAVender.getName() + " te ha compradoo " + itemAVender.getType().toString() + " al precio de " + ChatColor.GREEN + formatea.format(precio) + " PC");
        jugadorVendedor.playSound(jugadorVendedor.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        jugadorAVender.sendMessage(ChatColor.GOLD + "Has comprado el item");
        jugadorAVender.playSound(jugadorAVender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        closeMenu();
    }

    @Override
    public void cancelar() {
        isClicked = true;

        jugadorAVender.sendMessage(ChatColor.GOLD + "Has cancelado la solicitud");
        if(Bukkit.getPlayer(jugadorVendedor.getName()) != null){
            jugadorVendedor.sendMessage(ChatColor.GOLD + "Te ha cancelado la solicitud");
        }

        closeMenu();
    }

    @Override
    public void onCloseInventory(InventoryCloseEvent event) {
        if(!isClicked && Bukkit.getPlayer(jugadorVendedor.getName()) != null){
            jugadorVendedor.sendMessage(ChatColor.GOLD + "Te ha cancelado la solicitud");
        }
    }
}
