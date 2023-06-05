package es.serversurvival.v2.minecraftserver.empresas.cerrar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.Empresa;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.v2.pixelcoins.empresas.cerrar.CerrarEmpresaParametros;
import es.serversurvival.v2.pixelcoins.empresas.cerrar.CerrarEmpresaUseCase;
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
    private final CerrarEmpresaUseCase cerrarEmpresaUseCase;
    private final EmpresasService empresasService;

    @Override
    public void execute(CerrarEmpresaComando comando, Player player) {
        Empresa empresa = empresasService.getByNombre(comando.getEmpresa());

        cerrarEmpresaUseCase.cerrar(new CerrarEmpresaParametros(empresa.getEmpresaId(), player.getUniqueId()));

        player.sendMessage(ChatColor.GOLD + "Has cerrado la empresa");
    }
}
