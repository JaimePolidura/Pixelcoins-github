package es.serversurvival.empresas.empresas.pagarsueldostask;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empleados._shared.domain.TipoSueldo;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.jugadores._shared.application.JugadoresService;
import lombok.SneakyThrows;

import java.util.Date;
import java.util.List;

import static es.serversurvival._shared.utils.Funciones.*;

public final class PagarSueldosUseCase implements AllMySQLTablesInstances {
    private final EmpresasService empresasService;
    private final JugadoresService jugadoresService;
    private final EmpleadosService empleadosService;

    public PagarSueldosUseCase() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
    }

    public void pagarSueldos (Empresa empresa, List<Empleado> empleados) {
        Date hoy = formatFehcaDeHoyException();

        for (Empleado empleado : empleados) {
            Date actualEmpl = formatFechaDeLaBaseDatosException(empleado.getFechaUltimaPaga());
            String tipoSueldo = empleado.getTipoSueldo().codigo;

            int diferenciaDias = (int) diferenciaDias(hoy, actualEmpl);

            if(TipoSueldo.dentroDeLosDias(tipoSueldo, diferenciaDias)){
                continue;
            }

            pagarSueldo(empleado, empresa);
        }
    }

    private void pagarSueldo (Empleado empleado, Empresa empresa)  {
        if (empresa.getPixelcoins() < empleado.getSueldo()) {
            Pixelcoin.publish(new ErrorPagandoSueldo(empleado.getNombre(), empresa.getNombre(), "No hay las suficientes pixelcoins"));
            return;
        }

        var jugadorEmpleado = this.jugadoresService.getByNombre(empleado.getNombre());

        this.empresasService.save(empresa.decrementPixelcoinsBy(empleado.getSueldo())
                .incrementGastosBy(empleado.getSueldo()) );
        this.jugadoresService.save(jugadorEmpleado.incrementPixelcoinsBy(empleado.getSueldo())
                .incrementIngresosBy(empleado.getSueldo()));
        this.empleadosService.save(empleado.withFechaUltimaPaga(hoy()));

        Pixelcoin.publish(new SueldoPagadoEvento(empleado.getNombre(), empleado.getEmpleadoId(), empresa.getNombre(), empleado.getSueldo()));
    }

    @SneakyThrows
    private Date formatFechaDeLaBaseDatosException (String fecha) {
        return dateFormater.parse(fecha);
    }

    @SneakyThrows
    private Date formatFehcaDeHoyException () {
        return dateFormater.parse(dateFormater.format(new Date()));
    }
}
