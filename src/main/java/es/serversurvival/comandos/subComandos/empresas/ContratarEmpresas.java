package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Empleados;
import es.serversurvival.objetos.mySQL.Empresas;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empleado;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empresa;
import es.serversurvival.objetos.solicitudes.ContratarSolicitud;
import es.serversurvival.objetos.solicitudes.Solicitud;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.security.IdentityScope;

public class ContratarEmpresas extends EmpresasSubCommand {
    private Empleados empleadosMySQL = new Empleados();
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
        if(!Funciones.esDouble(args[3])){
            p.sendMessage(ChatColor.DARK_RED + "A ser posible introduce texto, no numeros.");
            return;
        }
        double sueldo = Double.parseDouble(args[3]);
        if(sueldo <= 0){
            p.sendMessage(ChatColor.DARK_RED + "EXPLOTADORRRR MACHURULOOO MACHISTA OPRESOR");
            return;
        }
        if(Bukkit.getPlayer(args[1]) == null){
            p.sendMessage(ChatColor.DARK_RED + "Solo puedes contratar a jugadores que esten online");
            return;
        }
        Player jugadorAContratarPlayer = Bukkit.getPlayer(args[1]);
        if(jugadorAContratarPlayer.getName().equalsIgnoreCase(p.getName())){
            p.sendMessage(ChatColor.DARK_RED + "No te puedes contratar a ti mismo crack");
            return;
        }
        if(!Empleados.esUnTipoDeSueldo(args[4])){
            p.sendMessage(ChatColor.DARK_RED + "El tipo de pago de sueldo puede ser:");
            p.sendMessage(ChatColor.DARK_RED + "   s: El sueldo se pagara cada semana");
            p.sendMessage(ChatColor.DARK_RED + "   2s: El sueldo se pagara cada 2 semanas");
            p.sendMessage(ChatColor.DARK_RED + "   m: El sueldo se pagara cada mes");
            p.sendMessage(ChatColor.DARK_RED + "   d: El sueldo se pagara cada dia");
            return;
        }

        String cargo;
        if(args.length == 6){
            cargo = args[5];
        }else{
            cargo = "Trabajador";
        }

        empresasMySQL.conectar();
        Empresa empresaDondeContratar = empresasMySQL.getEmpresa(args[2]);
        if (empresaDondeContratar == null) {
            p.sendMessage(ChatColor.DARK_RED + "Esa empresa no existe");
            empresasMySQL.desconectar();
            return;
        }
        if (!empresaDondeContratar.getOwner().equalsIgnoreCase(p.getName())) {
            p.sendMessage(ChatColor.DARK_RED + "No eres dueño de esa empresa");
            empresasMySQL.desconectar();
            return;
        }
        Empleado empleadoSupuestamenteNoContratado = empleadosMySQL.getEmpleado(args[1], args[2]);
        if (empleadoSupuestamenteNoContratado != null) {
            empresasMySQL.desconectar();
            p.sendMessage(ChatColor.DARK_RED + "Ese jugador ya esta contratado / ya le has enviado solicitud");
            return;
        }
        if (Solicitud.haSidoSolicitado(args[1])) {
            p.sendMessage(ChatColor.DARK_RED + "A ese jugador ya le han enviado una solicitud/ya le has enviado una solicitud");
            return;
        }

        ContratarSolicitud contratarSolicitud = new ContratarSolicitud(p.getName(), jugadorAContratarPlayer.getName(), args[2], sueldo, args[4], cargo);
        contratarSolicitud.enviarSolicitud();
    }
}