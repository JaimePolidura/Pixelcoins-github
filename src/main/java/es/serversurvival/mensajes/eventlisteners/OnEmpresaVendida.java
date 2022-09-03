package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.dependencyinjector.annotations.Component;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas.vender.EmpresaVendedida;
import es.serversurvival.mensajes._shared.application.MensajesService;

import java.util.List;

@Component
public final class OnEmpresaVendida {
    private final MensajesService mensajesService;
    private final EmpleadosService empleadosService;

    public OnEmpresaVendida(){
        this.mensajesService = DependecyContainer.get(MensajesService.class);
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
    }

    @EventListener
    public void onEmpresaVendida (EmpresaVendedida evento) {
        List<Empleado> empleados = empleadosService.findByEmpresa(evento.getEmpresaNombre());

        empleados.forEach(empleado -> {
            mensajesService.save(empleado.getNombre(), "La empresa en la que trabajas " + evento.getEmpresaNombre()
                    + " ha cambiado de owner a " + evento.getComprador());
        });
    }
}
