package es.serversurvival.nfs.empresas.depositar;

import es.serversurvival.legacy.main.Pixelcoin;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.tablasObjetos.Empresa;
import es.serversurvival.legacy.mySQL.tablasObjetos.Jugador;

public final class DepositarPixelcoinsUseCase implements AllMySQLTablesInstances {
    public static final DepositarPixelcoinsUseCase INSTANCE = new DepositarPixelcoinsUseCase();

    private DepositarPixelcoinsUseCase() {}

    public void depositar (String empresa, String owner, double pixelcoins) {
        Jugador jugador = jugadoresMySQL.getJugador(owner);
        Empresa empresaADepositar = empresasMySQL.getEmpresa(empresa);
        double pixelcoinsEmpresa = empresaADepositar.getPixelcoins();

        empresasMySQL.setPixelcoins(empresa, pixelcoinsEmpresa + pixelcoins);

        Pixelcoin.publish(new PixelcoinsDepositadasEvento(jugador, empresaADepositar, pixelcoins));
    }
}
