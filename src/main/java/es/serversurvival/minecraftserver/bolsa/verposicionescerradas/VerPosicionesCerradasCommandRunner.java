package es.serversurvival.minecraftserver.bolsa.verposicionescerradas;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "bolsa posicionescerradas",
        explanation = "Ver todas tus operaciones pasadas en bolsa"
)
@RequiredArgsConstructor
public final class VerPosicionesCerradasCommandRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player player) {
        menuService.open(player, VerPosicionesCerradasMenu.class, VerPosicionesCerradasMenu.Orden.RENTABILIDAD);
    }
}
