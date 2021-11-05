package es.serversurvival.empresas.solicitarservicio;

import es.serversurvival.empleados.mysql.Empleado;
import es.serversurvival.empresas.mysql.Empresa;
import es.serversurvival.shared.mysql.AllMySQLTablesInstances;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.List;

import static es.serversurvival.shared.utils.Funciones.*;

public final class SolicitarServicioUseCase implements AllMySQLTablesInstances {
    public static final SolicitarServicioUseCase INSTANCE = new SolicitarServicioUseCase();

    private SolicitarServicioUseCase () {}

    public void solicitar (String quienSolicita, String empresaNombre) {
        Empresa empresa = empresasMySQL.getEmpresa(empresaNombre);

        String mensajeOnline = ChatColor.GOLD + quienSolicita + " te ha solicitado el servicio de tu empresa: " + empresaNombre;
        String mensajeOffline = quienSolicita + " te ha solicitado el servicio de tu empresa: " + empresaNombre;

        enviarMensaje(empresa.getOwner(), mensajeOnline, mensajeOffline, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        List<Empleado> empleados = empleadosMySQL.getEmpleadosEmrpesa(empresaNombre);
        empleados.forEach( empleado -> {
            enviarMensajeYSonidoSiOnline(empleado.getJugador(), ChatColor.GOLD + quienSolicita +
                    " te ha solicitado el servicio de la empresa: " + empresaNombre, Sound.ENTITY_PLAYER_LEVELUP);
        });
    }
}
