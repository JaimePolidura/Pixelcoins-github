package es.serversurvival.minecraftserver.empresas.emitiracciones;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.pixelcoins.empresas.emitiracciones.EmitirAccionesServerParametros;
import es.serversurvival.pixelcoins.empresas.emitiracciones.EmitirAccionesServerUseCase;
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
    private final EmitirAccionesServerUseCase emitirAccionesServerUseCase;
    private final EmpresasService empresasService;

    @Override
    public void execute(EmitirAccionesCommand command, Player player) {
        Empresa empresa = empresasService.getByNombre(command.getEmpresa());

        emitirAccionesServerUseCase.emitir(EmitirAccionesServerParametros.builder()
                .numeroNuevasAcciones(command.getNumeroNuevasAcciones())
                .precioPorAccion(command.getPrecioPorAccion())
                .jugadorId(player.getUniqueId())
                .empresaId(empresa.getEmpresaId())
                .build());

        player.sendMessage(GOLD + "Has emitido " + command.getNumeroNuevasAcciones() + " por " + GREEN +
                FORMATEA.format(command.getPrecioPorAccion()) + " PC " + GOLD + "Para ver las acciones " + AQUA + "/empresas mercado");
        Bukkit.broadcastMessage(GOLD + command.getEmpresa() + " ha emitodo " + command.getNumeroNuevasAcciones() + " por " + GREEN +
                FORMATEA.format(command.getPrecioPorAccion()) + " PC " + GOLD + "Para ver las acciones " + AQUA + "/empresas mercado");
    }
}
