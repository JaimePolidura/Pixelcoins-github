package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.objetos.mySQL.Empresas;
import org.bukkit.entity.Player;

public class VerTodasEmpresasSubComando extends EmpresasSubCommand {
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
        Empresas empr = new Empresas();
        empr.conectar();
        empr.mostrarEmpresas(p);
        empr.desconectar();
    }
}
