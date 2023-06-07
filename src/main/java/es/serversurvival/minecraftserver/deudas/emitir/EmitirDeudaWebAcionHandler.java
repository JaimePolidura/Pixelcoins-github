package es.serversurvival.minecraftserver.deudas.emitir;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver.webaction.WebActionException;
import es.serversurvival.minecraftserver.webaction.WebActionHandler;
import es.serversurvival.pixelcoins.deudas.emitir.EmitirDeudaParametros;
import es.serversurvival.pixelcoins.deudas.emitir.EmitirDeudaUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.util.UUID;

import static org.bukkit.ChatColor.*;

@Service
@AllArgsConstructor
public final class EmitirDeudaWebAcionHandler implements WebActionHandler<EmitirDeudaWebActionRequestBody> {
    private final EmitirDeudaUseCase emitirDeudaUseCase;

    @Override
    public void handle(UUID jugadorId, EmitirDeudaWebActionRequestBody body) throws WebActionException {
        emitirDeudaUseCase.emitir(EmitirDeudaParametros.builder()
                .nominal(body.getNominal())
                .interes(body.getInteres())
                .jugadorId(jugadorId)
                .numeroCuotasTotales(body.getNumeroCuotasTotales())
                .periodoPagoCuota(body.getPeriodoPagoCuota())
                .build());

        MinecraftUtils.enviarMensajeYSonido(Bukkit.getPlayer(jugadorId), GOLD + "Has puesto la deuda en el mercado. Para verlo: " +
                AQUA + "/deudas mercado", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
