package es.serversurvival.empresas.empresas.borrar;

import es.jaime.EventBus;
import es.jaime.javaddd.domain.exceptions.NotTheOwner;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.accionistasserver._shared.application.AccionistasServerService;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class BorrarEmpresaUseCase {
    private final EmpresasService empresasService;
    private final JugadoresService jugadoresService;
    private final EmpleadosService empleadosService;
    private final AccionistasServerService accionistasEmpresasServerService;
    private final EventBus eventBus;

    public BorrarEmpresaUseCase() {
        this.accionistasEmpresasServerService = DependecyContainer.get(AccionistasServerService.class);
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public void borrar (String owner, String empresaNombre) {
        var empresaABorrar = this.empresasService.getByNombre(empresaNombre);
        var jugadorOwner = jugadoresService.getByNombre(owner);
        var empleados = this.empleadosService.findByEmpresa(empresaNombre);
        this.ensureOwner(empresaABorrar, owner);

        if(empresaABorrar.isCotizada()){
            this.accionistasEmpresasServerService.findByEmpresa(empresaNombre).forEach(accionista -> {
                double ownershipPercentaje = accionista.getCantidad() / empresaABorrar.getAccionesTotales();
                String jugadorNombre = accionista.esJugador() ? accionista.getNombreAccionista() : empresaABorrar.getOwner();
                var jugadorAPagar = jugadoresService.getByNombre(jugadorNombre);

                this.jugadoresService.save(jugadorAPagar.incrementPixelcoinsBy(ownershipPercentaje * empresaABorrar.getPixelcoins()));
            });
        }else{
            this.jugadoresService.save(jugadorOwner.incrementPixelcoinsBy(empresaABorrar.getPixelcoins()));
        }

        this.empresasService.deleteByEmpresaId(empresaABorrar.getEmpresaId());
        empleados.forEach(empleado -> {
            this.empleadosService.deleteById(empleado.getEmpleadoId());
        });

        this.eventBus.publish(new EmpresaBorrada(owner, empresaNombre, empresaABorrar.getPixelcoins(),
                empleados.stream().map(Empleado::getNombre).toList()));
    }

    private void ensureOwner(Empresa empresa, String ownerSupuesto){
        if(!empresa.getOwner().equals(ownerSupuesto))
            throw new NotTheOwner("No eres el owner de la empresa");
    }
}
