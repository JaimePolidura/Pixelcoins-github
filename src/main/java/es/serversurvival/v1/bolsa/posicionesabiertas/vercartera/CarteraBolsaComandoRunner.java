package es.serversurvival.v1.bolsa.posicionesabiertas.vercartera;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(value = "bolsa cartera", explanation = "Ver todas las posiciones que tienes")
@AllArgsConstructor
public class CarteraBolsaComandoRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, (Class<? extends Menu<?>>) VerBolsaCarteraMenu.class);
    }
}
