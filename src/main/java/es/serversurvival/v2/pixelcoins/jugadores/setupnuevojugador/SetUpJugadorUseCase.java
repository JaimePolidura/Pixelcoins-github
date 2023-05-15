package es.serversurvival.v2.pixelcoins.jugadores.setupnuevojugador;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.v2.pixelcoins.jugadores._shared.Jugador;
import es.serversurvival.v2.pixelcoins.jugadores._shared.JugadoresService;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public final class SetUpJugadorUseCase {
    private final JugadoresService jugadoresService;

    public void setUpJugadorUnido (Player player) {
        Optional<Jugador> jugadorOptional = jugadoresService.findById(player.getUniqueId());

        if(jugadorOptional.isEmpty()){
            jugadoresService.save(new Jugador(player.getUniqueId(), player.getName()));
        }else if(!jugadorOptional.get().getNombre().equalsIgnoreCase(player.getName())){
            jugadoresService.save(jugadorOptional.get().withNombre(player.getName()));
        }
    }
}
