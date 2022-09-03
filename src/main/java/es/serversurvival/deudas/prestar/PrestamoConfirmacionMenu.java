package es.serversurvival.deudas.prestar;

import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.dependencyinjector.annotations.Component;
import es.serversurvival._shared.menus.ConfirmacionMenu;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@AllArgsConstructor
@Component
public final class PrestamoConfirmacionMenu extends ConfirmacionMenu {
    private final String destinatarioJugadorNombre;
    private final String enviadorJugadorNombre;
    private final double pixelcoins;
    private final int dias;
    private final int interes;

    @Override
    public void onAceptar(Player player, InventoryClickEvent event) {
        Player enviadorPlayer = Bukkit.getPlayer(enviadorJugadorNombre);
        Player destinatarioPlayer = Bukkit.getPlayer(destinatarioJugadorNombre);

        PrestarUseCase.INSTANCE.prestar(enviadorJugadorNombre, destinatarioJugadorNombre, pixelcoins, interes, dias);

        destinatarioPlayer.sendMessage(GOLD + "Has aceptado la solicitud de: " + GREEN + FORMATEA.format(pixelcoins) + " PC " +
                GOLD + "con un interes del: " + GREEN + interes + GOLD + " a " + GREEN + dias + GOLD + " dias");
        if (enviadorPlayer != null) {
            enviadorPlayer.sendMessage(GOLD + enviadorPlayer.getName() + " Te ha aceptado la solicitud de deuda");
            enviadorPlayer.playSound(enviadorPlayer.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        }
    }

    @Override
    public ItemStack aceptarItem() {
        String descAceptarString = GOLD + "Prestamo de " + enviadorJugadorNombre + " de " + GREEN + FORMATEA.format(pixelcoins) +
                GOLD + " a " + dias + " dias  con un interes del " + interes + "% (" + GREEN +
                FORMATEA.format(aumentarPorcentaje(pixelcoins, interes)) + " PC" + GOLD + ")";
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
