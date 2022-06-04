package es.serversurvival.jugadores.venderjugador;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.menus.ConfirmacionMenu;
import es.serversurvival.jugadores.pagar.PagarUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class VenderJugadorConfirmacionMenu extends ConfirmacionMenu {
    private final Player jugadorComprador;
    private final Player jugadorVendedor;
    private final ItemStack itemAVender;
    private final int slotItemVender;
    private final double precio;

    @Override
    public void onAceptar(Player player, InventoryClickEvent event) {
        var itemToVenderIsIntVendedorInventory = this.isItemInInventory(jugadorVendedor, itemAVender, slotItemVender);
        if(!itemToVenderIsIntVendedorInventory){
            this.jugadorVendedor.sendMessage(DARK_RED + "casi maquina");
            this.jugadorComprador.sendMessage(DARK_RED + "No se ha podido completar el pago, por que el jugador ha movido el objeto a venderte");
            return;
        }

        var pagarUseCase = new PagarUseCase();

        pagarUseCase.realizarPago(jugadorComprador.getName(), jugadorVendedor.getName(), precio);

        jugadorComprador.getInventory().addItem(itemAVender);
        jugadorVendedor.getInventory().clear(slotItemVender);

        jugadorVendedor.sendMessage(ChatColor.GOLD + jugadorComprador.getName() + " te ha compradoo " + itemAVender.getType() +
                " al precio de " + ChatColor.GREEN + FORMATEA.format(precio) + " PC");
        jugadorVendedor.playSound(jugadorVendedor.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        jugadorComprador.sendMessage(ChatColor.GOLD + "Has comprado el item");
        jugadorComprador.playSound(jugadorComprador.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Pixelcoin.publish(new ItemVendidoJugadorEvento(jugadorComprador.getName(), jugadorVendedor.getName(), precio, itemAVender.getType().toString()));
    }

    private boolean isItemInInventory(Player jugadorVendedor, ItemStack itemAVender, int slotItemVender) {
        var itemAVenderEnInventario = jugadorVendedor.getInventory().getItem(slotItemVender);

        return itemAVenderEnInventario != null && itemAVenderEnInventario.equals(itemAVender);
    }

    @Override
    public String titulo() {
        return DARK_RED + "" + BOLD + "   OFERTA DE COMPRA";
    }

    @Override
    public ItemStack aceptarItem() {
        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(ChatColor.GREEN + "" + ChatColor.BOLD + "COMPRAR")
                .lore(List.of(
                        GOLD + "Aceptar la oferta de " + jugadorVendedor.getName(),
                        GOLD + "Precio: " + GREEN + FORMATEA.format(precio) + "PC"
                ))
                .build();
    }
}
