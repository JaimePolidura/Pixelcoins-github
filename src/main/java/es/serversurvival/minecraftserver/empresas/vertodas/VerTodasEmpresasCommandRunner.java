package es.serversurvival.minecraftserver.empresas.vertodas;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command("empresas vertodas")
@RequiredArgsConstructor
public final class VerTodasEmpresasCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player player) {
        menuService.open(player, TodasEmpresasMenu.class);
    }
}
