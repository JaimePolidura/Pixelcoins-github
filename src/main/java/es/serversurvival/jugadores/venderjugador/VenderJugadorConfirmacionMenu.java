package es.serversurvival.jugadores.venderjugador;

import es.jaimetruman.ItemBuilder;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.menus2.ConfirmacionMenu;
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
    private final Player jugadorAVender;
    private final Player jugadorVendedor;
    private final ItemStack itemAVender;
    private final int slotItemVender;
    private final double precio;

    @Override
    public void onAceptar(Player player, InventoryClickEvent event) {
        var pagarUseCase = new PagarUseCase();

        pagarUseCase.realizarPago(jugadorAVender.getName(), jugadorVendedor.getName(), precio);

        jugadorAVender.getInventory().addItem(itemAVender);
        jugadorVendedor.getInventory().clear(slotItemVender);

        jugadorVendedor.sendMessage(ChatColor.GOLD + jugadorAVender.getName() + " te ha compradoo " + itemAVender.getType() +
                " al precio de " + ChatColor.GREEN + FORMATEA.format(precio) + " PC");
        jugadorVendedor.playSound(jugadorVendedor.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        jugadorAVender.sendMessage(ChatColor.GOLD + "Has comprado el item");
        jugadorAVender.playSound(jugadorAVender.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        Pixelcoin.publish(new ItemVendidoJugadorEvento(jugadorAVender.getName(), jugadorVendedor.getName(), precio, itemAVender.getType().toString()));
    }

    @Override
    public String titulo() {
        return DARK_RED + "" + BOLD + "   OFERTA DE COMPRA";
    }

    @Override
    public ItemStack aceptarItem() {
        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(ChatColor.GREEN + "" + ChatColor.BOLD + "COMPRAR")
                .lore(List.of(GOLD + "Cancelar la oferta de " + jugadorVendedor.getName()))
                .build();
    }
}
