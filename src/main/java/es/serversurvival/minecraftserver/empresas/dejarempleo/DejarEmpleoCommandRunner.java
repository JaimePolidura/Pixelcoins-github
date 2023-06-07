package es.serversurvival.minecraftserver.empresas.dejarempleo;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.pixelcoins.empresas.irseempleo.DejarEmpleoParametros;
import es.serversurvival.pixelcoins.empresas.irseempleo.DejarEmpleoUseCase;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "empresas dejarempleo",
        args = {"empresa"}
)
@RequiredArgsConstructor
public final class DejarEmpleoCommandRunner implements CommandRunnerArgs<DejarEmpleoComando> {
    private final DejarEmpleoUseCase dejarEmpleoUseCase;
    private final EmpresasService empresasService;

    @Override
    public void execute(DejarEmpleoComando comando, Player player) {
        Empresa empresa = empresasService.getByNombre(comando.getEmpresa());

        dejarEmpleoUseCase.dejar(new DejarEmpleoParametros(player.getUniqueId(), empresa.getEmpresaId()));
    }
}
