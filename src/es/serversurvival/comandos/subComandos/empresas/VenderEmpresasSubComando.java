package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.objetos.mySQL.Empresas;
import es.serversurvival.objetos.mySQL.Jugadores;
import es.serversurvival.objetos.solicitudes.VenderSolicitud;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class VenderEmpresasSubComando extends EmpresasSubCommand {
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
        double precio;
        try {
            precio = Double.parseDouble(args[3]);
            if (precio <= 0) {
                p.sendMessage(ChatColor.DARK_RED + "A ser posible mete numeros que no sean negativo o que no sean ceros");
                return;
            }
        } catch (Exception e) {
            p.sendMessage(ChatColor.DARK_RED + "A ser posible mete numeros de precio no texto");
            return;
        }
        String jugador = args[2];
        if (jugador.equalsIgnoreCase(p.getName())) {
            p.sendMessage(ChatColor.DARK_RED + "No puedes vender tu empresa a ti mismo");
            return;
        }
        Player tp1 = p.getServer().getPlayer(jugador);
        if (tp1 == null) {
            p.sendMessage(ChatColor.DARK_RED + "Solo puedes vender tu empresa a jugadores que esten online");
            return;
        }

        String nombreempresa = args[1];
        Empresas empr = new Empresas();
        empr.conectar();
        boolean regVen = empr.estaRegistradoNombre(nombreempresa);
        if (!regVen) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        boolean owVen = empr.esOwner(p.getName(), nombreempresa);
        if (!owVen) {
            p.sendMessage(ChatColor.DARK_RED + "No eres dueño de esa empresa");
            return;
        }
        empr.desconectar();

        Jugadores j = new Jugadores();
        j.conectar();
        double pixelcoinsDest = j.getDinero(jugador);
        j.desconectar();

        if (pixelcoinsDest < precio) {
            p.sendMessage(ChatColor.DARK_RED + "" + jugador + " no tiene tantas pixelcoins como crees xd");
            return;
        }
        VenderSolicitud venderSolicitud = new VenderSolicitud(p.getName(), jugador, nombreempresa, precio);
        venderSolicitud.enviarSolicitud();
        //t.nuevaTransaccion(jugador, p.getName(), precio, nombreempresa, Transacciones.TIPO.EMPRESA_VENTA);
    }
}