package es.serversurvival.v1.jugadores.cambio.ingresarItem;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.jaime.EventBus;
import es.serversurvival.v1.jugadores._shared.application.JugadoresService;
import es.serversurvival.v1.jugadores._shared.domain.Jugador;
import es.serversurvival.v1.jugadores.cambio.TipoCambioPixelcoins;
import lombok.AllArgsConstructor;

@UseCase
@AllArgsConstructor
public final class IngresarItemUseCase {
    private final JugadoresService jugadoresService;
    private final EventBus eventBus;

    public void ingresarItem(String nombrJugador, TipoCambioPixelcoins tipoCambio, int cantidad) {
        Jugador jugador = jugadoresService.getByNombre(nombrJugador);

        double pixelcoinsAnadir = tipoCambio.cambio * cantidad;

        this.jugadoresService.save(jugador.incrementPixelcoinsBy(pixelcoinsAnadir));

        this.eventBus.publish(new ItemIngresadoEvento(jugador, pixelcoinsAnadir, tipoCambio.name()));
    }
}
