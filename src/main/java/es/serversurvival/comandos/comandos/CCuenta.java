package es.serversurvival.comandos.comandos;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.CommandRunner;
import es.serversurvival.comandos.ComandoUtilidades;
import es.serversurvival.mySQL.tablasObjetos.Cuenta;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(name = "cuenta")
public class CCuenta extends ComandoUtilidades implements CommandRunner {

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        cuentasMySQL.conectar();

        Player player = (Player) commandSender;
        Cuenta cuenta = cuentasMySQL.getCuenta(player.getName());
        Jugador jugador = jugadoresMySQL.getJugador(player.getName());

        if(jugador.getNumero_cuenta() == 0){
            jugadoresMySQL.setNumeroCuenta(player.getName(), jugadoresMySQL.generearNumeroCuenta());
        }else if(cuenta == null){
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            player.sendMessage(ChatColor.AQUA + "Tu numero de cuenta: " + ChatColor.BOLD + jugador.getNumero_cuenta());
            player.sendMessage("   ");
            player.sendMessage(ChatColor.AQUA + "Para registrarse: " + ChatColor.BOLD + "http://serversurvival.ddns.net/registrarse");
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        }else{
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            player.sendMessage(ChatColor.AQUA + "Tu numero de cuenta: " + ChatColor.BOLD + jugador.getNumero_cuenta());
            player.sendMessage(ChatColor.AQUA + "Tu contraseña: " + ChatColor.BOLD + cuenta.getPassword());
            player.sendMessage("   ");
            player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "http://serversurvival.ddns.net/perfil");
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        }
        cuentasMySQL.desconectar();
    }
}
