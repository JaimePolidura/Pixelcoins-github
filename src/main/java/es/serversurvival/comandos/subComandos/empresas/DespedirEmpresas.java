package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DespedirEmpresas extends EmpresasSubCommand {
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

    public void execute(Player jugadorPlayer, String[] args) {
        if (args.length != 4) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: /despedir <empresa> <nombreJugador> <razon>");
            return;
        }
        if (args[2].equalsIgnoreCase(jugadorPlayer.getName())) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No te puedes despedir a ti mismo");
            return;
        }
        String empresanombre = args[1];
        String empleado = args[2];
        String razon = args[3];

        empresasMySQL.conectar();
        Empresa empresaDondeSeDespide = empresasMySQL.getEmpresa(empresanombre);
        if(empresaDondeSeDespide == null){
            empresasMySQL.desconectar();
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Esa empresa no exsiste");
            return;
        }
        if (!empresaDondeSeDespide.getOwner().equalsIgnoreCase(jugadorPlayer.getName())) {
            empresasMySQL.desconectar();
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No eres owner de esa empresa");
            return;
        }
        if(!empleadosMySQL.trabajaEmpresa(empleado, empresanombre)){
            empresasMySQL.desconectar();
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Creo que ese men no trabaja en tu empresa");
            return;
        }

        empleadosMySQL.despedir(empresanombre, empleado, razon, jugadorPlayer);
        empleadosMySQL.desconectar();
    }
}
