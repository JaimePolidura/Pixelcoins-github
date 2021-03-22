package es.serversurvival.comandos.subComandos.deudas;

import es.serversurvival.objetos.mySQL.Deudas;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PagarDeudaDeudasSubComando extends DeudasSubCommand {
    private String scnombre = "pagar";
    private String sintaxis = "/deudas pagar <id>";
    private String ayuda = "Pagar toda la deuda, la id se ve en /deudas ver";

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
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis + " la id se ve en /deudas");
            return;
        }
        int ideuda;
        try {
            ideuda = Integer.parseInt(args[1]);
        } catch (Exception e) {
            p.sendMessage(ChatColor.DARK_RED + "A ser posible mete numeros no letras, las id se ven el comando /deudas");
            return;
        }
        Deudas d = new Deudas();
        d.conectar();
        d.pagarDeuda(p, ideuda);
        d.desconectar();
        return;
    }
}
