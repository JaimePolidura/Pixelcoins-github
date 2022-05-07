package es.serversurvival.empresas._shared.application.tasks;

import es.serversurvival.Pixelcoin;
import es.serversurvival.empleados._shared.mysql.Empleado;
import es.serversurvival.empleados._shared.mysql.TipoSueldo;
import es.serversurvival.empresas._shared.domain.Empresa;
import es.serversurvival._shared.mysql.AllMySQLTablesInstances;
import es.serversurvival._shared.utils.Funciones;
import lombok.SneakyThrows;

import java.util.Date;
import java.util.List;

public final class PagarSueldosUseCase implements AllMySQLTablesInstances {
    public static final PagarSueldosUseCase INSTANCE = new PagarSueldosUseCase();

    private PagarSueldosUseCase () {}

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

        empresasMySQL.setPixelcoins(empresa.getNombre(), empresa.getPixelcoins() - empleado.getSueldo());
        empresasMySQL.setGastos(empresa.getNombre(), empresa.getPixelcoins() + empleado.getSueldo());

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
