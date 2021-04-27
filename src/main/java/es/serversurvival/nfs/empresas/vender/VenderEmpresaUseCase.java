package es.serversurvival.nfs.empresas.vender;

import es.serversurvival.nfs.Pixelcoin;
import es.serversurvival.nfs.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.eventos.empresas.EmpresaVendidaEvento;

public final class VenderEmpresaUseCase implements AllMySQLTablesInstances {
    public static final VenderEmpresaUseCase INSTANCE = new VenderEmpresaUseCase();

    public void vender (String vendedor, String comprador, double pixelcoins, String nombreEmpresa) {
        empresasMySQL.setOwner(nombreEmpresa, comprador);

        //TODO desacoplar
        Jugadores.INSTANCE.realizarTransferenciaConEstadisticas(comprador, vendedor, pixelcoins);

        Pixelcoin.publish(new EmpresaVendidaEvento(comprador, vendedor, nombreEmpresa, pixelcoins));
    }
}
