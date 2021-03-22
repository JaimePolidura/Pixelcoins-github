package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.objetos.mySQL.Cuentas;
import es.serversurvival.objetos.mySQL.NumeroCuentas;
import es.serversurvival.objetos.mySQL.tablasObjetos.Cuenta;
import es.serversurvival.objetos.mySQL.tablasObjetos.NumeroCuenta;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CuentaComando extends Comando {
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
        NumeroCuentas numeroCuentas = new NumeroCuentas();
        Cuentas cuentas = new Cuentas();
        cuentas.conectar();

        Cuenta cuenta = cuentas.getCuenta(player.getName());
        NumeroCuenta numeroCuenta = numeroCuentas.getNumeroCuenta(player.getName());

        if(numeroCuenta == null ){
            numeroCuentas.nuevoNumeroCuenta(player.getName());
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
        cuentas.desconectar();
    }
}
