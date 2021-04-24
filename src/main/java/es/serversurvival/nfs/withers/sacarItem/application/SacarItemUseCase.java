package es.serversurvival.nfs.withers.sacarItem.application;

import es.serversurvival.legacy.main.Pixelcoin;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.eventos.withers.ItemSacadoEvento;
import es.serversurvival.legacy.mySQL.tablasObjetos.Jugador;

public final class SacarItemUseCase implements AllMySQLTablesInstances {
    public void sacarItem(Jugador jugador, String itemNombre, int pixelcoinsPorItem) {
        jugadoresMySQL.setPixelcoin(jugador.getNombre(), jugador.getPixelcoins() - pixelcoinsPorItem);

        Pixelcoin.publish(new ItemSacadoEvento(jugador, itemNombre, pixelcoinsPorItem));
    }
}
