package es.serversurvival.minecraftserver.empresas.votaciones;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins.empresas._shared.EmpresasValidador;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "empresas votaciones",
        args = {"empresa"},
        explanation = "Ver las votaciones de una empresa de la que seas accionista"
)
@AllArgsConstructor
public final class VerVotacionesCommandRunner implements CommandRunnerArgs<VerVotacionesComando> {
    private final EmpresasValidador empresasValidador;
    private final EmpresasService empresasService;
    private final MenuService menuService;

    @Override
    public void execute(VerVotacionesComando comando, Player player) {
        Empresa empresa = empresasService.getByNombre(comando.getEmpresa());
        empresasValidador.accionistaDeEmpresa(empresa.getEmpresaId(), player.getUniqueId());
        empresasValidador.empresaCotizada(empresa.getEmpresaId());

        menuService.open(player, VerVotacionesEmpresaMenu.class, empresa);
    }
}
