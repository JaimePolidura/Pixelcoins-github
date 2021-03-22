package es.serversurvival.comandos;


import es.serversurvival.comandos.subComandos.ayuda.*;
import es.serversurvival.comandos.comandos.*;
import es.serversurvival.comandos.subComandos.bolsa.*;
import es.serversurvival.comandos.subComandos.deudas.*;
import es.serversurvival.comandos.subComandos.empleado.AyudaEmpleos;
import es.serversurvival.comandos.subComandos.empleado.IrseEmpleos;
import es.serversurvival.comandos.subComandos.empleado.MisTrabajosEmpleos;
import es.serversurvival.comandos.subComandos.empresas.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class CommandManager implements CommandExecutor {
    private static Set<Comando> comandos = new HashSet<>();
    private static Set<SubComando> subComandos = new HashSet();

    public CommandManager() {
        comandos.add(new Dinero());
        comandos.add(new Estadisticas());
        comandos.add(new Pagar());
        comandos.add(new Top());
        comandos.add(new CancelarDeudaDeudas());
        comandos.add(new PagarDeudaDeudas());
        comandos.add(new PrestarDeudas());
        comandos.add(new VerDeudas());
        comandos.add(new Tienda());
        comandos.add(new Vender());
        comandos.add(new CMensajes());
        comandos.add(new AyudaDeudas());
        comandos.add(new CrearEmpresas());
        comandos.add(new DepositarEmpresas());
        comandos.add(new SacarEmpresas());
        comandos.add(new LogotipoEmpresas());
        comandos.add(new VerTodasEmpresas());
        comandos.add(new MiEmpresaEmpresas());
        comandos.add(new ContratarEmpresas());
        comandos.add(new DespedirEmpresas());
        comandos.add(new BorrarEmpresas());
        comandos.add(new VenderEmpresas());
        comandos.add(new EditarEmpleadoEmpresas());
        comandos.add(new EditarDescEmpresas());
        comandos.add(new EditarNombreEmpresas());
        comandos.add(new AyudaEmpresas());
        comandos.add(new Comprar());
        comandos.add(new MisTrabajosEmpleos());
        comandos.add(new IrseEmpleos());
        comandos.add(new AyudaEmpleos());
        comandos.add(new JugarAyuda());
        comandos.add(new NormasAyuda());
        comandos.add(new EmpresarioAyuda());
        comandos.add(new EmpleoAyuda());
        comandos.add(new DineroAyuda());
        comandos.add(new EstadisticasAyuda());
        comandos.add(new DeudaAyuda());
        comandos.add(new TiendaAyuda());
        comandos.add(new PrecioBolsa());
        comandos.add(new ValoresBolsa());
        comandos.add(new InvertirBolsa());
        comandos.add(new CarteraBolsa());
        comandos.add(new VenderBolsa());
        comandos.add(new EstadiscticasBolsa());
        comandos.add(new OperacionesCerradasBolsa());
        comandos.add(new AyudaBolsa());
        comandos.add(new BolsaAyuda());
        comandos.add(new VerCarteraBolsa());
        comandos.add(new CCuenta());

        subComandos.add(new MisTrabajosEmpleos());
        subComandos.add(new IrseEmpleos());
        subComandos.add(new AyudaEmpleos());

        subComandos.add(new PrecioBolsa());
        subComandos.add(new ValoresBolsa());
        subComandos.add(new InvertirBolsa());
        subComandos.add(new CarteraBolsa());
        subComandos.add(new VenderBolsa());
        subComandos.add(new EstadiscticasBolsa());
        subComandos.add(new OperacionesCerradasBolsa());
        subComandos.add(new AyudaBolsa());
        subComandos.add(new VerCarteraBolsa());

        subComandos.add(new CancelarDeudaDeudas());
        subComandos.add(new PagarDeudaDeudas());
        subComandos.add(new PrestarDeudas());
        subComandos.add(new VerDeudas());
        subComandos.add(new AyudaDeudas());
        subComandos.add(new TiendaAyuda());

        subComandos.add(new JugarAyuda());
        subComandos.add(new NormasAyuda());
        subComandos.add(new EmpresarioAyuda());
        subComandos.add(new EmpleoAyuda());
        subComandos.add(new DineroAyuda());
        subComandos.add(new EstadisticasAyuda());
        subComandos.add(new DeudaAyuda());
        subComandos.add(new BolsaAyuda());

        subComandos.add(new CrearEmpresas());
        subComandos.add(new DepositarEmpresas());
        subComandos.add(new SacarEmpresas());
        subComandos.add(new LogotipoEmpresas());
        subComandos.add(new VerTodasEmpresas());
        subComandos.add(new MiEmpresaEmpresas());
        subComandos.add(new ContratarEmpresas());
        subComandos.add(new DespedirEmpresas());
        subComandos.add(new BorrarEmpresas());
        subComandos.add(new VenderEmpresas());
        subComandos.add(new EditarEmpleadoEmpresas());
        subComandos.add(new EditarDescEmpresas());
        subComandos.add(new EditarNombreEmpresas());
        subComandos.add(new AyudaEmpresas());
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("No se puede poner comandos desde la consola del servidor");
            return true;
        }
        Player p = (Player) commandSender;
        String cn = command.getName();

        Comando comando = this.getComando(cn);
        if (comando == null) {
            p.sendMessage(ChatColor.DARK_RED + "Comando no encontrado: /ayuda");
            return true;
        }
        SubComando subComando = null;
        if (args.length != 0) {
            subComando = this.getSubComando(cn, args[0]);
            if (subComando != null) {
                subComando.execute(p, args);
                return true;
            }
        }
        if (comando instanceof SubComando) {
            if (comando instanceof AyudaSubCommand && args.length == 0) {
                AyudaSubCommand.mostrarAyudas(p);
                return true;
            }
            p.sendMessage(ChatColor.DARK_RED + "Necesitas introducer un subcomando: /" + cn + " ayuda");
            return true;
        }
        comando.execute(p, args);
        return true;
    }

    private Comando getComando(String cn) {
        for (Comando comando : comandos) {
            if (comando.getCNombre().equalsIgnoreCase(cn)) {
                return comando;
            }
        }
        return null;
    }

    private SubComando getSubComando(String cn, String scn) {
        for (SubComando subComando : subComandos) {
            if (subComando.getCNombre().equalsIgnoreCase(cn) && subComando.getSCNombre().equalsIgnoreCase(scn)) {
                return subComando;
            }
        }
        return null;
    }

    public static Set<SubComando> getSubComandos() {
        return subComandos;
    }
}