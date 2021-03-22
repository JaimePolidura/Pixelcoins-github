package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EditarDescEmpresas extends EmpresasSubCommand {
    private final String SCNombre = "editardescripccion";
    private final String sintaxis = "/empresas editardescripccion <empresa> <nueva desc>";
    private final String ayuda = "Editar la descripcion de tu empresa";

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
        if (args.length < 3) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        String descripcion = buildDescripcion(args, 2);
        if(descripcion.length() + 1 > Empresas.CrearEmpresaDescLonMax){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "La longitud maxima de la descripccion es de " + Empresas.CrearEmpresaDescLonMax + " caracteres");
            return;
        }

        empresasMySQL.conectar();
        Empresa empresaAEditar = empresasMySQL.getEmpresa(args[1]);
        if(empresaAEditar == null){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "La empresa no existe");
            empresasMySQL.desconectar();
            return;
        }
        if(!empresaAEditar.getOwner().equalsIgnoreCase(jugadorPlayer.getName())){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No eres el owner de la empresa");
            empresasMySQL.desconectar();
            return;
        }

        empresasMySQL.cambiarDescripciom(empresaAEditar.getNombre(), descripcion, jugadorPlayer);
        empresasMySQL.desconectar();
    }

    private String buildDescripcion (String[] args, int startIndex) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = startIndex; i < args.length; i++){
            stringBuilder.append(args[i]).append(" ");
        }

        return stringBuilder.toString();
    }
}
