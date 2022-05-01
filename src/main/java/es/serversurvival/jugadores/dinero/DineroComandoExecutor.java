package es.serversurvival.jugadores.dinero;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "dinero",
        explanation = "Ver tus pixelcoins que tengas en efectivo"
)
public class DineroComandoExecutor extends PixelcoinCommand implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender commandSender) {
        Player player = (Player) commandSender;
        String nombreJugador = player.getName();

        double patrimonioJugador = Funciones.getPatrimonioJugador(nombreJugador);
        double totalAhorrado = AllMySQLTablesInstances.jugadoresMySQL.getJugador(nombreJugador).getPixelcoins();

        patrimonioJugador = patrimonioJugador - totalAhorrado;

        player.sendMessage(ChatColor.GOLD + "Ahorrado (disponible) : " + ChatColor.GREEN + AllMySQLTablesInstances.formatea.format(totalAhorrado) + " PC");
        if(patrimonioJugador != 0){
            player.sendMessage("             ");
            player.sendMessage(ChatColor.GOLD + "Tienes: " + ChatColor.GREEN + AllMySQLTablesInstances.formatea.format(patrimonioJugador) + " PC " + ChatColor.GOLD + "en otras partes. /perfil o /estadisticas");
        }
    }
}
