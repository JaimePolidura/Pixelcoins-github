package es.serversurvival.bolsa.posicionesabiertas.vercartera;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "bolsa cartera", explanation = "Ver todas las posiciones que tienes")
public class CarteraBolsaComandoRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    public CarteraBolsaComandoRunner() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, new VerBolsaCarteraMenu(sender.getName()));
    }
}
