package es.serversurvival.jugadores.perfil;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.jaimetruman.menus.MenuService;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "perfil", explanation = "Ver tus estadisticas")
public class PerfilComandoRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    public PerfilComandoRunner(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, new PerfileMenu(sender.getName()));
    }
}
