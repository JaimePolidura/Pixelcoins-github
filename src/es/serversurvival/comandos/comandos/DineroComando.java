package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.objetos.mySQL.Jugador;
import org.bukkit.entity.Player;

public class DineroComando extends Comando {
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
        Jugador j = new Jugador();
        j.conectar();
        j.mostarPixelcoin(player);
        j.desconectar();
    }
}
