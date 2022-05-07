package es.serversurvival.empresas.depositar;

import es.serversurvival.Pixelcoin;
import es.serversurvival.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores._shared.newformat.domain.Jugador;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;

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
