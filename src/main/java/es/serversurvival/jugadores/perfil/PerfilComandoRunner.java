package es.serversurvival.jugadores.perfil;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "perfil", explanation = "Ver tus estadisticas")
public class PerfilComandoRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    public PerfilComandoRunner() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(CommandSender sender) {
        PerfilMenu menu = new PerfilMenu(sender.getName());
        this.menuService.open((Player) sender, menu);
    }
}
