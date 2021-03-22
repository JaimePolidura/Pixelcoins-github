package es.serversurvival.comandos;


import es.serversurvival.comandos.subComandos.ayuda.*;
import es.serversurvival.comandos.comandos.*;
import es.serversurvival.comandos.subComandos.bolsa.*;
import es.serversurvival.comandos.subComandos.deudas.*;
import es.serversurvival.comandos.subComandos.empleado.AyudaEmpleosSubComando;
import es.serversurvival.comandos.subComandos.empleado.IrseEmpleosSubComando;
import es.serversurvival.comandos.subComandos.empleado.MisTrabajosEmpleosSubComando;
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
        comandos.add(new DineroComando());
        comandos.add(new EstadisticasComando());
        comandos.add(new PagarComando());
        comandos.add(new TopComando());
        comandos.add(new CancelarDeudaDeudasSubComando());
        comandos.add(new PagarDeudaDeudasSubComando());
        comandos.add(new PrestarDeudasSubComando());
        comandos.add(new VerDeudasSubComando());
        comandos.add(new TiendaComando());
        comandos.add(new VenderComando());
        comandos.add(new MensajesComando());
        comandos.add(new AyudaDeudasSubComando());
        comandos.add(new CrearEmpresasSubComando());
        comandos.add(new DepositarEmpresasSubComando());
        comandos.add(new SacarEmpresasSubComando());
        comandos.add(new LogotipoEmpresasSubComando());
        comandos.add(new VerTodasEmpresasSubComando());
        comandos.add(new MiEmpresaEmpresasSubComando());
        comandos.add(new ContratarEmpresasSubComando());
        comandos.add(new DespedirEmpresasSubComando());
        comandos.add(new BorrarEmpresasSubComando());
        comandos.add(new VenderEmpresasSubComando());
        comandos.add(new EditarEmpleadoEmpresasSubComando());
        comandos.add(new EditarDescEmpresasSubComando());
        comandos.add(new EditarNombreEmpresasSubComando());
        comandos.add(new AyudaEmpresasSubComando());
        comandos.add(new ComprarComando());
        comandos.add(new MisTrabajosEmpleosSubComando());
        comandos.add(new IrseEmpleosSubComando());
        comandos.add(new AyudaEmpleosSubComando());
        comandos.add(new JugarAyudaSubComando());
        comandos.add(new NormasAyudaSubComando());
        comandos.add(new EmpresarioAyudaSubComando());
        comandos.add(new EmpleoAyudaSubComando());
        comandos.add(new DineroAyudaSubComando());
        comandos.add(new EstadisticasAyudaSubComando());
        comandos.add(new DeudaAyudaSubComando());
        comandos.add(new TiendaAyudaSubComando());
        comandos.add(new PrecioBolsaSubComando());
        comandos.add(new ValoresBolsaSubComando());
        comandos.add(new InvertirBolsaSubComando());
        comandos.add(new CarteraBolsaSubComando());
        comandos.add(new VenderBolsaSubComando());
        comandos.add(new EstadiscticasBolsaSubComando());
        comandos.add(new OperacionesCerradasBolsaSubComando());
        comandos.add(new AyudaBolsaSubComando());
        comandos.add(new BolsaAyuda());
        comandos.add(new VerCarteraBolsaSubComando());
        comandos.add(new CuentaComando());

        subComandos.add(new MisTrabajosEmpleosSubComando());
        subComandos.add(new IrseEmpleosSubComando());
        subComandos.add(new AyudaEmpleosSubComando());

        subComandos.add(new PrecioBolsaSubComando());
        subComandos.add(new ValoresBolsaSubComando());
        subComandos.add(new InvertirBolsaSubComando());
        subComandos.add(new CarteraBolsaSubComando());
        subComandos.add(new VenderBolsaSubComando());
        subComandos.add(new EstadiscticasBolsaSubComando());
        subComandos.add(new OperacionesCerradasBolsaSubComando());
        subComandos.add(new AyudaBolsaSubComando());
        subComandos.add(new VerCarteraBolsaSubComando());

        subComandos.add(new CancelarDeudaDeudasSubComando());
        subComandos.add(new PagarDeudaDeudasSubComando());
        subComandos.add(new PrestarDeudasSubComando());
        subComandos.add(new VerDeudasSubComando());
        subComandos.add(new AyudaDeudasSubComando());
        subComandos.add(new TiendaAyudaSubComando());

        subComandos.add(new JugarAyudaSubComando());
        subComandos.add(new NormasAyudaSubComando());
        subComandos.add(new EmpresarioAyudaSubComando());
        subComandos.add(new EmpleoAyudaSubComando());
        subComandos.add(new DineroAyudaSubComando());
        subComandos.add(new EstadisticasAyudaSubComando());
        subComandos.add(new DeudaAyudaSubComando());
        subComandos.add(new BolsaAyuda());

        subComandos.add(new CrearEmpresasSubComando());
        subComandos.add(new DepositarEmpresasSubComando());
        subComandos.add(new SacarEmpresasSubComando());
        subComandos.add(new LogotipoEmpresasSubComando());
        subComandos.add(new VerTodasEmpresasSubComando());
        subComandos.add(new MiEmpresaEmpresasSubComando());
        subComandos.add(new ContratarEmpresasSubComando());
        subComandos.add(new DespedirEmpresasSubComando());
        subComandos.add(new BorrarEmpresasSubComando());
        subComandos.add(new VenderEmpresasSubComando());
        subComandos.add(new EditarEmpleadoEmpresasSubComando());
        subComandos.add(new EditarDescEmpresasSubComando());
        subComandos.add(new EditarNombreEmpresasSubComando());
        subComandos.add(new AyudaEmpresasSubComando());
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