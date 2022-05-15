package es.serversurvival.jugadores.venderjugador;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.menus.Menu;
import es.serversurvival._shared.menus.inventory.InventoryCreator;
import es.serversurvival._shared.menus.solicitudes.Solicitud;
import es.serversurvival.jugadores.pagar.PagarUseCase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;

public class VenderJugadorSolicitud extends Menu implements Solicitud {
    private final PagarUseCase pagarUseCase;

    private boolean isClicked = false;

    private final Inventory inventory;
    private final Player jugadorAVender;
    private final Player jugadorVendedor;
    private final ItemStack itemAVender;
    private final int slotItemVender;
    private final double precio;

    public VenderJugadorSolicitud(Player jugadorVendedor, Player jugadorAVender, ItemStack itemAVender, double precio) {
        this.jugadorVendedor = jugadorVendedor;
        this.jugadorAVender = jugadorAVender;
        this.itemAVender = itemAVender;
        this.precio = precio;
        this.inventory = buildInventory();
        this.slotItemVender = jugadorVendedor.getInventory().getHeldItemSlot();
        this.pagarUseCase = new PagarUseCase();

        enviarSolicitud();
    }

    private Inventory buildInventory () {
        String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "   OFERTA DE COMPRA";

        String nombreAceptar = ChatColor.GREEN + "" + ChatColor.BOLD + "COMPRAR";
        List<String> loreAceptar = Arrays.asList(ChatColor.GOLD + jugadorVendedor.getName() + " quiere venderte " + itemAVender.getAmount() + " de ", ChatColor.GOLD +
                itemAVender.getType().toString() + " a " + ChatColor.GREEN + FORMATEA.format(precio) + " PC " + ChatColor.GOLD + " en total");

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

        this.pagarUseCase.realizarPago(jugadorAVender.getName(), jugadorVendedor.getName(), precio);

        jugadorAVender.getInventory().addItem(itemAVender);
        jugadorVendedor.getInventory().clear(slotItemVender);

        jugadorVendedor.sendMessage(ChatColor.GOLD + jugadorAVender.getName() + " te ha compradoo " + itemAVender.getType() + " al precio de " + ChatColor.GREEN + FORMATEA.format(precio) + " PC");
        jugadorVendedor.playSound(jugadorVendedor.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        jugadorAVender.sendMessage(ChatColor.GOLD + "Has comprado el item");
        jugadorAVender.playSound(jugadorAVender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Pixelcoin.publish(new ItemVendidoJugadorEvento(jugadorAVender.getName(), jugadorVendedor.getName(), precio, itemAVender.getType().toString()));

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
