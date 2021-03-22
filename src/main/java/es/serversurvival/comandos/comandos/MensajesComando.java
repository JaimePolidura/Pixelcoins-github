package es.serversurvival.comandos.comandos;

import es.serversurvival.comandos.Comando;
import es.serversurvival.objetos.mySQL.Mensajes;
import org.bukkit.entity.Player;

public class MensajesComando extends Comando {
    private final String CNombre = "mensajes";
    private final String sintaxis = "/mensajes";
    private final String ayuda = "ver todos los mensajes pendientes";

    public String getCNombre() {
        return CNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player p, String[] args) {
        Mensajes m = new Mensajes();
        m.conectar();
        m.mostrarMensajes(p);
        m.desconectar();
    }
}
