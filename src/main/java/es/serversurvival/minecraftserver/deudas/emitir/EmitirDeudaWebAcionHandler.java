package es.serversurvival.minecraftserver.deudas.emitir;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver.webaction.WebActionException;
import es.serversurvival.minecraftserver.webaction.WebActionHandler;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.deudas.emitir.EmitirDeudaParametros;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.util.UUID;

import static org.bukkit.ChatColor.*;

@Service
@AllArgsConstructor
public final class EmitirDeudaWebAcionHandler implements WebActionHandler<EmitirDeudaWebActionRequestBody> {
    private final UseCaseBus useCaseBus;

    @Override
    public void handle(UUID jugadorId, EmitirDeudaWebActionRequestBody body) throws WebActionException {
        useCaseBus.handle(EmitirDeudaParametros.builder()
                .nominal(body.getNominal())
                .interes(body.getInteres())
                .jugadorId(jugadorId)
                .numeroCuotasTotales(body.getNumeroCuotasTotales())
                .periodoPagoCuota(body.getPeriodoPagoCuotaEnSegundos())
                .build());

        MinecraftUtils.enviarMensajeYSonido(Bukkit.getPlayer(jugadorId), GOLD + "Has puesto la deuda en el mercado. Para verlo: " +
                AQUA + "/deudas mercado", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
