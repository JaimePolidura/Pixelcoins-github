package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.jaimetruman.annotations.Component;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas.editarnombre.EmpresaNombreEditadoEvento;
import es.serversurvival.mensajes._shared.application.MensajesService;

import java.util.List;

@Component
public final class OnNombreEmpresaEditado {
    private final MensajesService mensajesService;
    private final EmpleadosService empleadosService;

    public OnNombreEmpresaEditado(){
        this.mensajesService = DependecyContainer.get(MensajesService.class);
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
    }

    @EventListener
    public void onNombreEmpresaEditado (EmpresaNombreEditadoEvento evento) {
        List<Empleado> empleados = empleadosService.findByEmpresa(evento.getNuevoNombre());

        empleados.forEach(empleado -> {
            mensajesService.save(empleado.getNombre(), "La empresa en la que trabajas: " + evento.getAntiguoNombre() +
                    " ha cambiado a de nombre a " + evento.getNuevoNombre());
        });
    }
}
