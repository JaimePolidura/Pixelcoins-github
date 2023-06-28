package es.serversurvival.minecraftserver.empresas.dejarempleo;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas.irseempleo.DejarEmpleoParametros;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.GOLD;

@Command(
        value = "empresas dejarempleo",
        args = {"empresa"}
)
@RequiredArgsConstructor
public final class DejarEmpleoCommandRunner implements CommandRunnerArgs<DejarEmpleoComando> {
    private final EmpresasService empresasService;
    private final UseCaseBus useCaseBus;

    @Override
    public void execute(DejarEmpleoComando comando, Player player) {
        Empresa empresa = empresasService.getByNombre(comando.getEmpresa());

        useCaseBus.handle(new DejarEmpleoParametros(player.getUniqueId(), empresa.getEmpresaId()));

        player.sendMessage(GOLD + "Te has ido del trabajo");
    }
}
