package es.serversurvival.jugadores.top;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.bukkitclassmapper.menus.MenuService;
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
