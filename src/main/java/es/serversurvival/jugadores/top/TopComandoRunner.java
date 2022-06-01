package es.serversurvival.jugadores.top;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@Command(
        value = "top",
        explanation = "Ver el top ricos, pobres, mejores vendedores etc"
)
public class TopComandoRunner implements CommandRunnerNonArgs {
    public final static String titulo = ChatColor.DARK_RED + "" + ChatColor.BOLD + "              TOP";
    private final MenuService menuService;

    public TopComandoRunner() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, new TopMenu());
    }
}
