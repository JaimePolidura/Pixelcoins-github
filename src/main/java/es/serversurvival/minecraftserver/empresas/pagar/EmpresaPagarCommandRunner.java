package es.serversurvival.minecraftserver.empresas.pagar;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.empresas._shared.empresas.application.EmpresasService;
import es.serversurvival.pixelcoins.empresas.pagar.PagarEmpresaParametros;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas pagar",
        args = {"empresa", "precio"},
        explanation = "Hacer un pago a una empresa"
)
@AllArgsConstructor
public final class EmpresaPagarCommandRunner implements CommandRunnerArgs<EmpresaPagarComando> {
    private final EmpresasService empresasService;
    private final UseCaseBus useCaseBus;

    @Override
    public void execute(EmpresaPagarComando comando, Player player) {
        UUID empresaId = empresasService.getByNombre(comando.getEmpresa()).getEmpresaId();

        useCaseBus.handle(PagarEmpresaParametros.builder()
                .jugadorId(player.getUniqueId())
                .empresaId(empresaId)
                .pixelcoins(comando.getPrecio())
                .build());
    }
}
