package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.objetos.mySQL.Empresas;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EditarNombreEmpresasSubComando extends EmpresasSubCommand {
    private final String SCNOmbre = "editarnombre";
    private final String sintaxis = "/empresas editarnombre <empresa> <nuevo nombre>";
    private final String ayuda = "cambiar el nombre de una empresa tuya";

    public String getSCNombre() {
        return SCNOmbre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player p, String[] args) {
        if (args.length != 3) {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        String nuevoNombre = args[2];
        if (nuevoNombre.toCharArray().length > Empresas.CrearEmpresaNombreLonMax) {
            p.sendMessage(ChatColor.DARK_RED + "La longitud maxima del nombre es " + Empresas.CrearEmpresaNombreLonMax);
            return;
        }
        Empresas empr = new Empresas();
        empr.conectar();
        empr.cambiarNombre(p, args[1], nuevoNombre);
        empr.desconectar();
    }
}