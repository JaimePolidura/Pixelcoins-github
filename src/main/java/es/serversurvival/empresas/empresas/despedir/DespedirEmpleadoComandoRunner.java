package es.serversurvival.empresas.empresas.despedir;

import es.jaimetruman.commands.Command;
import es.jaimetruman.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.empresas.empleados.despedir.DespedirEmpleadoUseCase;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

import static es.serversurvival._shared.utils.Funciones.*;

@Command(
        value = "empresas despedir",
        args = {"empresa", "nombreAccionista", "razon"},
        explanation = "Despedir a un nombreAccionista de tu empresa"
)
public class DespedirEmpleadoComandoRunner implements CommandRunnerArgs<DespedirEmpleadoComando> {
    private final DespedirEmpleadoUseCase useCase;

    public DespedirEmpleadoComandoRunner(){
        this.useCase = new DespedirEmpleadoUseCase();
    }

    @Override
    public void execute(DespedirEmpleadoComando comando, CommandSender player) {
        String empresa = comando.getEmpresa();
        String jugador = comando.getJugador();
        String razon = comando.getRazon();

        useCase.despedir(player.getName(), jugador, empresa, razon);

        player.sendMessage(ChatColor.GOLD + "Has despedido a: " + jugador);
        String mensajeOnline = ChatColor.RED + "Has sido despedido de " + empresa + " razon: " + razon;
        enviarMensaje(jugador, mensajeOnline, mensajeOnline, Sound.BLOCK_ANVIL_LAND, 10, 1);
    }
}
