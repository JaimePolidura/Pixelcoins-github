package es.serversurvival.nfs.empresas.crear;

import es.serversurvival.legacy.main.Pixelcoin;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;

public final class CrearEmpresaUseCase implements AllMySQLTablesInstances {
    public static final CrearEmpresaUseCase INSTANCE = new CrearEmpresaUseCase();

    private CrearEmpresaUseCase () {}

    public void crear (String jugador, String nombreEmpresa, String descripcion) {
        empresasMySQL.nuevaEmpresa(nombreEmpresa, jugador, 0, 0, 0, "DIAMOND_PICKAXE", descripcion);

        Pixelcoin.publish(new EmpresaCreadaEvento(jugador, nombreEmpresa));
    }
}
