package es.serversurvival.empresas.empresas.contratar;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.jaime.javaddd.domain.exceptions.CannotBeYourself;
import es.jaime.javaddd.domain.exceptions.IllegalQuantity;
import es.serversurvival.empresas.empleados._shared.domain.TipoSueldo;
import es.serversurvival.empresas.empleados.contratar.ContratarConfirmacionMenu;
import es.serversurvival.empresas.empleados.contratar.ContratarConfirmacionMenuState;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas contratar",
        args = {"nombreAccionista", "empresa", "sueldo", "tipoSueldo", "[cargo]¡trabajador!"},
        explanation = "Contratar a un nombreAccionista en tu empresa con un sueldo, <tipoSueldo> frequencia de pago: d (cada dia), s (cada semana), " +
                "2s (cada 2 semanas), m (cada mes)"
)
@AllArgsConstructor
public class ContratarComandoRunner implements CommandRunnerArgs<ContratarComando> {
    private final MenuService menuService;

    @Override
    public void execute(ContratarComando contratarComando, CommandSender player) {
        String destinoJugadorNOmbre = contratarComando.getJugador();
        String empresa = contratarComando.getEmpresa();
        double sueldo = contratarComando.getSueldo();
        String tipoSueldoString = contratarComando.getTipoSueldo();
        TipoSueldo tipoSueldo = TipoSueldo.ofCodigo(tipoSueldoString);
        String cargo = contratarComando.getCargo();

        if(destinoJugadorNOmbre.equals(player.getName()))
            throw new CannotBeYourself("Tu eres tonto o q te pasa");
        if(sueldo <= 0)
            throw new IllegalQuantity("El sueldo debe de ser un numero positivo y mayor que 0");

        this.menuService.open(Bukkit.getPlayer(destinoJugadorNOmbre), ContratarConfirmacionMenu.class, new ContratarConfirmacionMenuState(
                player.getName(), destinoJugadorNOmbre, empresa, cargo, sueldo, tipoSueldo
        ));

        player.sendMessage(GOLD + "Has enviado la solicitud");
    }
}
