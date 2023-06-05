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
import org.bukkit.entity.Player;

@Command(
        value = "empresas editarempleado",
        args = {"empresa", "empleado", "queSeEditar", "nuevoValor"},
        explanation = "Editar un empleado que este en tu empresa. <queSeEditar> puede ser 'sueldo' ejemplo: '/empresas editarempleado empr empl sueldo 10'. " +
                "Tambien puede ser 'cargo' o 'descipccion', ejemplo: '/empresas editarempleado empr empl cargo jefe'. " +
                "Tambien puede ser 'periodopago' expresado en dias, ejemplo: /empresas editarempleado empr empl periodoPago 10'"
)
@AllArgsConstructor
public final class EditarEmpleadoCommandRunner implements CommandRunnerArgs<EditarEmpleadoComando> {
    private final EditarEmpleadoUseCase editarEmpleadoUseCase;
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

        switch (comando.getQueSeEditar().toLowerCase()) {
            case "sueldo" -> editarEmpleadoUseCase.editar(editarEmpleadoBuilder.nuevoSueldo(Double.parseDouble(comando.getNuevoValor())).build());
            case "cargo", "descipccion" -> editarEmpleadoUseCase.editar(editarEmpleadoBuilder.nuevaDescripccion(comando.getNuevoValor()).build());
            case "periodopago" -> editarEmpleadoUseCase.editar(editarEmpleadoBuilder.nuevoPeriodoPago(
                    Long.parseLong(comando.getNuevoValor()) * 24 * 60 * 60 * 1000
            ).build());
        }
    }
}
