package es.serversurvival.empresas.empresas.borrar;

import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.jugadores._shared.newformat.application.JugadoresService;

public final class BorrarEmpresaUseCase implements AllMySQLTablesInstances {
    private final EmpresasService empresasService;
    private final JugadoresService jugadoresService;

    public BorrarEmpresaUseCase() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    public void borrar (String owner, String empresaNombre) {
        Empresa empresaABorrar = this.empresasService.getEmpresaByNombre(empresaNombre);
        this.ensureOwner(empresaABorrar, owner);
        var jugadorOwner = jugadoresService.getJugadorByNombre(empresaABorrar.getOwner());

        this.empresasService.deleteByEmpresaId(empresaABorrar.getEmpresaId());
        this.jugadoresService.save(jugadorOwner.incrementPixelcoinsBy(empresaABorrar.getPixelcoins()));

        Pixelcoin.publish(new EmpresaBorradaEvento(owner, empresaNombre, empresaABorrar.getPixelcoins()));
    }

    private void ensureOwner(Empresa empresa, String ownerSupuesto){
        if(!empresa.getOwner().equals(ownerSupuesto))
            throw new NotTheOwner("No eres el owner de la empresa");
    }
}
