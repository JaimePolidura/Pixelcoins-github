package es.serversurvival.empresas.empresas.pagarsueldostask;

import es.serversurvival.Pixelcoin;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empleados._shared.mysql.Empleado;
import es.serversurvival.empresas.empleados._shared.mysql.TipoSueldo;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.jugadores._shared.newformat.application.JugadoresService;
import lombok.SneakyThrows;

import java.util.Date;
import java.util.List;

public final class PagarSueldosUseCase implements AllMySQLTablesInstances {
    private final EmpresasService empresasService;
    private final JugadoresService jugadoresService;

    public PagarSueldosUseCase() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.jugadoresService = DependecyContainer.get(JugadoresService.class);
    }

    public void pagarSueldos (Empresa empresa, List<Empleado> empleados) {
        Date hoy = formatFehcaDeHoyException();

        for (Empleado empleado : empleados) {
            Date actualEmpl = formatFechaDeLaBaseDatosException(empleado.getFecha_ultimapaga());
            String tipoSueldo = empleado.getTipo_sueldo().codigo;

            int diferenciaDias = (int) Funciones.diferenciaDias(hoy, actualEmpl);

            if(TipoSueldo.dentroDeLosDias(tipoSueldo, diferenciaDias)){
                continue;
            }

            pagarSueldo(empleado, empresa);
        }
    }

    private void pagarSueldo (Empleado empleado, Empresa empresa)  {
        if (empresa.getPixelcoins() < empleado.getSueldo()) {
            Pixelcoin.publish(new ErrorPagandoSueldo(empleado.getJugador(), empresa.getNombre(), "No hay las suficientes pixelcoins"));
            return;
        }

        var jugadorEmpleado = this.jugadoresService.getJugadorByNombre(empleado.getJugador());

        empresasService.save(empresa.decrementPixelcoinsBy(empleado.getSueldo())
                .incrementGastosBy(empleado.getSueldo()) );
        this.jugadoresService.save(jugadorEmpleado.incrementPixelcoinsBy(empleado.getSueldo())
                .incrementIngresosBy(empleado.getSueldo()));

        Pixelcoin.publish(new SueldoPagadoEvento(empleado.getJugador(), empleado.getId(), empresa.getNombre(), empleado.getSueldo()));
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
