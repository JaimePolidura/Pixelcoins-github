package es.serversurvival.v1.deudas.prestar;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.v1._shared.menus.ConfirmacionMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

import static es.serversurvival.v1._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class PrestamoConfirmacionMenu extends ConfirmacionMenu<PrestamoConfirmacionMenuState> {
    private final PrestarUseCase prestarUseCase;

    @Override
    public void onAceptar(Player destinatario, InventoryClickEvent event, PrestamoConfirmacionMenuState confirmacion) {
        Player enviadorPlayer = Bukkit.getPlayer(confirmacion.enviadorJugadorNombre());
        Player destinatarioPlayer = Bukkit.getPlayer(confirmacion.destinatarioJugadorNombre());

        this.prestarUseCase.prestar(confirmacion.enviadorJugadorNombre(), confirmacion.destinatarioJugadorNombre(),
                confirmacion.pixelcoins(), confirmacion.interes(), confirmacion.dias());

        destinatarioPlayer.sendMessage(GOLD + "Has aceptado la solicitud de: " + GREEN + FORMATEA.format(confirmacion.pixelcoins()) + " PC " +
                GOLD + "con un interes del: " + GREEN + confirmacion.interes() + GOLD + " a " + GREEN + confirmacion.dias() + GOLD + " dias");
        if (enviadorPlayer != null) {
            enviadorPlayer.sendMessage(GOLD + enviadorPlayer.getName() + " Te ha aceptado la solicitud de deuda");
            enviadorPlayer.playSound(enviadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        }
    }

    @Override
    public ItemStack aceptarItem() {
        String descAceptarString = GOLD + "Prestamo de " + getState().enviadorJugadorNombre() + " de " + GREEN + FORMATEA.format(getState().pixelcoins()) +
                GOLD + " a " + getState().dias() + " dias  con un interes del " + getState().interes() + "% (" + GREEN +
                FORMATEA.format(aumentarPorcentaje(getState().pixelcoins(), getState().interes())) + " PC" + GOLD + ")";
        List<String> loreAceptar = dividirDesc(descAceptarString, 40).stream()
                .map(line -> GOLD + line)
                .collect(Collectors.toList());

        return ItemBuilder.of(Material.GREEN_WOOL)
                .title(GREEN + "" + BOLD + "ACEPTAR")
                .lore(loreAceptar)
                .build();
    }

    @Override
    public String titulo() {
        return DARK_RED + "" + BOLD + "    Solicitud Prestamo";
    }
}
