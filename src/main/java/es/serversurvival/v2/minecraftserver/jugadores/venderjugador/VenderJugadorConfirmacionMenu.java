package es.serversurvival.v2.minecraftserver.jugadores.venderjugador;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.jaime.EventBus;
import es.serversurvival.v1._shared.menus.ConfirmacionMenu;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v2.pixelcoins.jugadores.pagar.HacerPagarParametros;
import es.serversurvival.v2.pixelcoins.jugadores.pagar.PagarUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class VenderJugadorConfirmacionMenu extends ConfirmacionMenu<VenderJugadorConfirmacionMenuState> {
    private final PagarUseCase pagarUseCase;
    private final EventBus eventBus;

    @Override
    public void onAceptar(Player player, InventoryClickEvent event, VenderJugadorConfirmacionMenuState state) {
        var jugadorVendedor = state.jugadorVendedor();
        var jugadorComprador = state.jugadorComprador();
        var itemAVender = state.itemAVender();
        var precio = state.precio();
        var slotItemVender = state.slotItemVender();

        var itemToVenderIsIntVendedorInventory = this.isItemInInventory(jugadorVendedor, itemAVender, slotItemVender);
        if(!itemToVenderIsIntVendedorInventory){
            jugadorVendedor.sendMessage(DARK_RED + "casi maquina");
            jugadorComprador.sendMessage(DARK_RED + "No se ha podido completar el pago, por que el nombreAccionista ha movido el objeto a venderte");
            return;
        }

        pagarUseCase.hacerPago(HacerPagarParametros.of(jugadorComprador.getUniqueId(), jugadorVendedor.getUniqueId(), precio));

        jugadorComprador.getInventory().addItem(itemAVender);
        jugadorVendedor.getInventory().clear(slotItemVender);

        jugadorVendedor.sendMessage(ChatColor.GOLD + jugadorComprador.getName() + " te ha compradoo " + itemAVender.getType() +
                " al precio de " + ChatColor.GREEN + Funciones.FORMATEA.format(precio) + " PC");
        jugadorVendedor.playSound(jugadorVendedor.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        jugadorComprador.sendMessage(ChatColor.GOLD + "Has comprado el item");
        jugadorComprador.playSound(jugadorComprador.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        this.eventBus.publish(new ItemVendidoJugadorEvento(jugadorComprador.getUniqueId(), jugadorVendedor.getUniqueId(), precio, itemAVender.getType().toString()));
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
                        GOLD + "Aceptar la oferta de " + this.getState().jugadorVendedor().getName(),
                        GOLD + "Precio: " + GREEN + Funciones.FORMATEA.format(this.getState().precio()) + "PC"
                ))
                .build();
    }
}
