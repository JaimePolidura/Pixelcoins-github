package es.serversurvival.jugadores.setupjugadorunido;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.jugadores._shared.newformat.application.JugadoresService;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import org.bukkit.entity.Player;

public final class SetUpJugadorUseCase {
    private final JugadoresService jugadoresService;

    public SetUpJugadorUseCase(){
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    public void setUpJugadorUnido (Player player) {
        Jugador jugadorPorUUID = jugadoresService.getJugadorById(player.getUniqueId());

        if(jugadorPorUUID == null){
            Jugador jugadorPorNombre = jugadoresService.getJugadorByNombre(player.getName());

            if(jugadorPorNombre == null){
                jugadoresService.save(player.getUniqueId(), player.getName());
            }
        }else{
            if(!player.getName().equalsIgnoreCase(jugadorPorUUID.getNombre())){
                jugadoresService.save(jugadorPorUUID.withNombre(jugadorPorUUID.getNombre()));

                Pixelcoin.publish(new JugadorCambiadoDeNombreEvento(jugadorPorUUID.getNombre(), player.getName()));
            }

            if(jugadorPorUUID.getNumeroVerificacionCuenta() == 0){
                jugadoresService.save(jugadorPorUUID.withNumeroCuenta(jugadoresService.generearNumeroCuenta()));
            }
        }
    }
}
