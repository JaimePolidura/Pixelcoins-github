package es.serversurvival.empresas.empresas.pagarsueldostask;

import es.jaime.EventBus;
import es.dependencyinjector.annotations.UseCase;
import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empleados._shared.domain.TipoSueldo;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.Date;
import java.util.List;

import static es.serversurvival._shared.utils.Funciones.*;

@AllArgsConstructor
@UseCase
public final class PagarSueldosUseCase {
    private final EmpresasService empresasService;
    private final JugadoresService jugadoresService;
    private final EmpleadosService empleadosService;
    private final EventBus eventBus;

    public PagarSueldosUseCase() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
        this.eventBus = DependecyContainer.get(EventBus.class);
    }

    public void pagarSueldos (Empresa empresa, List<Empleado> empleados) {
        Date hoy = formatFehcaDeHoyException();

        for (Empleado empleado : empleados) {
            Date actualEmpl = formatFechaDeLaBaseDatosException(empleado.getFechaUltimaPaga());
            String tipoSueldo = empleado.getTipoSueldo().codigo;
            int diferenciaDias = (int) diferenciaDias(hoy, actualEmpl);

            if(!TipoSueldo.dentroDeLosDias(tipoSueldo, diferenciaDias)){
                pagarSueldo(empleado, empresa.getNombre());
            }
        }
    }

    private void pagarSueldo (Empleado empleado, String empresaNombre)  {
        var empresa = this.empresasService.getByNombre(empresaNombre);

        if (empresa.getPixelcoins() < empleado.getSueldo()) {
            this.eventBus.publish(new ErrorPagandoSueldo(empleado.getNombre(), empresa.getNombre(), "No hay las suficientes pixelcoins"));
            return;
        }

        var jugadorEmpleado = this.jugadoresService.getByNombre(empleado.getNombre());

        this.empresasService.save(empresa.decrementPixelcoinsBy(empleado.getSueldo())
                .incrementGastosBy(empleado.getSueldo()) );
        this.jugadoresService.save(jugadorEmpleado.incrementPixelcoinsBy(empleado.getSueldo())
                .incrementIngresosBy(empleado.getSueldo()));
        this.empleadosService.save(empleado.withFechaUltimaPaga(hoy()));

        this.eventBus.publish(new SueldoPagadoEvento(empleado.getNombre(), empleado.getEmpleadoId(), empresa.getNombre(), empleado.getSueldo()));
    }

    @SneakyThrows
    private Date formatFechaDeLaBaseDatosException (String fecha) {
        return DATE_FORMATER_LEGACY.parse(fecha);
    }

    @SneakyThrows
    private Date formatFehcaDeHoyException () {
        return DATE_FORMATER_LEGACY.parse(DATE_FORMATER_LEGACY.format(new Date()));
    }
}
