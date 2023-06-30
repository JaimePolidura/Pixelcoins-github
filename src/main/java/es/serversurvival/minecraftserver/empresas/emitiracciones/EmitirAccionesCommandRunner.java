package es.serversurvival.minecraftserver.empresas.emitiracciones;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas.emitiracciones.EmitirAccionesServerParametros;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas emitiracciones",
        args = {"empresa", "numeroNuevasAcciones", "precioPorAccion"}
)
@RequiredArgsConstructor
public final class EmitirAccionesCommandRunner implements CommandRunnerArgs<EmitirAccionesCommand> {
    private final EmpresasService empresasService;
    private final UseCaseBus useCaseBus;

    @Override
    public void execute(EmitirAccionesCommand command, Player player) {
        Empresa empresa = empresasService.getByNombre(command.getEmpresa());

        useCaseBus.handle(EmitirAccionesServerParametros.builder()
                .numeroNuevasAcciones(command.getNumeroNuevasAcciones())
                .precioPorAccion(command.getPrecioPorAccion())
                .jugadorId(player.getUniqueId())
                .empresaId(empresa.getEmpresaId())
                .build());

        player.sendMessage(GOLD + "Has emitido " + command.getNumeroNuevasAcciones() + " por " +
                formatPixelcoins(command.getPrecioPorAccion()) + "Para ver las acciones " + AQUA + "/empresas mercado");
        MinecraftUtils.broadcastExcept(player, GOLD + command.getEmpresa() + " ha emitodo " + command.getNumeroNuevasAcciones() + " por " +
                formatPixelcoins(command.getPrecioPorAccion()) + "Para ver las acciones " + AQUA + "/empresas mercado");
    }
}
