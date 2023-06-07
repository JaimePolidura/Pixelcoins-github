package es.serversurvival.minecraftserver.empresas.mercado;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command("empresas mercado")
@RequiredArgsConstructor
public final class VerMercadoAccionesEmpresasCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player player) {
        menuService.open(player, MercadoAccionesEmpresasMenu.class);
    }
}
