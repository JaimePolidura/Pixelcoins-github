package es.serversurvival.minecraftserver.deudas.emitir;

import es.bukkitbettermenus.MenuService;
import es.bukkitbettermenus.modules.sync.SyncMenuService;
import es.dependencyinjector.dependencies.annotations.Service;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.minecraftserver._shared.MinecraftUtils;
import es.serversurvival.minecraftserver.deudas.verdeudasmercado.MercadoDeudaMenu;
import es.serversurvival.minecraftserver.webaction.WebActionException;
import es.serversurvival.minecraftserver.webaction.WebActionHandler;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseBus;
import es.serversurvival.pixelcoins.deudas._shared.OfertaDeudaMercado;
import es.serversurvival.pixelcoins.deudas.emitir.EmitirDeudaParametros;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.Jugador;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.A;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static es.serversurvival.minecraftserver._shared.MinecraftUtils.*;
import static org.bukkit.ChatColor.*;

@Service
@AllArgsConstructor
public final class EmitirDeudaWebAcionHandler implements WebActionHandler<EmitirDeudaWebActionRequestBody> {
    private final JugadoresService jugadoresService;
    private final UseCaseBus useCaseBus;

    @Override
    public void handle(UUID jugadorId, EmitirDeudaWebActionRequestBody body) throws WebActionException {
        useCaseBus.handle(EmitirDeudaParametros.builder()
                .nominal(body.getPixelcoins())
                .interes(body.getInteres() / 100)
                .jugadorId(jugadorId)
                .numeroCuotasTotales(body.getNumeroCuotasTotales())
                .periodoPagoCuota(Funciones.diasToMillis(body.getPeriodoPagoCuotaEnDias()))
                .build());

        enviarMensajeYSonido(jugadorId, GOLD + "Has puesto la deuda en el mercado. " +
                AQUA + "/deudas mercado", Sound.ENTITY_PLAYER_LEVELUP);
        broadcastExcept(jugadorId, GOLD + jugadoresService.getNombreById(jugadorId) + " ha puesto deuda en el mercado. "
                + AQUA + "/deudas mercado");
    }
}
