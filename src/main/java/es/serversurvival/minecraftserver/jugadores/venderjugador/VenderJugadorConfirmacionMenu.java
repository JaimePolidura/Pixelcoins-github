package es.serversurvival.minecraftserver.jugadores.venderjugador;

import es.bukkitbettermenus.utils.ItemBuilder;
import es.bukkitbettermenus.utils.ItemUtils;
import es.jaime.EventBus;
import es.serversurvival.minecraftserver._shared.menus.ConfirmacionMenu;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.jugadores.pagar.HacerPagarParametros;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class VenderJugadorConfirmacionMenu extends ConfirmacionMenu<VenderJugadorConfirmacionMenuState> {
    private final UseCaseBus useCaseBus;

    @Override
    public void onAceptar(Player destinatario, InventoryClickEvent event, VenderJugadorConfirmacionMenuState state) {
        var vendedor = state.jugadorVendedor();
        var comprador = state.jugadorComprador();
        var itemAVender = state.itemAVender();
        var precio = state.precio();
        var slotItemVender = state.slotItemVender();

        if(!isItemInInventory(vendedor, itemAVender, slotItemVender)){
            vendedor.sendMessage(DARK_RED + "casi maquina");
            comprador.sendMessage(DARK_RED + "No se ha podido completar el pago, por que el " + getState().jugadorVendedor().getName() + " ha movido el objeto a venderte");
            return;
        }
        if(!state.jugadorVendedor().isOnline()){
            comprador.sendMessage(DARK_RED + "No se ha podido completar el pago, por que el " + getState().jugadorVendedor().getName() + " no esta online");
            return;
        }
        if(!state.jugadorComprador().isOnline()){
            vendedor.sendMessage(DARK_RED + "No se ha podido completar el pago, por que el " + getState().jugadorComprador().getName() + " no esta online");
            return;
        }

        useCaseBus.handle(HacerPagarParametros.of(comprador.getUniqueId(), vendedor.getUniqueId(), precio));

        comprador.getInventory().addItem(itemAVender);
        vendedor.getInventory().clear(slotItemVender);

        vendedor.sendMessage(GOLD + comprador.getName() + " te ha compradoo " + itemAVender.getType() +
                " al precio de " + formatPixelcoins(precio));
        vendedor.playSound(vendedor.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        comprador.sendMessage(GOLD + "Has comprado el item");
        comprador.playSound(comprador.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    private boolean isItemInInventory(Player jugadorVendedor, ItemStack itemAVender, int slotItemVender) {
        var itemAVenderEnInventario = jugadorVendedor.getInventory().getItem(slotItemVender);

        return itemAVenderEnInventario != null && itemAVenderEnInventario.equals(itemAVender);
    }

    @Override
    protected ItemStack buildItemOptional() {
        return ItemUtils.setDisplayname(getState().itemAVender().clone(), GOLD + "" + BOLD + "ITEM A COMPRAR");
    }

    @Override
    public String titulo() {
        return DARK_RED + "" + BOLD + "      OFERTA DE VENTA";
    }

    @Override
    public ItemStack aceptarItem() {
        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(GREEN + "" + BOLD + "COMPRAR")
                .lore(List.of(
                        GOLD + "Aceptar la oferta de " + this.getState().jugadorVendedor().getName(),
                        GOLD + "Por: " + formatPixelcoins(getState().precio())
                ))
                .build();
    }
}
