package es.serversurvival.mensajes.eventlisteners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas.vender.EmpresaVendedida;
import es.serversurvival.mensajes._shared.application.MensajesService;
import lombok.AllArgsConstructor;

import java.util.List;

@EventHandler
@AllArgsConstructor
public final class OnEmpresaVendida {
    private final MensajesService mensajesService;
    private final EmpleadosService empleadosService;

    @EventListener
    public void onEmpresaVendida (EmpresaVendedida evento) {
        List<Empleado> empleados = empleadosService.findByEmpresa(evento.getEmpresaNombre());

        empleados.forEach(empleado -> {
            mensajesService.save(empleado.getNombre(), "La empresa en la que trabajas " + evento.getEmpresaNombre()
                    + " ha cambiado de owner a " + evento.getComprador());
        });
    }
}
