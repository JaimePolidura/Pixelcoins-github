package es.serversurvival.empresas.tasks;

import es.serversurvival.Pixelcoin;
import es.serversurvival.empleados.mysql.TipoSueldo;
import es.serversurvival.empresas.mysql.Empresa;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import es.serversurvival.utils.Funciones;
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
            mensajesMySQL.nuevoMensaje("", empleado.getJugador(), "No has podido cobrar tu sueldo por parte de " + empleado.getEmpresa() + " por que no tiene las suficientes pixelcoins");
            return;
        }

        empresasMySQL.setPixelcoins(empresa.getNombre(), empresa.getPixelcoins() - empleado.getSueldo());
        empresasMySQL.setGastos(empresa.getNombre(), empresa.getPixelcoins() + empleado.getSueldo());

        //TODO Desacoplar
        mensajesMySQL.nuevoMensaje("", empleado.getJugador(), "Has cobrado " + empleado.getSueldo() + " PC de parte de la empresa: " + empleado.getEmpresa());

        Pixelcoin.publish(new SueldoPagadoEvento(empresa.getNombre(), empleado.getId(), empresa.getNombre(), empleado.getSueldo()));
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
