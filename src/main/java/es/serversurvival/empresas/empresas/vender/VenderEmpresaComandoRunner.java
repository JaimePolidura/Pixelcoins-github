package es.serversurvival.empresas.empresas.vender;

import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.exceptions.NotEnoughPixelcoins;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas vender",
        args = {"empresa", "jugador", "precio"},
        explanation = "Vender tu empresa a otro jugador por pixelcoins"
)
public class VenderEmpresaComandoRunner implements CommandRunnerArgs<VenderEmpresaComando> {
    private final MenuService menuService;
    private final JugadoresService jugadoresService;

    public VenderEmpresaComandoRunner() {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(VenderEmpresaComando comando, CommandSender player) {
        String empresa = comando.getEmpresa();
        String jugadorAVender = comando.getJugador();
        double precio = comando.getPrecio();

        if(precio <= 0)
            throw new IllegalQuantity("El precio tiene que ser un numero positivo");
        if(this.jugadoresService.getByNombre(jugadorAVender).getPixelcoins() < precio)
            throw new NotEnoughPixelcoins("No tiene tantas pixelcoins como crees xdd");

        ValidationResult result = ValidatorService
                .startValidating(jugadorAVender, JugadorOnline, NotEqualsIgnoreCase.of(player.getName(), "No te lo puedes vender a ti mismo"))
                .and(empresa, OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(DARK_RED + result.getMessage());
            return;
        }

        this.menuService.open(Bukkit.getPlayer(jugadorAVender),
                new VenderEmpresaConfirmacionMenu(new VenderEmpresaUseCase(), player.getName(), jugadorAVender, empresa, precio)
        );

        player.sendMessage(GOLD + "Has enviado la soilcitud");
    }
}
