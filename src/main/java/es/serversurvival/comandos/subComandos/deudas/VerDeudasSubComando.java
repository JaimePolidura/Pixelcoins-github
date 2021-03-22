package es.serversurvival.comandos.subComandos.deudas;

import es.serversurvival.objetos.mySQL.Deudas;
import org.bukkit.entity.Player;

public class VerDeudasSubComando extends DeudasSubCommand {
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
        Deudas d = new Deudas();
        d.conectar();
        d.mostarDeudas(p);
        d.desconectar();
    }
}
