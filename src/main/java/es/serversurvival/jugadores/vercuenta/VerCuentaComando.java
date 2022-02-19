package es.serversurvival.jugadores.vercuenta;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.jaimetruman.commands.CommandRunnerNonArgs;
import es.serversurvival.jugadores._shared.mySQL.Jugador;
import es.serversurvival._shared.comandos.PixelcoinCommand;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("cuenta")
public class VerCuentaComando extends PixelcoinCommand implements CommandRunnerNonArgs {
    @Override
    public void execute(CommandSender sender) {
        Player player = (Player) sender;
        Jugador jugador = AllMySQLTablesInstances.jugadoresMySQL.getJugador(player.getName());

        player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        player.sendMessage("   ");
        player.sendMessage(ChatColor.AQUA + "Tu numero de cuenta: " + ChatColor.BOLD + jugador.getNumero_cuenta());
        player.sendMessage(ChatColor.AQUA + "Para registrarse: " + ChatColor.BOLD + "http://serversurvival.ddns.net/registrarse");
        player.sendMessage(ChatColor.AQUA + "Para iniciarSesion: " + ChatColor.BOLD + "http://serversurvival.ddns.net/login");
        player.sendMessage("   ");
        player.sendMessage(ChatColor.AQUA + "Para ver tu perfil: " + ChatColor.BOLD + "http://serversurvival.ddns.net/perfil");
        player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
    }
}
