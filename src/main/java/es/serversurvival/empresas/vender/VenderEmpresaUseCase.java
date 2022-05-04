package es.serversurvival.empresas.vender;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.empleados._shared.mysql.Empleado;

public final class VenderEmpresaUseCase implements AllMySQLTablesInstances {
    public static final VenderEmpresaUseCase INSTANCE = new VenderEmpresaUseCase();

    public void vender (String vendedor, String comprador, double pixelcoins, String nombreEmpresa) {
        empresasMySQL.setOwner(nombreEmpresa, comprador);

        Empleado emplado = empleadosMySQL.getEmpleado(comprador, nombreEmpresa);
        if(emplado != null)
            empleadosMySQL.borrarEmplado(emplado.getId());

        Pixelcoin.publish(new EmpresaVendedidaEvento(comprador, vendedor, pixelcoins, nombreEmpresa));
    }
}
