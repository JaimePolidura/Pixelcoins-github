package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.objetos.mySQL.Cuentas;
import es.serversurvival.objetos.mySQL.tablasObjetos.Cuenta;
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
        Cuentas cuentas = new Cuentas();
        cuentas.conectar();

        Cuenta cuenta = cuentas.getCuenta(player.getName());

        if(cuenta == null){
            cuentas.nuevaIDCuenta(player.getName());
        }else if(cuenta.getContra() == null){
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.DARK_AQUA + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.AQUA + "Tu numero de cuenta: " + cuenta.getId());
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.AQUA + "Para iniciar sesion: <link>");
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.DARK_AQUA + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        }else{
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.DARK_AQUA + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.AQUA + "Tu numero de cuenta: " + cuenta.getId());
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.AQUA + "Tu email: " + cuenta.getEmail());
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.AQUA + "Tu contraseña: " +cuenta.getContra());
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.AQUA + "  (Para ver <link>)");
            player.sendMessage(ChatColor.BOLD + "" + ChatColor.DARK_AQUA + "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        }
        cuentas.desconectar();
    }
}
