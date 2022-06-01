package es.serversurvival.empresas.empresas.vender;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;

@Command(
        value = "empresas vender",
        args = {"empresa", "jugador", "precio"},
        explanation = "Vender tu empresa a otro jugador por pixelcoins"
)
public class VenderEmpresaComandoRunner implements CommandRunnerArgs<VenderEmpresaComando> {
    private final MenuService menuService;

    public VenderEmpresaComandoRunner() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(VenderEmpresaComando comando, CommandSender player) {
        String empresa = comando.getEmpresa();
        String jugador = comando.getJugador();
        double preico = comando.getPrecio();

        ValidationResult result = ValidatorService
                .startValidating(jugador, JugadorOnline, NotEqualsIgnoreCase.of(player.getName(), "No te lo puedes vender a ti mismo"))
                .and(preico, PositiveNumber, SuficientesPixelcoins.of(jugador, "No tiene tantas pixelcoins como crees xdd"))
                .and(empresa, OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        this.menuService.open((Player) player, new VenderEmpresaConfirmacionMenu(new VenderEmpresaUseCase(), player.getName(), jugador, empresa, preico));
    }
}
