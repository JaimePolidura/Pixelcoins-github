package es.serversurvival.minecraftserver.empresas.sacar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas.sacar.SacarPixelcoinsEmpresaParametros;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "empresas sacar",
        args = {"empresa", "pixelcoins"},
        explanation = "Sacar pixelcoinsd de tu empresa"
)
@RequiredArgsConstructor
public final class SacarPixelcoinsEmpresaCommandRunner implements CommandRunnerArgs<SacarPixelcoinsEmpresaComando> {
    private final EmpresasService empresasService;
    private final UseCaseBus useCaseBus;

    @Override
    public void execute(SacarPixelcoinsEmpresaComando comando, Player player) {
        Empresa empresa = empresasService.getByNombre(comando.getEmpresa());

        useCaseBus.handle(SacarPixelcoinsEmpresaParametros.builder()
                .empresaId(empresa.getEmpresaId())
                .jugadorId(player.getUniqueId())
                .pixelcoins(comando.getPixelcoins())
                .build());
    }
}
