package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.objetos.mySQL.Empresas;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empresa;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EditarNombreEmpresas extends EmpresasSubCommand {
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

    public void execute(Player jugadorPlauer, String[] args) {
        if (args.length != 3) {
            jugadorPlauer.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        String nombreEmpresaAEditar = args[1];
        String nuevoNombreEmpresa = args[2];
        if (nuevoNombreEmpresa.toCharArray().length > Empresas.CrearEmpresaNombreLonMax) {
            jugadorPlauer.sendMessage(ChatColor.DARK_RED + "La longitud maxima del nombre es " + Empresas.CrearEmpresaNombreLonMax);
            return;
        }

        empresasMySQL.conectar();
        Empresa empresaACambiar = empresasMySQL.getEmpresa(nombreEmpresaAEditar);
        if (empresaACambiar == null) {
            jugadorPlauer.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            empresasMySQL.desconectar();
            return;
        }
        if (empresasMySQL.getEmpresa(nuevoNombreEmpresa) != null) {
            jugadorPlauer.sendMessage(net.md_5.bungee.api.ChatColor.DARK_RED + "Esa nombre ya esta cogido");
            empresasMySQL.desconectar();
            return;
        }
        if (!empresaACambiar.getOwner().equalsIgnoreCase(jugadorPlauer.getName())) {
            jugadorPlauer.sendMessage(net.md_5.bungee.api.ChatColor.DARK_RED + "No eres el due?o de esa empresa");
            empresasMySQL.desconectar();
            return;
        }

        empresasMySQL.cambiarNombre(jugadorPlauer, args[1], nuevoNombreEmpresa);
        empresasMySQL.desconectar();
    }
}