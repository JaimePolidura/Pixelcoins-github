package es.serversurvival.comandos.subComandos.empresas;

import es.serversurvival.objetos.mySQL.Empleados;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class EditarEmpleadoEmpresasSubComando extends EmpresasSubCommand {
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

    public void execute(Player p, String[] args) {
        if (args.length != 5) {
            p.sendMessage(ChatColor.DARK_RED + "Uso incorrecto: " + this.sintaxis);
            return;
        }

        if (args[2].equalsIgnoreCase(p.getName())) {
            p.sendMessage(ChatColor.DARK_RED + "No te puedes editar a ti mismo");
            return;
        }

        String tipoEditar = args[3];
        switch (tipoEditar.toLowerCase()) {
            case "sueldo":
                double sueldoEditar;
                try {
                    sueldoEditar = Double.parseDouble(args[4]);
                } catch (Exception e) {
                    p.sendMessage(ChatColor.DARK_RED + "A ser posible mete texto no numeros");
                    break;
                }
                if (sueldoEditar <= 0) {
                    p.sendMessage(ChatColor.DARK_RED + "A ser posible mete numeros que sean superiores a 0");
                    break;
                }
                Empleados empl = new Empleados();
                empl.conectar();
                empl.editarSueldo(args[2], args[1], p, args[3], args[4]);
                empl.desconectar();
                break;

            case "tiposueldo":
                String tipoSueldo = args[4];
                tipoSueldo.toLowerCase();
                if (!tipoSueldo.equalsIgnoreCase("s") && !tipoSueldo.equalsIgnoreCase("2s") && !tipoSueldo.equalsIgnoreCase("d") && !tipoSueldo.equalsIgnoreCase("m")) {
                    p.sendMessage(ChatColor.DARK_RED + "Tipo incorrecto:");
                    p.sendMessage(ChatColor.DARK_RED + "d: el sueldo se paga diariamente");
                    p.sendMessage(ChatColor.DARK_RED + "s: el sueldo se paga cada semana");
                    p.sendMessage(ChatColor.DARK_RED + "2s: el sueldo se paga cada 2 semanas");
                    p.sendMessage(ChatColor.DARK_RED + "m: el sueldo se paga cada mes");
                    break;
                }
                Empleados empll = new Empleados();
                empll.conectar();
                empll.editarSueldo(args[2], args[1], p, args[3], args[4]);
                empll.desconectar();
                break;

            default:
                p.sendMessage(ChatColor.DARK_RED + "Tipo incorrecto, tipos:");
                p.sendMessage(ChatColor.DARK_RED + "sueldo");
                p.sendMessage(ChatColor.DARK_RED + "tiposueldo");
                break;
        }
    }
}
