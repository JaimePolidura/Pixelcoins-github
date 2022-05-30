package es.serversurvival.bolsa.activosinfo.vervalores;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "bolsa valores", explanation = "Ver un ejemplo de valores que puedes invertir")
public class ValoresBolsaComandoRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    public ValoresBolsaComandoRunner() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, new ElegirInversionMenu());
    }
}
