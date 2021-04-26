package es.serversurvival.nfs.jugadores.dinero;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import es.serversurvival.nfs.utils.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("dinero")
public class DineroComando extends PixelcoinCommand implements CommandRunner{

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        Player player = (Player) commandSender;
        String nombreJugador = player.getName();

        double patrimonioJugador = Funciones.getPatrimonioJugador(nombreJugador);
        double totalAhorrado = jugadoresMySQL.getJugador(nombreJugador).getPixelcoins();

        patrimonioJugador = patrimonioJugador - totalAhorrado;

        player.sendMessage(ChatColor.GOLD + "Ahorrado (disponible) : " + ChatColor.GREEN + formatea.format(totalAhorrado) + " PC");
        if(patrimonioJugador != 0){
            player.sendMessage("             ");
            player.sendMessage(ChatColor.GOLD + "Tienes: " + ChatColor.GREEN + formatea.format(patrimonioJugador) + " PC " + ChatColor.GOLD + "en otras partes. /perfil o /estadisticas");
        }
    }
}
