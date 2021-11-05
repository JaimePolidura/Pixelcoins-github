package es.serversurvival.jugadores.withers.sacarItem;

import es.serversurvival.Pixelcoin;
import es.serversurvival.jugadores._shared.mySQL.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class SacarItemUseCase implements AllMySQLTablesInstances {
    public static final SacarItemUseCase INSTANCE = new SacarItemUseCase();

    private SacarItemUseCase () {}

    public void sacarItem(Jugador jugador, String itemNombre, int pixelcoinsPorItem) {
        jugadoresMySQL.setPixelcoin(jugador.getNombre(), jugador.getPixelcoins() - pixelcoinsPorItem);

        Pixelcoin.publish(new ItemSacadoEvento(jugador, itemNombre, pixelcoinsPorItem));
    }
}
