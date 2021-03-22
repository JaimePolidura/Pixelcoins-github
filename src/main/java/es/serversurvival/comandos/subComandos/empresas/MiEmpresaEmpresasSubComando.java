package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.objetos.mySQL.Empresas;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MiEmpresaEmpresasSubComando extends EmpresasSubCommand {
    private final String SCNombre = "miempresa";
    private final String sintaxis = "/empresas miempresa <empresa>";
    private final String ayuda = "ver los empleados, pixelcoins etc de mi propia empresa";

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
        Empresas empr = new Empresas();
        empr.conectar();
        empr.verEmpresa(p, args[1]);
        empr.desconectar();
    }
}
