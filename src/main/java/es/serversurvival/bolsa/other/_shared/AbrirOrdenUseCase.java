package es.serversurvival.bolsa.other._shared;

import es.serversurvival.Pixelcoin;
import es.serversurvival.bolsa.ordenespremarket._shared.domain.TipoAccion;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

public final class AbrirOrdenUseCase implements AllMySQLTablesInstances {
    public static final AbrirOrdenUseCase INSTANCE = new AbrirOrdenUseCase();

    private AbrirOrdenUseCase () {}

    public void abrirOrden(String playerName, String ticker, int cantidad, TipoAccion tipoOrden, int id_posicionabierta) {
        if(ordenesMySQL.ordenRegistrada(id_posicionabierta)){
            Pixelcoin.publish(new OrdenNoAbiertaEvento(playerName, ticker, cantidad, tipoOrden, id_posicionabierta));

            return;
        }

        ordenesMySQL.nuevaOrden(playerName, ticker, cantidad, tipoOrden, id_posicionabierta);

        Pixelcoin.publish(new OrdenAbiertaEvento(playerName, ticker, cantidad, tipoOrden, id_posicionabierta));
    }
}
