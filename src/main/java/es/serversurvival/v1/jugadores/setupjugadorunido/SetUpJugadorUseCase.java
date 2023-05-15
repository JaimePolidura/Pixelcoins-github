package es.serversurvival.v1.jugadores.setupjugadorunido;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public final class SetUpJugadorUseCase {
    private final JugadoresService jugadoresService;
    private final EventBus eventBus;

    public void setUpJugadorUnido (Player player) {
        Optional<Jugador> jugadorOptional = jugadoresService.findById(player.getUniqueId());

        if(jugadorOptional.isEmpty()){
            Optional<Jugador> jugadorPorNombre = jugadoresService.findByNombre(player.getName());

            if(jugadorPorNombre.isEmpty()){
                jugadoresService.save(player.getUniqueId(), player.getName());
            }else{
                this.eventBus.publish(new JugadorCambiadoDeNombreEvento(player.getName(), player.getName()));
            }
        }else{
            Jugador jugador = jugadorOptional.get();

            if(!player.getName().equalsIgnoreCase(jugador.getNombre())){
                jugadoresService.save(jugador.withNombre(jugador.getNombre()));

                this.eventBus.publish(new JugadorCambiadoDeNombreEvento(jugador.getNombre(), player.getName()));
            }

            if(jugador.getNumeroVerificacionCuenta() == 0){
                jugadoresService.save(jugador.withNumeroCuenta(jugadoresService.generearNumeroCuenta()));
            }
        }
    }
}
