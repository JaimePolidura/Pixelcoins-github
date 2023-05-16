package es.serversurvival.v2.minecraftserver.jugadores.dinero;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerNonArgs;
import es.serversurvival.v1._shared.utils.Funciones;
import es.serversurvival.v1.jugadores._shared.application.CalculadorPatrimonio;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        value = "dinero",
        explanation = "Ver tus pixelcoins que tengas en efectivo"
)
@AllArgsConstructor
public class DineroComandoExecutor implements CommandRunnerNonArgs {
    private final CalculadorPatrimonio calculadorPatrimonio;
    private final JugadoresService jugadoresService;

    @Override
    public void execute(CommandSender commandSender) {
        Player player = (Player) commandSender;
        String nombreJugador = player.getName();

        double patrimonioJugador = this.calculadorPatrimonio.calcular(nombreJugador);
        double totalAhorrado = jugadoresService.getByNombre(nombreJugador).getPixelcoins();

        patrimonioJugador = patrimonioJugador - totalAhorrado;

        player.sendMessage(ChatColor.GOLD + "Ahorrado (disponible) : " + ChatColor.GREEN + Funciones.FORMATEA.format(totalAhorrado) + " PC");
        if(patrimonioJugador != 0){
            player.sendMessage("             ");
            player.sendMessage(ChatColor.GOLD + "Tienes: " + ChatColor.GREEN + Funciones.FORMATEA.format(patrimonioJugador) + " PC " + ChatColor.GOLD + "en otras partes. /perfil o /estadisticas");
        }
    }
}
