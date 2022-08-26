package es.serversurvival.bolsa.posicionesabiertas.vercartera;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.bukkitclassmapper.menus.MenuService;
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
