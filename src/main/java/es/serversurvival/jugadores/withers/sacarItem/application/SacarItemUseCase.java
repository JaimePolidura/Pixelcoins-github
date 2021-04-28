package es.serversurvival.jugadores.withers.sacarItem.application;

import es.serversurvival.Pixelcoin;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.jugadores.withers.sacarItem.ItemSacadoEvento;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class SacarItemUseCase implements AllMySQLTablesInstances {
    public void sacarItem(Jugador jugador, String itemNombre, int pixelcoinsPorItem) {
        jugadoresMySQL.setPixelcoin(jugador.getNombre(), jugador.getPixelcoins() - pixelcoinsPorItem);

        Pixelcoin.publish(new ItemSacadoEvento(jugador, itemNombre, pixelcoinsPorItem));
    }
}
