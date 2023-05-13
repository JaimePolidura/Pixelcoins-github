package es.serversurvival.jugadores.perfil;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "perfil", explanation = "Ver tus estadisticas")
@AllArgsConstructor
public class PerfilComandoRunner implements CommandRunnerNonArgs {
    private final JugadoresService jugadoresService;
    private final MenuService menuService;

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, PerfilMenu.class, this.jugadoresService.getByNombre(sender.getName()));
    }
}
