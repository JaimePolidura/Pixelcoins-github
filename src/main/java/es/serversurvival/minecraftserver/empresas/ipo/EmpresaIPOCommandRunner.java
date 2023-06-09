package es.serversurvival.minecraftserver.empresas.ipo;

import es.bukkitbettermenus.MenuService;
import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins.empresas._shared.empresas.domain.Empresa;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas.ipo.EmpresaIPOParametros;
import es.serversurvival.pixelcoins.empresas.ipo.EmpresasIPOUseCase;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@Command(
        value = "empresas ipo",
        args = {"empresa", "numeroAccionesEnPropiedadAVender", "precioPorAccion"}
)
@RequiredArgsConstructor
public final class EmpresaIPOCommandRunner implements CommandRunnerArgs<EmpresaIPOComando> {
    private final EmpresasIPOUseCase empresasIPOUseCase;
    private final EmpresasService empresasService;
    private final MenuService menuService;

    @Override
    public void execute(EmpresaIPOComando comando, Player player) {
        Empresa empresa = empresasService.getByNombre(comando.getEmpresa());
        empresasIPOUseCase.validar(EmpresaIPOParametros.builder()
                        .empresaId(empresa.getEmpresaId())
                        .jugadorId(player.getUniqueId())
                        .numeroAccionesVender(comando.getNumeroAccionesEnPropiedadAVender())
                        .precioPorAccion(comando.getPrecioPorAccion())
                .build());

        this.menuService.open(player, ConfirmarIPOMenu.class, ConfirmarIPOMenu.ConfirmarIPOMenuState.from(empresa,
                comando.getPrecioPorAccion(), comando.getNumeroAccionesEnPropiedadAVender()));
    }
}
