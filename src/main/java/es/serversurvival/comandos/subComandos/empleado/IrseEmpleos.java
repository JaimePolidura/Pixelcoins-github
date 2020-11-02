package es.serversurvival.comandos.subComandos.empleado;

import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class IrseEmpleos extends EmpleosSubCommand {
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

    public void execute(Player jugadorPlayer, String[] args) {
        if (args.length != 2) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        empresasMySQL.conectar();
        Empresa empresaAEditarEmpleado = empresasMySQL.getEmpresa(args[1]);
        if (empresaAEditarEmpleado == null) {
            empresasMySQL.desconectar();
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Esa empresa no exsiste");
            return;
        }
        if (!empleadosMySQL.trabajaEmpresa(jugadorPlayer.getName(), empresaAEditarEmpleado.getNombre())) {
            empresasMySQL.desconectar();
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Ese jugador no trabaja en la empresa");
            return;
        }

        empleadosMySQL.irseEmpresa(args[1], jugadorPlayer);
        empleadosMySQL.desconectar();
    }
}
