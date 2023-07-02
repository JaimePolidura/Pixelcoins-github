package es.serversurvival.minecraftserver.empresas.despedir;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empleados.domain.Empleado;
import es.serversurvival.pixelcoins.empresas._shared.empleados.application.EmpleadosService;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas.despedir.DespedirEmpleadoParametros;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import es.serversurvival.pixelcoins.mensajes._shared.application.EnviadorMensajes;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

@Command(
        value = "empresas despedir",
        args = {"empresa", "empleado", "...causaDespido"}
)
@RequiredArgsConstructor
public final class DespedirEmpleadoCommandRunner implements CommandRunnerArgs<DespedirEmpleadoComando> {
    private final JugadoresService jugadoresService;
    private final EnviadorMensajes enviadorMensajes;
    private final EmpleadosService empleadosService;
    private final EmpresasService empresasService;
    private final UseCaseBus useCaseBus;

    @Override
    public void execute(DespedirEmpleadoComando comando, Player player) {
        Empresa empresa = empresasService.getByNombre(comando.getEmpresa());
        UUID jugadorDespedirId = jugadoresService.getByNombre(comando.getEmpleado()).getJugadorId();
        Empleado empleado = empleadosService.getEmpleoActivoByEmpresaIdAndEmpleadoJugadorId(empresa.getEmpresaId(), jugadorDespedirId);

        useCaseBus.handle(DespedirEmpleadoParametros.builder()
                .empleadoIdADespedir(empleado.getEmpleadoId())
                .causa(comando.getCausaDespido())
                .empresaId(empresa.getEmpresaId())
                .jugadorId(player.getUniqueId())
                .build());

    }
}
