package es.serversurvival.empresas.ofertasaccionesserver.verofertasaccioneserver;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.bukkitclassmapper.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "empresas mercado",
        explanation = "Ver todas las ofertas de venta de cantidad de empresas del servidor"
)
public class MercadoEmpresas implements CommandRunnerNonArgs {
    private final MenuService menuService;

    public MercadoEmpresas() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, new VerOfertasAccionesServerMenu((Player) sender));
    }
}
