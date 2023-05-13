package es.serversurvival.mensajes.eventlisteners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas.editarnombre.EmpresaNombreEditadoEvento;
import es.serversurvival.mensajes._shared.application.MensajesService;
import lombok.AllArgsConstructor;

import java.util.List;

@EventHandler
@AllArgsConstructor
public final class OnNombreEmpresaEditado {
    private final MensajesService mensajesService;
    private final EmpleadosService empleadosService;

    @EventListener
    public void onNombreEmpresaEditado (EmpresaNombreEditadoEvento evento) {
        List<Empleado> empleados = empleadosService.findByEmpresa(evento.getNuevoNombre());

        empleados.forEach(empleado -> {
            mensajesService.save(empleado.getNombre(), "La empresa en la que trabajas: " + evento.getAntiguoNombre() +
                    " ha cambiado a de nombre a " + evento.getNuevoNombre());
        });
    }
}
