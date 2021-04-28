package es.serversurvival.empresas.sacar;

import es.serversurvival.Pixelcoin;
import es.serversurvival.empresas.mysql.Empresa;
import es.serversurvival.jugadores.mySQL.Jugador;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class SacarPixelcoinsUseCase implements AllMySQLTablesInstances {
    public static final SacarPixelcoinsUseCase INSTANCE = new SacarPixelcoinsUseCase();

    public void sacar (String jugador, String empresa, double pixelcoins) {
        Empresa empresaASacar = empresasMySQL.getEmpresa(empresa);
        Jugador jugadorQueSaca = jugadoresMySQL.getJugador(jugador);
        double pixelcoinsEmpresa = empresaASacar.getPixelcoins();

        empresasMySQL.setPixelcoins(empresa, pixelcoinsEmpresa - pixelcoins);

        Pixelcoin.publish(new PixelcoinsSacadasEvento(jugadorQueSaca, empresaASacar, pixelcoins));
    }
}
