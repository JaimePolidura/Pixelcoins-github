package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.objetos.mySQL.Empresas;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LogotipoEmpresasSubComando extends EmpresasSubCommand {
    private final String SCNombre = "logotipo";
    private final String sintaxis = "/empresas logotipo <empresa>";
    private final String ayuda = "Cambiar el logotipo de tu empresa al objeto que selecciones en la mano el ejecutar este comando";

    public String getSCNombre() {
        return SCNombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player p, String[] args) {
        if (args.length == 2) {
            String tipo2 = p.getInventory().getItemInMainHand().getType().toString();
            if (!tipo2.equalsIgnoreCase("AIR")) {
                Empresas empr = new Empresas();
                empr.conectar();
                empr.cambiarIcono(args[1], p, tipo2);
                empr.desconectar();
            } else {
                p.sendMessage(ChatColor.DARK_RED + "Tienes que seleccionar un objeto en la mano");
            }
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /logotipo <nombre empresa>");
        }
    }
}
