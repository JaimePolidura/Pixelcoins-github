package es.serversurvival.v2.minecraftserver.jugadores.perfil;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "perfil", explanation = "Ver tus estadisticas")
@AllArgsConstructor
public class PerfilComandoRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(Player sender) {
        this.menuService.open(sender, PerfilMenu.class);
    }
}
