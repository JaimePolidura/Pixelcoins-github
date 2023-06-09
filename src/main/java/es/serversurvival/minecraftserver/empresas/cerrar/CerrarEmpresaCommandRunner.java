package es.serversurvival.minecraftserver.empresas.cerrar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.pixelcoins.empresas.cerrar.CerrarEmpresaParametros;
import es.serversurvival.pixelcoins.empresas.cerrar.CerrarEmpresaUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Command(
        value = "empresas cerrar",
        args = {"empresa"},
        explanation = "Cerrar una empresa que tengas"
)
@AllArgsConstructor
public final class CerrarEmpresaCommandRunner implements CommandRunnerArgs<CerrarEmpresaComando> {
    private final EmpresasService empresasService;
    private final UseCaseBus useCaseBus;

    @Override
    public void execute(CerrarEmpresaComando comando, Player player) {
        Empresa empresa = empresasService.getByNombre(comando.getEmpresa());

        useCaseBus.handle(new CerrarEmpresaParametros(empresa.getEmpresaId(), player.getUniqueId()));

        player.sendMessage(ChatColor.GOLD + "Has cerrado la empresa");
    }
}
