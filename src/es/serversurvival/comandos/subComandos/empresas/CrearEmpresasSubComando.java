package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.objetos.mySQL.Empresas;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class CrearEmpresasSubComando extends EmpresasSubCommand {
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

    //empresa crear <?> <?...>
    public void execute(Player p, String[] args) {
        if (args.length >= 3) {
            String nombre = args[1];
            int nombreLongitud = nombre.toCharArray().length;
            if (nombreLongitud <= Empresas.CrearEmpresaNombreLonMax) {
                String descripcion = "";
                StringBuffer sb = new StringBuffer();
                for (int i = 2; i < args.length; i++) {
                    sb.append(args[i] + " ");
                }
                descripcion = sb.toString();

                int longitudDescripccion = descripcion.toCharArray().length;
                if (longitudDescripccion <= Empresas.CrearEmpresaNombreLonMax) {
                    Empresas empr = new Empresas();
                    empr.conectar();
                    empr.crearEmpresa(nombre, p, descripcion);
                    empr.desconectar();
                } else {
                    p.sendMessage(ChatColor.DARK_RED + "La descripcion solo puede tener como maximo " + Empresas.CrearEmpresaNombreLonMax + " DescLonMax caracteres (incluidos espacios)");
                }

            } else {
                p.sendMessage(ChatColor.DARK_RED + "El tamaño del nombre de la empresas tiene que tener como maximo " + Empresas.CrearEmpresaNombreLonMax + " caracteres");
            }
        } else {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
        }
    }
}
