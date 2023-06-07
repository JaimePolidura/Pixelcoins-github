package es.serversurvival.minecraftserver.empresas.cerrar;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.configuration.MenuConfiguration;
import es.bukkitbettermenus.modules.confirmation.ConfirmationConfiguration;
import es.bukkitclassmapper._shared.utils.ItemBuilder;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.pixelcoins.empresas.cerrar.CerrarEmpresaParametros;
import es.serversurvival.pixelcoins.empresas.cerrar.CerrarEmpresaUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static org.bukkit.ChatColor.*;

@AllArgsConstructor
public final class CerrarEmpresaConfirmacionMenu extends Menu<Empresa> {
    private final CerrarEmpresaUseCase cerrarEmpresaUseCase;

    @Override
    public int[][] items() {
        return new int[][]{{1, 0, 0, 0, 2}};
    }

    @Override
    public MenuConfiguration configuration() {
        return MenuConfiguration.builder()
                .title(DARK_RED + "" + BOLD + "     Confirmar cerrar")
                .fixedItems()
                .confirmation(ConfirmationConfiguration.builder()
                        .cancel(1, buildItemCancel(), this::onCancel)
                        .accept(2, buildItemAccept(), this::onAccept)
                        .build())
                .build();
    }

    private void onAccept(Player player, InventoryClickEvent event) {
        this.cerrarEmpresaUseCase.cerrar(new CerrarEmpresaParametros(getState().getEmpresaId(), getPlayer().getUniqueId()));

        player.sendMessage(ChatColor.GOLD + "Has cerrado tu empresa, has recibido todas las pixelcoins");
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }

    private void onCancel(Player player, InventoryClickEvent event) {
        player.sendMessage(GOLD + "Has cancelado");
    }

    private ItemStack buildItemAccept() {
        return ItemBuilder.of(Material.GREEN_WOOL).title(GREEN + "" + BOLD + "CERRARA").build();
    }

    private ItemStack buildItemCancel() {
        return ItemBuilder.of(Material.RED_WOOL).title(RED + "" + BOLD + "CANCELAR").build();
    }
}
