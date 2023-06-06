package es.serversurvival.v2.minecraftserver.empresas.editarempleado;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.v2.pixelcoins.empresas._shared.empleados.Empleado;
import es.serversurvival.v2.pixelcoins.empresas._shared.empleados.EmpleadosService;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.v2.pixelcoins.empresas.editarempleado.EditarEmpleadoParametros;
import es.serversurvival.v2.pixelcoins.empresas.editarempleado.EditarEmpleadoUseCase;
import es.serversurvival.v2.pixelcoins.jugadores._shared.jugadores.Jugador;
import es.serversurvival.v2.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Command(
        value = "empresas editarempleado",
        args = {"empresa", "empleado", "queSeEdita", "nuevoValor"},
        explanation = "'/empresas editarempleado <nombre de tu empresa> <nombre del empleado> sueldo 10' " +
                "'/empresas editarempleado <nombre de tu empresa> <nombre del empleado> cargo nuevoCargo'. " +
                "'/empresas editarempleado <nombre de tu empresa> <nombre del empleado> periodoPago 10 (10 expresado en dias)'"
)
@AllArgsConstructor
public final class EditarEmpleadoCommandRunner implements CommandRunnerArgs<EditarEmpleadoComando> {
    private final EditarEmpleadoUseCase editor;
    private final EmpleadosService empleadosService;
    private final JugadoresService jugadoresService;
    private final EmpresasService empresasService;

    @Override
    public void execute(EditarEmpleadoComando comando, Player player) {
        Empresa empresa = empresasService.getByNombre(comando.getEmpresa());
        Jugador jugador = jugadoresService.getByNombre(comando.getEmpleado());

        Empleado empleado = empleadosService.getEmpleoActivoByEmpresaIdAndEmpleadoJugadorId(empresa.getEmpresaId(), jugador.getJugadorId());

        EditarEmpleadoParametros.EditarEmpleadoParametrosBuilder editarEmpleadoBuilder = EditarEmpleadoParametros.builder()
                .empleadoIdEdtiar(empleado.getEmpleadoId())
                .jugadorId(player.getUniqueId())
                .nuevoSueldo(empleado.getSueldo())
                .nuevaDescripccion(empleado.getDescripccion())
                .nuevoPeriodoPago(empleado.getPeriodoPagoMs())
                .empresaId(empresa.getEmpresaId());

        switch (comando.getQueSeEdita().toLowerCase()) {
            case "sueldo" -> editor.editar(editarEmpleadoBuilder.nuevoSueldo(comando.nuevoValorToDouble()).build());
            case "cargo", "descipccion", "desc" -> editor.editar(editarEmpleadoBuilder.nuevaDescripccion(comando.getNuevoValor()).build());
            case "periodopago" -> editor.editar(editarEmpleadoBuilder.nuevoPeriodoPago(
                    comando.nuevoValorToLong() * 24 * 60 * 60 * 1000
            ).build());
        }

        player.sendMessage(ChatColor.GOLD + "Has editado el empleado");
    }
}
