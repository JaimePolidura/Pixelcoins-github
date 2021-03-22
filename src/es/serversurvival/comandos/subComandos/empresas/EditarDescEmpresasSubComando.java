package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.objetos.mySQL.Empresas;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EditarDescEmpresasSubComando extends EmpresasSubCommand {
    private final String SCNombre = "editardescripccion";
    private final String sintaxis = "/empresas editardescripccion <empresa> <nueva desc>";
    private final String ayuda = "Editar la descripcion de tu empresa";

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
        if (args.length < 3) {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        String empresa = args[1];
        String descripcion = "";
        StringBuffer sb = new StringBuffer();
        for (int i = 2; i < args.length; i++) {
            sb.append(args[i] + " ");
        }
        descripcion = sb.toString();

        Empresas empr = new Empresas();
        empr.conectar();
        empr.cambiarDescripciom(empresa, descripcion, p);
        empr.desconectar();
    }
}
