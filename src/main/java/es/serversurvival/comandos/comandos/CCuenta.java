package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.mySQL.Cuentas;
import es.serversurvival.mySQL.NumeroCuentas;
import es.serversurvival.mySQL.tablasObjetos.Cuenta;
import es.serversurvival.mySQL.tablasObjetos.NumeroCuenta;
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
        NumeroCuenta numeroCuenta = numeroCuentasMySQL.getNumeroCuenta(player.getName());

        if(numeroCuenta == null ){
            numeroCuentasMySQL.nuevoNumeroCuenta(player.getName());
        }else if(cuenta == null){
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            player.sendMessage(ChatColor.AQUA + "Tu numero de cuenta: " + ChatColor.BOLD + numeroCuenta.getNumero());
            player.sendMessage("   ");
            player.sendMessage(ChatColor.AQUA + "Para registrarse: " + ChatColor.BOLD + "http://serversurvival2.ddns.net/registrarse");
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        }else{
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            player.sendMessage(ChatColor.AQUA + "Tu numero de cuenta: " + ChatColor.BOLD +numeroCuenta.getNumero());
            player.sendMessage(ChatColor.AQUA + "Tu contraseña: " + ChatColor.BOLD + cuenta.getPassword());
            player.sendMessage("   ");
            player.sendMessage(ChatColor.AQUA + "" + ChatColor.BOLD + "http://serversurvival2.ddns.net/perfil");
            player.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        }
        cuentasMySQL.desconectar();
    }
}
