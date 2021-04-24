package es.serversurvival.legacy.comandos.comandos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.legacy.comandos.PixelcoinCommand;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.tablasObjetos.Cuenta;
import es.serversurvival.legacy.mySQL.tablasObjetos.Jugador;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "cuenta")
public class CCuenta extends PixelcoinCommand implements CommandRunner {

    @Override
    public void execute(CommandSender commandSender, java.lang.String[] strings) {

        Player player = (Player) commandSender;
        Cuenta cuenta = AllMySQLTablesInstances.cuentasMySQL.getCuenta(player.getName());
        Jugador jugador = AllMySQLTablesInstances.jugadoresMySQL.getJugador(player.getName());

        if(jugador.getNumero_cuenta() == 0) {
            AllMySQLTablesInstances.jugadoresMySQL.setNumeroCuenta(player.getName(), AllMySQLTablesInstances.jugadoresMySQL.generearNumeroCuenta());
            return;
        }

        player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        player.sendMessage("   ");
        player.sendMessage(ChatColor.AQUA + "Tu numero de cuenta: " + ChatColor.BOLD + jugador.getNumero_cuenta());
        if(cuenta == null){
            player.sendMessage(ChatColor.AQUA + "Para registrarse: " + ChatColor.BOLD + "http://serversurvival.ddns.net/registrarse");
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        }else{
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            player.sendMessage(ChatColor.AQUA + "Tu numero de cuenta: " + ChatColor.BOLD + jugador.getNumero_cuenta());
        }
        player.sendMessage("   ");
        player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "http://serversurvival.ddns.net/perfil");
        player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

    }
}
