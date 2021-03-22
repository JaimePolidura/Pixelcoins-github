package es.serversurvival.comandos.subComandos.empleado;

import es.serversurvival.objetos.mySQL.Empleados;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class IrseEmpleosSubComando extends EmpleosSubCommand {
    private final String SCNombre = "irse";
    private final String sintaxis = "/empleos irse <empresa>";
    private final String ayuda = "irse de un trabajo";

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
        if (args.length != 2) {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        Empleados empl = new Empleados();
        empl.conectar();
        empl.irseEmpresa(args[1], p);
        empl.desconectar();
    }
}
