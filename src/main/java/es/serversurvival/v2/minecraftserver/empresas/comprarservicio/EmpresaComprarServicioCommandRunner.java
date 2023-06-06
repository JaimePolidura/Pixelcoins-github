package es.serversurvival.v2.minecraftserver.empresas.comprarservicio;

import es.bukkitclassmapper.commands.Command;
import es.bukkitclassmapper.commands.commandrunners.CommandRunnerArgs;
import es.serversurvival.v2.pixelcoins.empresas._shared.empresas.EmpresasService;
import es.serversurvival.v2.pixelcoins.empresas.comprarservicio.ComprarServicioParametros;
import es.serversurvival.v2.pixelcoins.empresas.comprarservicio.ComprarServicioUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.UUID;

import static es.serversurvival.v1._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.*;

@Command(
        value = "empresas comprar",
        args = {"empresa", "precio"},
        explanation = "Hacer un pago a una empresa"
)
@AllArgsConstructor
public final class EmpresaComprarServicioCommandRunner implements CommandRunnerArgs<EmpresaComprarServicioComando> {
    private final ComprarServicioUseCase comprarServicioUseCase;
    private final EmpresasService empresasService;

    @Override
    public void execute(EmpresaComprarServicioComando comando, Player player) {
        UUID empresaId = empresasService.getByNombre(comando.getEmpresa()).getEmpresaId();

        comprarServicioUseCase.comprarServicio(ComprarServicioParametros.builder()
                .jugadorId(player.getUniqueId())
                .empresaId(empresaId)
                .pixelcoins(comando.getPrecio())
                .build());

        player.sendMessage(GOLD + "Has pagado a la empresa " + comando.getEmpresa() + " " + GREEN +
                FORMATEA.format(comando.getPrecio()) + " PC");
    }
}
