package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.mySQL.MySQL;
import es.serversurvival.util.Funciones;
import es.serversurvival.mySQL.Empleados;
import es.serversurvival.mySQL.tablasObjetos.Empleado;
import es.serversurvival.mySQL.tablasObjetos.Empresa;
import es.serversurvival.validaciones.Validaciones;
import main.ValidationResult;
import main.ValidationsService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import static es.serversurvival.validaciones.Validaciones.*;

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
        MySQL.conectar();

        ValidationResult result = ValidationsService.startValidating(args.length == 5, True.of(mensajeUsoIncorrecto()))
                .andMayThrowException(() -> args[2], mensajeUsoIncorrecto(), NotEqualsIgnoreCase.of(jugadorPlayer.getName(), "No te puedes editar a ti mismo"))
                .andMayThrowException(() -> args[1], mensajeUsoIncorrecto(), OwnerDeEmpresa.of(jugadorPlayer.getName()))
                .andMayThrowException(() -> args[2], mensajeUsoIncorrecto(), TrabajaEmpresa.en(() -> args[1]))
                .validateAll();

        if(result.isFailed()){
            MySQL.desconectar();
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + result.getMessage());
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

        MySQL.desconectar();
    }

    public void editarSueldo (String empresa, String jugadorAEditar, String sueldo, Player player) {
        ValidationResult result = ValidationsService.validate(sueldo, PositiveNumber);

        if(result.isFailed()){
            player.sendMessage(ChatColor.DARK_RED + result.getMessage());
            return;
        }

        double sueldoAPoner = Double.parseDouble(sueldo);
        Empresa empresaAEditarEmpleado = empresasMySQL.getEmpresa(empresa);
        Empleado empleadoAditar = empleadosMySQL.getEmpleado(jugadorAEditar, empresa);

        empleadosMySQL.editarEmpleadoSueldo(empresaAEditarEmpleado, empleadoAditar, sueldoAPoner);
    }

    public void editarTipoSueldo (String empresa, String jugadorAEditar, String tipo, Player jugadorPlayer) {
        if(!Empleados.esUnTipoDeSueldo(tipo)){
            jugadorPlayer.sendMessage(ChatColor.DARK_RED + "Tipo incorrecto. d: cada dia, s: cada semana, 2s: cada dos semanas, m: cada mes");
            return;
        }

        Empresa empresaAEditarEmpleado = empresasMySQL.getEmpresa(empresa);
        Empleado empleadoAditar = empleadosMySQL.getEmpleado(jugadorAEditar, empresa);

        empleadosMySQL.editarTipoSueldo(empresaAEditarEmpleado, empleadoAditar, tipo);
    }
}
