package es.serversurvival.minecraftserver.empresas.despedir;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.pixelcoins.empresas._shared.empleados.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.pixelcoins.empresas.despedir.DespedirEmpleadoParametros;
import es.serversurvival.pixelcoins.empresas.despedir.DespedirEmpleadoUseCase;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.RequiredArgsConstructor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas despedir",
        args = {"empresa", "empleado", "...causaDespido"}
)
@RequiredArgsConstructor
public final class DespedirEmpleadoCommandRunner implements CommandRunnerArgs<DespedirEmpleadoComando> {
    private final DespedirEmpleadoUseCase despedirEmpleadoUseCase;
    private final JugadoresService jugadoresService;
    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;

    @Override
    public void execute(DespedirEmpleadoComando comando, Player player) {
        Empresa empresa = empresasService.getByNombre(comando.getEmpresa());
        UUID jugadorDespedirId = jugadoresService.getByNombre(comando.getEmpleado()).getJugadorId();
        Empleado empleado = empleadosService.getEmpleoActivoByEmpresaIdAndEmpleadoJugadorId(empresa.getEmpresaId(), jugadorDespedirId);

        despedirEmpleadoUseCase.despedir(DespedirEmpleadoParametros.builder()
                .empleadoIdADespedir(empleado.getEmpleadoId())
                .causa(comando.getCausaDespido())
                .empresaId(empresa.getEmpresaId())
                .jugadorId(player.getUniqueId())
                .build());

        player.sendMessage(GOLD + "Has despedido a: " + comando.getEmpleado() + " de la empresa " + comando.getEmpleado());
        MinecraftUtils.enviarMensajeYSonido(jugadorDespedirId, RED + "Has sido despedido de " + empresa + " por: " +
                comando.getCausaDespido(), Sound.BLOCK_ANVIL_LAND);
    }
}
