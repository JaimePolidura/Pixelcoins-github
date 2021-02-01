package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.mySQL.tablasObjetos.Cuenta;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CCuenta extends Comando {
    private final String CNombre = "cuenta";
    private final String sintaxis = "/cuenta";
    private final String ayuda = "ver todas tu numero de cuenta para iniciar sesion";

    @Override
    public String getCNombre() {
        return CNombre;
    }

    @Override
    public String getSintaxis() {
        return sintaxis;
    }

    @Override
    public String getAyuda() {
        return ayuda;
    }

    @Override
    public void execute(Player player, String[] args) {
        cuentasMySQL.conectar();

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
