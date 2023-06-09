package es.serversurvival.minecraftserver.tienda.vender;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.minecraftserver.tienda.vertienda.TiendaMenu;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.tienda.ponerventa.PonerVentaTiendaItemMinecraftParametros;
import es.serversurvival.pixelcoins.tienda.ponerventa.PonerVentaTiendaItemMinecraftUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

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
        useCaseBus.handle(PonerVentaTiendaItemMinecraftParametros.builder()
                .item(player.getInventory().getItemInMainHand())
                .jugadorId(player.getUniqueId())
                .precio(comando.getPrecio())
                .build());

        syncMenuService.sync(menuService.buildMenu(player, TiendaMenu.class));
    }
}
