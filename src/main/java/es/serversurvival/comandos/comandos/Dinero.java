package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.mySQL.*;
import es.serversurvival.util.Funciones;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Dinero extends Comando {
    private final String CNombre = "dinero";
    private final String sintaxis = "/dinero";
    private final String ayuda = "Ver todas las pixelcoins que tienes";

    public String getCNombre() {
        return CNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        String nombreJugador = player.getName();

        MySQL.conectar();
        double patrimonioJugador = Funciones.getPatrimonioJugador(nombreJugador);
        double totalAhorrado = jugadoresMySQL.getJugador(nombreJugador).getPixelcoins();
        MySQL.desconectar();

        patrimonioJugador = patrimonioJugador - totalAhorrado;

        player.sendMessage(ChatColor.GOLD + "Ahorrado (disponible) : " + ChatColor.GREEN + formatea.format(totalAhorrado) + " PC");
        if(patrimonioJugador != 0){
            player.sendMessage("             ");
            player.sendMessage(ChatColor.GOLD + "Tienes: " + ChatColor.GREEN + formatea.format(patrimonioJugador) + " PC " + ChatColor.GOLD + "en otras partes. /perfil o /estadisticas");
        }
    }
}
