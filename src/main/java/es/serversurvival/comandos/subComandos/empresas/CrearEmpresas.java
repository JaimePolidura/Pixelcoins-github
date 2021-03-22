package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.mySQL.Empresas;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CrearEmpresas extends EmpresasSubCommand {
    private final String scnombre = "crear";
    private final String sintaxis = "/empresas crear <nombre> <descripccion...>";
    private final String ayuda = "Crear una empresa con una descripccion";

    public String getSCNombre() {
        return scnombre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player jugadorPlayer, String[] args) {
        if(args.length < 3){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        if(args[1].length() > Empresas.CrearEmpresaNombreLonMax){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "El tamaño del nombre de la empresas tiene que tener como maximo " + Empresas.CrearEmpresaNombreLonMax + " caracteres");
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 2; i < args.length; i++){
            stringBuilder.append(args[i]).append(" ");
        }
        String descripcion = stringBuilder.toString();
        if(descripcion.length() > Empresas.CrearEmpresaDescLonMax) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "La descripcion solo puede tener como maximo " + Empresas.CrearEmpresaNombreLonMax + " DescLonMax caracteres (incluidos espacios)");
            return;
        }
        empresasMySQL.conectar();
        Empresa empresaConElNombre = empresasMySQL.getEmpresa(args[1]);
        if(empresaConElNombre != null){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Ese nombre ya esta cogido");
            empresasMySQL.desconectar();
            return;
        }
        if(empresasMySQL.getEmpresasOwner(jugadorPlayer.getName()).size() + 1 > Empresas.nMaxEmpresas){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Solo puedes tener como maximo 6 empresas");
            empresasMySQL.desconectar();
            return;
        }

        empresasMySQL.crearEmpresa(args[1], jugadorPlayer, descripcion);
        empresasMySQL.desconectar();
    }
}