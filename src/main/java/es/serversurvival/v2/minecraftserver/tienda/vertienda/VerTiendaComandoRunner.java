package es.serversurvival.v2.minecraftserver.tienda.vertienda;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;

@Command(
        value = "tienda ver",
        explanation = "Ver la tienda de objetos"
)
@AllArgsConstructor
public final class VerTiendaComandoRunner implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(CommandSender sender) {
    }
}
