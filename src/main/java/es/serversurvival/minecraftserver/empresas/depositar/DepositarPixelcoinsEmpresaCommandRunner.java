package es.serversurvival.minecraftserver.empresas.depositar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas.depositar.DepositarPixelcoinsEmpresaParametros;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "empresas depositar",
        args = {"empresa", "pixelcoins"},
        explanation = "Poner pixelcoins de tuyas a una empresa"
)
@AllArgsConstructor
public final class DepositarPixelcoinsEmpresaCommandRunner implements CommandRunnerArgs<DepositarPixelcoinsEmpresaComando> {
    private final EmpresasService empresasService;
    private final UseCaseBus useCaseBus;

    @Override
    public void execute(DepositarPixelcoinsEmpresaComando comando, Player player) {
        Empresa empresa = empresasService.getByNombre(comando.getEmpresa());

        useCaseBus.handle(DepositarPixelcoinsEmpresaParametros.builder()
                .empresaId(empresa.getEmpresaId())
                .jugadorId(player.getUniqueId())
                .pixelcoins(comando.getPixelcoins())
                .build());
    }
}
