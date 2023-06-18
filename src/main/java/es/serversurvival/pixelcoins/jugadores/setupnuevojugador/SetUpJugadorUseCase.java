package es.serversurvival.pixelcoins.jugadores.setupnuevojugador;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.pixelcoins._shared.usecases.UseCaseHandler;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.application.JugadoresEstadisticasService;
import es.serversurvival.pixelcoins.jugadores._shared.estadisticas.domain.JugadorEstadisticas;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.Jugador;
import es.serversurvival.pixelcoins.jugadores._shared.jugadores.JugadoresService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public final class SetUpJugadorUseCase {
    private final JugadoresEstadisticasService jugadoresEstadisticasService;
    private final JugadoresService jugadoresService;

    public void setUpJugadorUnido (Player player) {
        Optional<Jugador> jugadorOptional = jugadoresService.findById(player.getUniqueId());

        if(jugadorOptional.isEmpty()){
            jugadoresService.save(new Jugador(player.getUniqueId(), player.getName(), LocalDateTime.now()));
            jugadoresEstadisticasService.save(JugadorEstadisticas.empty(player.getUniqueId()));
        }else if(!jugadorOptional.get().getNombre().equalsIgnoreCase(player.getName())){
            jugadoresService.save(jugadorOptional.get().withNombre(player.getName()));
        }
    }
}
