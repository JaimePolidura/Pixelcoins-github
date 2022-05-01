package es.serversurvival.jugadores.withers.sacarItem;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.jugadores._shared.newformat.application.JugadoresService;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class SacarItemUseCase implements AllMySQLTablesInstances {
    private final JugadoresService jugadoresService;
    public static final SacarItemUseCase INSTANCE = new SacarItemUseCase();

    public SacarItemUseCase () {
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    public void sacarItem(Jugador jugador, String itemNombre, int pixelcoinsPorItem) {
        jugadoresService.save(jugador.decrementPixelcoinsBy(pixelcoinsPorItem));

        Pixelcoin.publish(new ItemSacadoEvento(jugador, itemNombre, pixelcoinsPorItem));
    }
}
