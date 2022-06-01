package es.serversurvival.tienda.vertienda;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "tienda ver",
        explanation = "Ver la tienda de objetos"
)
public class TiendaComandoRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    public TiendaComandoRunner() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(CommandSender p) {
        this.menuService.open((Player) p, new TiendaMenu(p.getName()));
    }
}
