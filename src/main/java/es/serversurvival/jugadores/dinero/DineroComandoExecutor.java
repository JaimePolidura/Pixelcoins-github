package es.serversurvival.jugadores.dinero;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.Funciones.FORMATEA;

@Command(
        value = "dinero",
        explanation = "Ver tus pixelcoins que tengas en efectivo"
)
public class DineroComandoExecutor extends PixelcoinCommand implements CommandRunnerNonArgs {
    private final JugadoresService jugadoresService;

    public DineroComandoExecutor() {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    @Override
    public void execute(CommandSender commandSender) {
        Player player = (Player) commandSender;
        String nombreJugador = player.getName();

        double patrimonioJugador = Funciones.getPatrimonioJugador(nombreJugador);
        double totalAhorrado = jugadoresService.getByNombre(nombreJugador).getPixelcoins();

        patrimonioJugador = patrimonioJugador - totalAhorrado;

        player.sendMessage(ChatColor.GOLD + "Ahorrado (disponible) : " + ChatColor.GREEN + FORMATEA.format(totalAhorrado) + " PC");
        if(patrimonioJugador != 0){
            player.sendMessage("             ");
            player.sendMessage(ChatColor.GOLD + "Tienes: " + ChatColor.GREEN + FORMATEA.format(patrimonioJugador) + " PC " + ChatColor.GOLD + "en otras partes. /perfil o /estadisticas");
        }
    }
}
