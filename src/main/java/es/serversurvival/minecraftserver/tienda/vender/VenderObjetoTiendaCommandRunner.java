package es.serversurvival.minecraftserver.tienda.vender;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver.tienda.vertienda.TiendaMenu;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.tienda.ponerventa.PonerVentaTiendaItemMinecraftParametros;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.chat.hover.content.Entity;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "tienda vender",
        args = {"precio"},
        explanation = "Vender item en la tienda con un precio"
)
@AllArgsConstructor
public final class VenderObjetoTiendaCommandRunner implements CommandRunnerArgs<VenderObjetoTiendaComando> {
    private final SyncMenuService syncMenuService;
    private final MenuService menuService;
    private final UseCaseBus useCaseBus;

    @Override
    public void execute(VenderObjetoTiendaComando comando, Player player) {
        ItemStack itemASubirTienda = player.getInventory().getItemInMainHand();

        useCaseBus.handle(PonerVentaTiendaItemMinecraftParametros.builder()
                .item(itemASubirTienda)
                .jugadorId(player.getUniqueId())
                .precio(comando.getPrecio())
                .build());

        syncTiendaMenu(player);

        player.sendMessage(GOLD + "Has subido a la tienda " + itemASubirTienda.getAmount() + " de " + itemASubirTienda.getType()
                + " por " + GREEN + FORMATEA.format(comando.getPrecio()) + " PC " + GOLD + "Para ver la tienda o retirarlo " + AQUA + "/tienda ver");
        player.playSound(player, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
        player.getInventory().clear(player.getInventory().getHeldItemSlot());

        Bukkit.broadcastMessage(GOLD + player.getName() + "ha subido a la tienda " + itemASubirTienda.getAmount() + " de " + itemASubirTienda.getType()
                + " por " + GREEN + FORMATEA.format(comando.getPrecio()) + " PC ");
    }

    private void syncTiendaMenu(Player player) {
        Menu<?> menu = menuService.buildMenu(player, TiendaMenu.class);
        syncMenuService.sync(menu);
        menu.close();
    }
}
