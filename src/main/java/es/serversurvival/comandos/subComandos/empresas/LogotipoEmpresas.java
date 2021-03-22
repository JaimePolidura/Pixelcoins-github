package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.objetos.mySQL.Empresas;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empresa;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LogotipoEmpresas extends EmpresasSubCommand {
    private final String SCNombre = "logotipo";
    private final String sintaxis = "/empresas logotipo <empresa>";
    private final String ayuda = "Cambiar el logotipo de tu empresa al objeto que selecciones en la mano el ejecutar este comando";

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
        if(args.length != 2){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        String nuevoLogotipo = jugadorPlayer.getInventory().getItemInMainHand().getType().toString();
        if(nuevoLogotipo.equalsIgnoreCase("AIR")){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Tienes que seleccionar un objeto en la mano");
        }

        empresasMySQL.conectar();
        Empresa empresaAEditar = empresasMySQL.getEmpresa(args[1]);
        if (empresaAEditar == null) {
            jugadorPlayer.sendMessage(net.md_5.bungee.api.ChatColor.DARK_RED + "Esa empresa no existe");
            empresasMySQL.desconectar();
            return;
        }
        if (!empresaAEditar.getOwner().equalsIgnoreCase(jugadorPlayer.getName())) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No eres el dueño de esa empresa");
            empresasMySQL.desconectar();
            return;
        }

        empresasMySQL.cambiarIcono(args[1], jugadorPlayer, nuevoLogotipo);
        empresasMySQL.desconectar();
    }
}
