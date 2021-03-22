package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.objetos.mySQL.Empleados;
import es.serversurvival.objetos.mySQL.Empresas;
import es.serversurvival.objetos.solicitudes.ContratarSolicitud;
import es.serversurvival.objetos.solicitudes.Solicitud;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ContratarEmpresasSubComando extends EmpresasSubCommand {
    private final String SCNombre = "contratar";
    private final String sintaxis = "/empresas contratar <jugador> <empresa> <sueldo> <tipo sueldo (/ayuda empresas)> [cargo]";
    private final String ayuda = "Contratar a un juagdor a tu empresa: <sueldo> cantidad de pixel coins que le vas a pagar, <tipo> frequencia de pago de sueldo (/ayuda empresario), [cargo] es opcional";

    public String getSCNombre() {
        return SCNombre;
    }

    @Override
    public String getSintaxis() {
        return sintaxis;
    }

    @Override
    public String getAyuda() {
        return null;
    }

    @Override
    public void execute(Player p, String[] args) {
        if (args.length != 5 && args.length != 6) {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        //empresas contratar <jugador> <empresa> <sueldo> <tipo sueldo (/ayuda empresas)> [cargo]
        double sueldo = 0;
        try {
            sueldo = Double.parseDouble(args[3]);
            if (sueldo <= 0) {
                p.sendMessage(ChatColor.DARK_RED + "EXPLOTADORRRR MACHURULOOO MACHISTA OPRESOR");
                return;
            }
        } catch (Exception e) {
            p.sendMessage(ChatColor.DARK_RED + "A ser posible introduce texto, no numeros.");
            return;
        }
        String nombreContratar = args[1];
        Player tp2 = p.getServer().getPlayer(nombreContratar);
        try {
            nombreContratar = tp2.getName();
        } catch (Exception e) {
            p.sendMessage(ChatColor.DARK_RED + "Solo puedes contratar a jugadores que esten online");
            return;
        }

        if (nombreContratar.equalsIgnoreCase(p.getName())) {
            p.sendMessage(ChatColor.DARK_RED + "No te puedes contratar a ti mismo crack");
            return;
        }

        String nombreEmpresa = args[2];
        String tipo = args[4];
        String cargo = "Trabajador";
        if (args.length == 6) {
            cargo = args[5];
        }

        if (!tipo.equalsIgnoreCase("s") && !tipo.equalsIgnoreCase("m") && !tipo.equalsIgnoreCase("2s") && !tipo.equalsIgnoreCase("d")) {
            p.sendMessage(ChatColor.DARK_RED + "El tipo de pago de sueldo puede ser:");
            p.sendMessage(ChatColor.DARK_RED + "   s: El sueldo se pagara cada semana");
            p.sendMessage(ChatColor.DARK_RED + "   2s: El sueldo se pagara cada 2 semanas");
            p.sendMessage(ChatColor.DARK_RED + "   m: El sueldo se pagara cada mes");
            p.sendMessage(ChatColor.DARK_RED + "   d: El sueldo se pagara cada dia");
            return;
        }

        Empresas empr = new Empresas();
        empr.conectar();
        boolean reg = empr.estaRegistradoNombre(nombreEmpresa);

        if (!reg) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            return;
        }
        boolean ow = empr.esOwner(p.getName(), nombreEmpresa);
        if (!ow) {
            p.sendMessage(ChatColor.DARK_RED + "No eres dueño de esa empresa");
            return;
        }
        empr.desconectar();

        Empleados empl = new Empleados();
        int id = 0;
        empl.conectar();
        id = empl.getId(nombreContratar, nombreEmpresa);
        if (id != -1) {
            p.sendMessage(ChatColor.DARK_RED + "Ese jugador ya esta contratado / ya le has enviado solicitud");
            return;
        }
        empl.desconectar();

        if (Solicitud.haSidoSolicitado(nombreContratar)) {
            p.sendMessage(ChatColor.DARK_RED + "A ese jugador ya le han enviado una solicitud/ya le has enviado una solicitud");
            return;
        }
        ContratarSolicitud contratarSolicitud = new ContratarSolicitud(p.getName(), tp2.getName(), nombreEmpresa, sueldo, tipo, cargo);
        contratarSolicitud.enviarSolicitud();
    }
}
