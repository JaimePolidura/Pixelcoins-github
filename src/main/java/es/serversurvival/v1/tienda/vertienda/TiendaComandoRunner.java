package es.serversurvival.v1.tienda.vertienda;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "tienda ver",
        explanation = "Ver la tienda de objetos"
)
@AllArgsConstructor
public class TiendaComandoRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, TiendaMenu.class);
    }
}
