package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.main.Funciones;
import es.serversurvival.objetos.mySQL.Empleados;
import es.serversurvival.objetos.mySQL.Empresas;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empleado;
import es.serversurvival.objetos.mySQL.tablasObjetos.Empresa;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EditarEmpleadoEmpresas extends EmpresasSubCommand {
    private final String SCNombre = "editarempleado";
    private final String sintaxis = "/empresas editarempleado <empresa> <empleado> <tipo> <valor>";
    private final String ayuda = "editar un empleado de tu empresa. El tipo (<tipo>) puede ser: sueldo y tiposueldo, donde el primero cambia el sueldo y el segundo la frecuencia de pago (/ayuda empresario). Y el valor lo que quieres poner";

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
        if (args.length != 5) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }
        if (args[2].equalsIgnoreCase(jugadorPlayer.getName())) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No te puedes editar a ti mismo");
            return;
        }

        String tipoEditar = args[3];
        switch (tipoEditar.toLowerCase()) {
            case "sueldo":
                editarSueldo(args[1], args[2], args[4] ,jugadorPlayer);
                break;

            case "tiposueldo":
                editarTipoSueldo(args[1], args[2], args[4], jugadorPlayer);
                break;

            default:
                jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Tipo incorrecto, tipos:");
                jugadorPlayer.sendMessage(ChatColor.DARK_RED + "sueldo");
                jugadorPlayer.sendMessage(ChatColor.DARK_RED + "tiposueldo");
                break;
        }
    }

    public void editarSueldo (String empresa, String jugadorAEditar, String sueldo, Player jugadorPlayer) {
        if(!Funciones.esDouble(sueldo)){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "El sueldo ha de ser un numero positivo");
            return;
        }
        double sueldoAPoner = Double.parseDouble(sueldo);
        if (sueldoAPoner <= 0) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "A ser posible mete numeros que sean superiores a 0");
            return;
        }
        empresasMySQL.conectar();
        Empresa empresaAEditarEmpleado = empresasMySQL.getEmpresa(empresa);
        if(empresaAEditarEmpleado == null){
            jugadorPlayer.sendMessage(net.md_5.bungee.api.ChatColor.DARK_RED + "Esa empresa no exsiste");
            empresasMySQL.desconectar();
            return;
        }
        if (!empresaAEditarEmpleado.getOwner().equalsIgnoreCase(jugadorPlayer.getName())) {
            jugadorPlayer.sendMessage(net.md_5.bungee.api.ChatColor.DARK_RED + "No eres due?o de eas empresa");
            empresasMySQL.desconectar();
            return;
        }
        Empleado empleadoAditar = empleadosMySQL.getEmpleado(jugadorAEditar, empresa);
        if (!empleadosMySQL.trabajaEmpresa(jugadorAEditar, empresa)) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Ese jugador no trabaja en la empresa");
            empresasMySQL.desconectar();
            return;
        }

        empleadosMySQL.editarEmpleadoSueldo(empresaAEditarEmpleado, empleadoAditar, sueldoAPoner);
        empresasMySQL.desconectar();
    }

    public void editarTipoSueldo (String empresa, String jugadorAEditar, String tipo, Player jugadorPlayer) {
        if (!Empleados.esUnTipoDeSueldo(tipo)) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Tipo incorrecto:");
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "d: el sueldo se paga diariamente");
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "s: el sueldo se paga cada semana");
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "2s: el sueldo se paga cada 2 semanas");
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "m: el sueldo se paga cada mes");
            return;
        }

        empresasMySQL.conectar();
        Empresa empresaAEditarEmpleado = empresasMySQL.getEmpresa(empresa);
        if(empresaAEditarEmpleado == null){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Esa empresa no exsiste");
            empresasMySQL.desconectar();
            return;
        }
        if (!empresaAEditarEmpleado.getOwner().equalsIgnoreCase(jugadorPlayer.getName())) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "No eres due?o de eas empresa");
            empresasMySQL.desconectar();
            return;
        }
        Empleado empleadoAditar = empleadosMySQL.getEmpleado(jugadorAEditar, empresa);
        if (!empleadosMySQL.trabajaEmpresa(jugadorAEditar, empresa)) {
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Ese jugador no trabaja en la empresa");
            empresasMySQL.desconectar();
            return;
        }

        empleadosMySQL.editarTipoSueldo(empresaAEditarEmpleado, empleadoAditar, tipo);
        empresasMySQL.desconectar();
    }
}