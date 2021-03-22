package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.objetos.mySQL.Empresas;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empresa;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MiEmpresaEmpresas extends EmpresasSubCommand {
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

        empresasMySQL.conectar();
        Empresa empresa = empresasMySQL.getEmpresa(args[1]);
        if (empresa == null) {
            p.sendMessage(net.md_5.bungee.api.ChatColor.DARK_RED + "Esa empresa no existe");
            empresasMySQL.desconectar();
            return;
        }
        if (!empresa.getOwner().equalsIgnoreCase(p.getName())) {
            p.sendMessage(net.md_5.bungee.api.ChatColor.DARK_RED + "No eres el dueño de la empresa");
            empresasMySQL.desconectar();
            return;
        }

        empresasMySQL.verEmpresa(p, args[1]);
        empresasMySQL.desconectar();
    }
}