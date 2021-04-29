package es.serversurvival.jugadores.venderjugador;

import es.serversurvival.Pixelcoin;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class VenderJugadorUseCase implements AllMySQLTablesInstances{
    public static final VenderJugadorUseCase INSTANCE = new VenderJugadorUseCase();

    private VenderJugadorUseCase () {}

    public void vender (String comprador, String vendedor, double pixelcoins, String tipoItemVender) {
        jugadoresMySQL.realizarTransferenciaConEstadisticas(comprador, vendedor, pixelcoins);

        Pixelcoin.publish(new ItemVendidoJugadorEvento(comprador, vendedor, pixelcoins, tipoItemVender));
    }
}
