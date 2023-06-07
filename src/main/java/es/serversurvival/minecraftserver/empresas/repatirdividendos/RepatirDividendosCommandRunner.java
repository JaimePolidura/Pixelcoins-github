package es.serversurvival.minecraftserver.empresas.repatirdividendos;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.EmpresasService;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "empresas repatirdividendos",
        args = {"empresa"},
        explanation = "Repatir dividendos entre los accionistas de la empresa"
)
@AllArgsConstructor
public final class RepatirDividendosCommandRunner implements CommandRunnerArgs<RepatirDividendosComando> {
    private final EmpresasValidador empresasValidador;
    private final EmpresasService empresasService;
    private final MenuService menuService;

    @Override
    public void execute(RepatirDividendosComando comando, Player player) {
        Empresa empresa = empresasService.getByNombre(comando.getEmpresa());
        empresasValidador.directorEmpresa(empresa.getEmpresaId(), player.getUniqueId());

        menuService.open(player, RepartirDividendosConfirmacionMenu.class, empresa);
    }
}
