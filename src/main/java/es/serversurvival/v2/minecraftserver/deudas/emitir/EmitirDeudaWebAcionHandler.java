package es.serversurvival.v2.minecraftserver.deudas.emitir;

import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival.v2.minecraftserver.webaction.WebActionException;
import es.serversurvival.v2.minecraftserver.webaction.WebActionHandler;
import es.serversurvival.v2.pixelcoins.deudas.emitir.EmitirDeudaParametros;
import es.serversurvival.v2.pixelcoins.deudas.emitir.EmitirDeudaUseCase;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.util.UUID;

import static es.serversurvival.v2.minecraftserver._shared.MinecraftUtils.*;
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

        enviarMensajeYSonido(Bukkit.getPlayer(jugadorId), GOLD + "Has puesto la deuda en el mercado. Para verlo: " +
                AQUA + "/deudas mercado", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
