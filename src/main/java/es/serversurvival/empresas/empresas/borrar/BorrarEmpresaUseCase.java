package es.serversurvival.empresas.empresas.borrar;

import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.accionistasempresasserver._shared.application.AccionistasEmpresasServerService;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionEmpresaServer;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores._shared.application.JugadoresService;

public final class BorrarEmpresaUseCase {
    private final EmpresasService empresasService;
    private final JugadoresService jugadoresService;
    private final EmpleadosService empleadosService;
    private final AccionistasEmpresasServerService accionistasEmpresasServerService;

    public BorrarEmpresaUseCase() {
        this.accionistasEmpresasServerService = DependecyContainer.get(AccionistasEmpresasServerService.class);
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
    }

    public void borrar (String owner, String empresaNombre) {
        var empresaABorrar = this.empresasService.getByNombre(empresaNombre);
        var jugadorOwner = jugadoresService.getByNombre(empresaABorrar.getOwner());
        var empleados = this.empleadosService.findByEmpresa(empresaNombre);
        this.ensureOwner(empresaABorrar, owner);

        if(empresaABorrar.isCotizada()){
            this.accionistasEmpresasServerService.findByEmpresa(empresaNombre, AccionEmpresaServer::esJugador).forEach(accionista -> {
                var jugadorAccionista = this.jugadoresService.getByNombre(accionista.getNombreAccionista());
                double ownershipPercentaje = accionista.getCantidad() / empresaABorrar.getAccionesTotales();

                this.jugadoresService.save(jugadorAccionista.incrementPixelcoinsBy(ownershipPercentaje * empresaABorrar.getPixelcoins()));
            });
        }else{
            this.jugadoresService.save(jugadorOwner.incrementPixelcoinsBy(empresaABorrar.getPixelcoins()));
        }

        this.empresasService.deleteByEmpresaId(empresaABorrar.getEmpresaId());
        this.empleadosService.findByEmpresa(empresaNombre).forEach(empleado -> {
            this.empleadosService.deleteById(empleado.getEmpleadoId());
        });

        Pixelcoin.publish(new EmpresaBorradaEvento(owner, empresaNombre, empresaABorrar.getPixelcoins(),
                empleados.stream().map(Empleado::getNombre).toList()));
    }

    private void ensureOwner(Empresa empresa, String ownerSupuesto){
        if(!empresa.getOwner().equals(ownerSupuesto))
            throw new NotTheOwner("No eres el owner de la empresa");
    }
}
