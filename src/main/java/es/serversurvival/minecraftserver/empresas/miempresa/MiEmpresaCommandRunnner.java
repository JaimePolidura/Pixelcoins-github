package es.serversurvival.minecraftserver.empresas.miempresa;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "empresas miempresa",
        args = {"empresa"},
        explanation = "Ver los datos de tu empresa"
)
@RequiredArgsConstructor
public final class MiEmpresaCommandRunnner implements CommandRunnerArgs<MiEmpresaComando> {
    private final EmpresasValidador empresasValidador;
    private final EmpresasService empresasService;
    private final MenuService menuService;

    @Override
    public void execute(MiEmpresaComando miEmpresaComando, Player player) {
        Empresa empresa = empresasService.getByNombre(miEmpresaComando.getEmpresa());
        empresasValidador.directorEmpresa(empresa.getEmpresaId(), player.getUniqueId());
        empresasValidador.empresaNoCerrada(empresa.getEmpresaId());

        menuService.open(player, MiEmpresaMenu.class, empresa);
    }
}
