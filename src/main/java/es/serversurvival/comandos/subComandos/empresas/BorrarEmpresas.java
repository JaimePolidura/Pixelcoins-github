package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.objetos.mySQL.Empresas;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empresa;
import es.serversurvival.objetos.solicitudes.BorrarSolicitud;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class BorrarEmpresas extends EmpresasSubCommand {
    private final String SCNombre = "borrar";
    private final String sintaxis = "/empresa borrar <empresa>";
    private final String ayuda = "Borrar tu propia empresa";

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
        String empresaNombre = args[1];

        empresasMySQL.conectar();
        Empresa empresaABorrar = empresasMySQL.getEmpresa(empresaNombre);
        if (empresaABorrar == null) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            empresasMySQL.desconectar();
            return;
        }
        if (!empresaABorrar.getOwner().equalsIgnoreCase(p.getName())) {
            p.sendMessage(ChatColor.DARK_RED + "No eres owner de esa empresa");
            empresasMySQL.desconectar();
            return;
        }
        empresasMySQL.desconectar();

        BorrarSolicitud borrarSolicitud = new BorrarSolicitud(p.getName(), empresaNombre);
        borrarSolicitud.enviarSolicitud();
    }
}