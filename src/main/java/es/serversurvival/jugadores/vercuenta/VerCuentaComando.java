package es.serversurvival.jugadores.vercuenta;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.shared.comandos.PixelcoinCommand;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command("cuenta")
public class VerCuentaComando extends PixelcoinCommand implements CommandRunner {
    @Override
    public void execute(CommandSender commandSender, java.lang.String[] strings) {
        Player player = (Player) commandSender;
        Jugador jugador = AllMySQLTablesInstances.jugadoresMySQL.getJugador(player.getName());

        player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        player.sendMessage("   ");
        player.sendMessage(ChatColor.AQUA + "Tu numero de cuenta: " + ChatColor.BOLD + jugador.getNumero_cuenta());
        player.sendMessage(ChatColor.AQUA + "Para registrarse: " + ChatColor.BOLD + "http://serversurvival.ddns.net/registrarse");
        player.sendMessage(ChatColor.AQUA + "Para iniciarSesion: " + ChatColor.BOLD + "http://serversurvival.ddns.net/login");
        player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        player.sendMessage("   ");
        player.sendMessage(ChatColor.AQUA + "Para ver tu perfil: " + ChatColor.BOLD + "http://serversurvival.ddns.net/perfil");
        player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
    }
}
