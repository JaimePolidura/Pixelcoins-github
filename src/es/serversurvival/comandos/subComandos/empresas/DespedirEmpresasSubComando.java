package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.objetos.mySQL.Empleados;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DespedirEmpresasSubComando extends EmpresasSubCommand {
    private final String SCNombre = "despedir";
    private final String sintaxis = "/empresas despedir <empresa> <jugador> <razon>";
    private final String ayuda = "despedir a un jugador de tu propia empresa";

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
        if (args.length != 4) {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /despedir <empresa> <nombreJugador> <razon>");
            return;
        }
        if (args[2].equalsIgnoreCase(p.getName())) {
            p.sendMessage(ChatColor.DARK_RED + "No te puedes despedir a ti mismo");
            return;
        }
        Empleados empl = new Empleados();
        empl.conectar();
        String empresanombre = args[1];
        String empleado = args[2];
        String razon = args[3];

        empl.despedir(empresanombre, empleado, razon, p);
        empl.desconectar();
    }
}
