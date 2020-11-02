package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.menus.menus.confirmaciones.BorrrarEmpresaConfirmacion;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
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

    public void execute(Player player, String[] args) {
        if (args.length != 2) {
            player.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        String empresaNombre = args[1];

        empresasMySQL.conectar();
        Empresa empresaABorrar = empresasMySQL.getEmpresa(empresaNombre);
        if (empresaABorrar == null) {
            player.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            empresasMySQL.desconectar();
            return;
        }
        if (!empresaABorrar.getOwner().equalsIgnoreCase(player.getName())) {
            player.sendMessage(ChatColor.DARK_RED + "No eres owner de esa empresa");
            empresasMySQL.desconectar();
            return;
        }
        empresasMySQL.desconectar();

        BorrrarEmpresaConfirmacion confirmacionMenu = new BorrrarEmpresaConfirmacion(player, empresaNombre);
        confirmacionMenu.openMenu();
    }
}