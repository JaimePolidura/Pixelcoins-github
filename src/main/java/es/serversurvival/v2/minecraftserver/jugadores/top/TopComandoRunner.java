package es.serversurvival.v2.minecraftserver.jugadores.top;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "top",
        explanation = "Ver el top ricos, pobres, mejores vendedores etc"
)
@AllArgsConstructor
public class TopComandoRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player sender) {
        this.menuService.open(sender, TopMenu.class);
    }
}
