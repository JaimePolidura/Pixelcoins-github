package es.serversurvival.empresas.vender;

import es.serversurvival.Pixelcoin;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;

public final class VenderEmpresaUseCase implements AllMySQLTablesInstances {
    public static final VenderEmpresaUseCase INSTANCE = new VenderEmpresaUseCase();

    public void vender (String vendedor, String comprador, double pixelcoins, String nombreEmpresa) {
        empresasMySQL.setOwner(nombreEmpresa, comprador);

        Pixelcoin.publish(new EmpresaVendedidaEvento(comprador, vendedor, pixelcoins, nombreEmpresa));
    }
}
