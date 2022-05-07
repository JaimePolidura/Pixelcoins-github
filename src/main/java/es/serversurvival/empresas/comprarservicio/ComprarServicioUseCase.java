package es.serversurvival.empresas.comprarservicio;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.empresas._shared.domain.Empresa;

public final class ComprarServicioUseCase implements AllMySQLTablesInstances {
    public static final ComprarServicioUseCase INSTANCE = new ComprarServicioUseCase();

    private ComprarServicioUseCase () {}

    public Empresa comprar (String jugador, String empresa, double pixelcoins) {
        Empresa empresaAComprar = empresasMySQL.getEmpresa(empresa);

        empresasMySQL.setPixelcoins(empresa, empresaAComprar.getPixelcoins() + pixelcoins);
        empresasMySQL.setIngresos(empresa, empresaAComprar.getIngresos() + pixelcoins);

        Pixelcoin.publish(new EmpresaServicioCompradoEvento(jugador, empresaAComprar, pixelcoins));

        return empresaAComprar;
    }
}
