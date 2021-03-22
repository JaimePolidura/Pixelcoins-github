package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.objetos.mySQL.Jugadores;
import org.bukkit.entity.Player;

public class EstadisticasComando extends Comando {
    private final String cnombre = "estadisticas";
    private final String sintaxis = "/estadisticas";
    private final String ayuda = "Ver tus estadisticas: /ayuda estadisticas";

    public String getCNombre() {
        return cnombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player player, String[] args) {
        Jugadores j = new Jugadores();
        j.conectar();
        j.mostarEstadisticas(player);
        j.desconectar();
    }
}
