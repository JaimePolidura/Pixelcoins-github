package es.serversurvival.empresas.empresas.despedir;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.empresas.empleados.despedir.DespedirEmpleadoUseCase;
import es.serversurvival.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;

import static es.serversurvival._shared.utils.Funciones.*;

@Command(
        value = "empresas despedir",
        args = {"empresa", "nombreAccionista", "razon"},
        explanation = "Despedir a un nombreAccionista de tu empresa"
)
@AllArgsConstructor
public class DespedirEmpleadoComandoRunner implements CommandRunnerArgs<DespedirEmpleadoComando> {
    private final EnviadorMensajes enviadorMensajes;
    private final DespedirEmpleadoUseCase useCase;

    @Override
    public void execute(DespedirEmpleadoComando comando, CommandSender player) {
        String empresa = comando.getEmpresa();
        String jugador = comando.getJugador();
        String razon = comando.getRazon();

        useCase.despedir(player.getName(), jugador, empresa, razon);

        player.sendMessage(ChatColor.GOLD + "Has despedido a: " + jugador);
        String mensajeOnline = ChatColor.RED + "Has sido despedido de " + empresa + " razon: " + razon;
        enviadorMensajes.enviarMensaje(jugador, mensajeOnline, mensajeOnline, Sound.BLOCK_ANVIL_LAND, 10, 1);
    }
}
