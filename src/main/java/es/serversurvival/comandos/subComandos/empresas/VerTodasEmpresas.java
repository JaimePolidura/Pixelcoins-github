package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.menus.menus.EmpresasVerTodasMenu;
import org.bukkit.entity.Player;

public class VerTodasEmpresas extends EmpresasSubCommand {
    private final String SCNombre = "vertodas";
    private final String sintaxis = "/empresas vertodas";
    private final String ayuda = "ver todas las empresas creadas hasta el momento";

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
        EmpresasVerTodasMenu menu = new EmpresasVerTodasMenu(p);
        menu.openMenu();
    }
}