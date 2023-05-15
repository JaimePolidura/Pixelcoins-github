package es.serversurvival.v1.bolsa.activosinfo.vervalores;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.v1.bolsa.activosinfo._shared.application.ActivosInfoService;
import es.serversurvival.v1.bolsa.posicionesabiertas.comprarlargo.ComprarLargoUseCase;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import lombok.AllArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.ExecutorService;

@Command(value = "bolsa valores", explanation = "Ver un ejemplo de valores que puedes invertir")
@AllArgsConstructor
public class ValoresBolsaComandoRunner implements CommandRunnerNonArgs {
    private final ComprarLargoUseCase comprarLargoUseCase;
    private final ActivosInfoService activosInfoService;
    private final JugadoresService jugadoresService;
    private final ExecutorService executor;
    private final MenuService menuService;

    @Override
    public void execute(CommandSender sender) {
        this.menuService.open((Player) sender, new ElegirInversionMenu(
                comprarLargoUseCase, activosInfoService, jugadoresService, executor, menuService
        ));
    }
}
