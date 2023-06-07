package es.serversurvival.v2.minecraftserver.bolsa.verposicionescerradas;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command("bolsa posicionescerradas")
@RequiredArgsConstructor
public final class VerPosicionesCerradasCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player player) {
        menuService.open(player, VerPosicionesCerradasMenu.class, VerPosicionesCerradasMenu.Orden.RENTABILIDAD);
    }
}
