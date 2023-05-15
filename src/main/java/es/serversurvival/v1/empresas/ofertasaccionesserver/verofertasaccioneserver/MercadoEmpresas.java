package es.serversurvival.v1.empresas.ofertasaccionesserver.verofertasaccioneserver;

import es.bukkitbettermenus.Menu;
import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "empresas mercado",
        explanation = "Ver todas las ofertas de venta de cantidad de empresas del servidor"
)
@AllArgsConstructor
public class MercadoEmpresas implements CommandRunnerNonArgs {
    private final MenuService menuService;

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, (Class<? extends Menu<?>>) VerOfertasAccionesServerMenu.class);
    }
}
