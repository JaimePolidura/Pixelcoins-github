package es.serversurvival.comandos.subComandos.deudas;

import es.serversurvival.objetos.mySQL.Deudas;
import org.bukkit.entity.Player;

public class VerDeudas extends DeudasSubCommand {
    private final String scnombre = "ver";
    private final String sintaxis = "/deudas ver";
    private final String ayuda = "Ver todas las pixelcoins que debes y que te deben";

    public String getSCNombre() {
        return scnombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player p, String[] args) {
        deudasMySQL.conectar();
        deudasMySQL.mostarDeudas(p);
        deudasMySQL.desconectar();
    }
}