package es.serversurvival.nfs.empresas.tasks;

import es.serversurvival.nfs.Pixelcoin;
import es.serversurvival.legacy.mySQL.AllMySQLTablesInstances;
import es.serversurvival.legacy.mySQL.enums.TipoSueldo;
import es.serversurvival.nfs.empresas.mysql.Empresa;
import lombok.SneakyThrows;

import java.util.Date;
import java.util.List;

import static es.serversurvival.nfs.utils.Funciones.diferenciaDias;

public final class PagarSueldosUseCase implements AllMySQLTablesInstances {
    public static final PagarSueldosUseCase INSTANCE = new PagarSueldosUseCase();

    private PagarSueldosUseCase () {}

    public void pagarSueldos (Empresa empresa, List<Empleado> empleados) {
        Date hoy = formatFehcaDeHoyException();

        for (Empleado empleado : empleados) {
            Date actualEmpl = formatFechaDeLaBaseDatosException(empleado.getFecha_ultimapaga());
            String tipoSueldo = empleado.getTipo_sueldo().codigo;

            int diferenciaDias = (int) diferenciaDias(hoy, actualEmpl);

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
