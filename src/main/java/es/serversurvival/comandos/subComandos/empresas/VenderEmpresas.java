package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Empresas;
import es.serversurvival.objetos.mySQL.Jugadores;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empresa;
import es.serversurvival.objetos.solicitudes.VenderSolicitud;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class VenderEmpresas extends EmpresasSubCommand {
    private Jugadores jugadoresMySQL = new Jugadores();
    private final String SCNOmbre = "vender";
    private final String sintaxis = "/empresas vender <empresa> <jugador> <precio>";
    private final String ayuda = "vender una empresa tuya a un determinado precio a un jugador";

    public String getSCNombre() {
        return SCNOmbre;
    }

    public String getSintaxis() {
        return sintaxis;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void execute(Player p, String[] args) {
        if (args.length != 4) {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        if(!Funciones.esDouble(args[3])){
            p.sendMessage(ChatColor.DARK_RED + "A ser posible mete numeros de precio no texto");
            return;
        }
        double precio = Double.parseDouble(args[3]);
        if(precio <= 0){
            p.sendMessage(ChatColor.DARK_RED + "A ser posible mete numeros que no sean negativo o que no sean ceros");
            return;
        }
        String jugador = args[2];
        if (jugador.equalsIgnoreCase(p.getName())) {
            p.sendMessage(ChatColor.DARK_RED + "No puedes vender tu empresa a ti mismo xd");
            return;
        }
        String nombreempresa = args[1];
        Player jugadorAVender = p.getServer().getPlayer(jugador);
        if (jugadorAVender == null) {
            p.sendMessage(ChatColor.DARK_RED + "Solo puedes vender tu empresa a jugadores que esten online");
            return;
        }

        empresasMySQL.conectar();
        Empresa empresaAVender = empresasMySQL.getEmpresa(nombreempresa);
        if (empresaAVender == null) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            empresasMySQL.desconectar();
            return;
        }
        if (!empresaAVender.getOwner().equalsIgnoreCase(p.getName())) {
            p.sendMessage(ChatColor.DARK_RED + "No eres dueño de esa empresa");
            empresasMySQL.desconectar();
            return;
        }
        double pixelcoinsDest = jugadoresMySQL.getDinero(jugador);
        if (pixelcoinsDest < precio) {
            p.sendMessage(ChatColor.DARK_RED + "" + jugador + " no tiene tantas pixelcoins como crees xd");
            empresasMySQL.desconectar();
            return;
        }

        VenderSolicitud venderSolicitud = new VenderSolicitud(p.getName(), jugador, nombreempresa, precio);
        venderSolicitud.enviarSolicitud();
    }
}