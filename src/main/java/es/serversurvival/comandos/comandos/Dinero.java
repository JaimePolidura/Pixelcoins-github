package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.objetos.mySQL.Jugadores;
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
        Jugadores jugadoresMySQL = new Jugadores();

        jugadoresMySQL.conectar();
        player.sendMessage(ChatColor.GOLD + "Tienes: " + ChatColor.GREEN + formatea.format(jugadoresMySQL.getDinero(player.getName())) + " PC");
        jugadoresMySQL.desconectar();
    }
}
