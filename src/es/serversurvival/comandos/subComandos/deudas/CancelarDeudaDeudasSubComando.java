package es.serversurvival.comandos.subComandos.deudas;

import es.serversurvival.objetos.mySQL.Deudas;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CancelarDeudaDeudasSubComando extends DeudasSubCommand {
    private final String scnombre = "cancelar";
    private final String sintaxis = "/deudas cancelar <id>";
    private final String ayuda = "cacelar toda la deuda a un jugador, la id se ve en /deudas ver";

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
        if (args.length != 2) {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis + " , la id se ve en el comando /deudas");
            return;
        }
        int iddeuda;
        try {
            iddeuda = Integer.parseInt(args[1]);
        } catch (Exception e) {
            p.sendMessage(ChatColor.DARK_RED + "La id debe de ser un numero no texto, la id se puede ver en el comando: /deudas");
            return;
        }
        Deudas d = new Deudas();
        d.conectar();
        d.cancelarDeuda(p, iddeuda);
        d.desconectar();
    }
}
