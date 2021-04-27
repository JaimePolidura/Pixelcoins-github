package es.serversurvival.nfs.empresas.sacar;

import es.serversurvival.nfs.Pixelcoin;
import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.nfs.empresas.mysql.Empresa;
import es.serversurvival.nfs.jugadores.mySQL.Jugador;

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
