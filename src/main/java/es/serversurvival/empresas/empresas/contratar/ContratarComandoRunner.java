package es.serversurvival.empresas.empresas.contratar;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.jaimetruman.menus.MenuService;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empleados._shared.domain.TipoSueldo;
import es.serversurvival.empresas.empleados.contratar.ContratarConfirmacionMenu;
import main.ValidationResult;
import main.ValidatorService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.validaciones.Validaciones.*;

@Command(
        value = "empresas contratar",
        args = {"jugador", "empresa", "sueldo", "tipoSueldo", "[cargo]¡trabajador!"},
        explanation = "Contratar a un jugador en tu empresa con un sueldo, <tipoSueldo> frequencia de pago: d (cada dia), s (cada semana), " +
                "2s (cada 2 semanas), m (cada mes)"
)
public class ContratarComandoRunner implements CommandRunnerArgs<ContratarComando> {
    private final MenuService menuService;

    public ContratarComandoRunner() {
        this.menuService = DependecyContainer.get(MenuService.class);
    }

    @Override
    public void execute(ContratarComando contratarComando, CommandSender player) {
        String destinoJugadorNOmbre = contratarComando.getJugador();
        String empresa = contratarComando.getEmpresa();
        double sueldo = contratarComando.getSueldo();
        String tipoSueldoString = contratarComando.getTipoSueldo();
        String cargo = contratarComando.getCargo();

        ValidationResult result = ValidatorService
                .startValidating(sueldo, NaturalNumber)
                .and(destinoJugadorNOmbre, JugadorOnline, NoLeHanEnviadoSolicitud, NotEqualsIgnoreCase.of(player.getName(),
                        "No te puedes contratar a ti mismo"))
                .and(TipoSueldo.codigoCorrecto(tipoSueldoString), True.of("El tipo de sueldo solo puede ser d: cdda dia, s: " +
                        "cada semana, 2s: cada dos semanas, m: cada mes"))
                .and(empresa, OwnerDeEmpresa.of(player.getName()))
                .validateAll();

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }


        TipoSueldo tipoSueldo = TipoSueldo.ofCodigo(tipoSueldoString);

        this.menuService.open((Player) player, new ContratarConfirmacionMenu(player.getName(), destinoJugadorNOmbre, empresa, cargo, sueldo, tipoSueldo));
    }
}
