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

import java.util.*;

public class CommandManager implements CommandExecutor {
    private Map<String, Comando> comandos = new HashMap<>();
    private Map<String, Map<String, SubComando>> subComandos = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("No se puede poner comandos desde la consola del servidor");
            return true;
        }

        Player player = (Player) commandSender;
        String commandName = command.getName();

        ejecutarComando(player, commandName, args);

        return true;
    }

    private void ejecutarComando (Player player, String commandName, String[] args) {
        if(esUnSubComando(commandName) && esUnSubComandoValido(commandName, args)) {
            subComandos.get(commandName).get(args[0]).execute(player, args);

        }else if (esUnComando(commandName)) {
            comandos.get(commandName).execute(player, args);

        }else if (esUnSubComando(commandName) && commandName.equalsIgnoreCase("ayuda")) {
            AyudaSubCommand.mostrarAyudas(player);

        }else{
            player.sendMessage(ChatColor.DARK_RED + "Comando no encontrado: /ayuda");
        }
    }

    private boolean esUnSubComando (String nombre) {
        return subComandos.get(nombre) != null;
    }

    private boolean esUnComando (String commandName) {
        return this.comandos.get(commandName) != null;
    }

    private boolean esUnSubComandoValido (String mainName, String[] args) {
        return args.length > 0 && subComandos.get(mainName).get(args[0]) != null;
    }

    public Map<String, SubComando> getSubComandosDe (String subComandoMain) {
        return subComandos.get(subComandoMain);
    }

    public CommandManager() {
        comandos.put("dinero", new Dinero());
        comandos.put("pagar", new Pagar());
        comandos.put("top", new Top());
        comandos.put("tienda", new Tienda());
        comandos.put("vender", new Vender());
        comandos.put("mensajes", new CMensajes());
        comandos.put("re", new Re());
        comandos.put("Comprar", new Comprar());
        comandos.put("cuenta", new CCuenta());
        comandos.put("perfil", new Perfil());
        comandos.put("venderjugador", new VenderJugador());

        Map<String, SubComando> subComandosBolsa = new HashMap<>();
        subComandosBolsa.put("precio", new PrecioBolsa());
        subComandosBolsa.put("valores", new ValoresBolsa());
        subComandosBolsa.put("invertir", new InvertirBolsa());
        subComandosBolsa.put("cartera", new CarteraBolsa());
        subComandosBolsa.put("vender", new VenderBolsa());
        subComandosBolsa.put("estadisticas", new EstadiscticasBolsa());
        subComandosBolsa.put("operacionescerradas", new OperacionesCerradasBolsa());
        subComandosBolsa.put("ayuda", new AyudaBolsa());
        subComandosBolsa.put("vercartera", new VerCarteraBolsa());
        subComandosBolsa.put("vendercorto", new VenderCortoBolsa());
        subComandosBolsa.put("comprarcorto", new ComprarCorto());
        subComandosBolsa.put("per", new PerBolsa());
        subComandosBolsa.put("dividendo", new DividendoBolsa());
        subComandosBolsa.put("ordenes", new OrdenesBolsa());
        subComandos.put("bolsa", subComandosBolsa);

        Map<String, SubComando> subComandoEmpleo = new HashMap<>();
        subComandoEmpleo.put("misempleos", new MisTrabajosEmpleos());
        subComandoEmpleo.put("irse", new IrseEmpleos());
        subComandoEmpleo.put("ayuda", new AyudaEmpleos());
        subComandos.put("empleos", subComandosBolsa);

        Map<String, SubComando> subComandosDeudas = new HashMap<>();
        subComandosDeudas.put("cancelar", new CancelarDeudaDeudas());
        subComandosDeudas.put("pagar", new PagarDeudaDeudas());
        subComandosDeudas.put("prestar", new PrestarDeudas());
        subComandosDeudas.put("ver", new VerDeudas());
        subComandosDeudas.put("ayuda", new AyudaDeudas());
        subComandos.put("deudas", subComandosDeudas);

        Map<String, SubComando> subComandosAyuda = new HashMap<>();
        subComandosAyuda.put("tienda", new TiendaAyuda());
        subComandosAyuda.put("jugar", new JugarAyuda());
        subComandosAyuda.put("normas", new NormasAyuda());
        subComandosAyuda.put("empresario", new EmpresarioAyuda());
        subComandosAyuda.put("empleo", new EmpleoAyuda());
        subComandosAyuda.put("dinero", new DineroAyuda());
        subComandosAyuda.put("deuda", new DeudaAyuda());
        subComandosAyuda.put("bolsa", new BolsaAyuda());
        subComandos.put("ayuda", subComandosAyuda);

        Map<String, SubComando> subComandosEmpresas = new HashMap<>();
        subComandosEmpresas.put("crear", new CrearEmpresas());
        subComandosEmpresas.put("depositar", new DepositarEmpresas());
        subComandosEmpresas.put("sacar", new SacarEmpresas());
        subComandosEmpresas.put("logotipo", new LogotipoEmpresas());
        subComandosEmpresas.put("vertodas", new VerTodasEmpresas());
        subComandosEmpresas.put("miempresa", new MiEmpresaEmpresas());
        subComandosEmpresas.put("contratar", new ContratarEmpresas());
        subComandosEmpresas.put("despedir", new DespedirEmpresas());
        subComandosEmpresas.put("borrar", new BorrarEmpresas());
        subComandosEmpresas.put("vender", new VenderEmpresas());
        subComandosEmpresas.put("editarempleado", new EditarEmpleadoEmpresas());
        subComandosEmpresas.put("editarnombre", new EditarNombreEmpresas());
        subComandosEmpresas.put("ayuda", new AyudaEmpresas());
        subComandosEmpresas.put("mercado", new MercadoEmpresas());
        subComandos.put("empresas", subComandosEmpresas);
    }
}
