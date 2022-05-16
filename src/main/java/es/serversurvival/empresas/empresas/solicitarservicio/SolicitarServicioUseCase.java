package es.serversurvival.empresas.empresas.solicitarservicio;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.*;

public final class SolicitarServicioUseCase {
    private final EmpresasService empresasService;
    private final EmpleadosService empleadosService;

    public SolicitarServicioUseCase() {
        this.empresasService = DependecyContainer.get(EmpresasService.class);
        this.empleadosService = DependecyContainer.get(EmpleadosService.class);
    }

    public void solicitar (String quienSolicita, String empresaNombre) {
        Empresa empresa = this.empresasService.getByNombre(empresaNombre);

        String mensajeOnline = ChatColor.GOLD + quienSolicita + " te ha solicitado el servicio de tu empresa: " + empresaNombre;
        String mensajeOffline = quienSolicita + " te ha solicitado el servicio de tu empresa: " + empresaNombre;

        enviarMensaje(empresa.getOwner(), mensajeOnline, mensajeOffline, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        List<Empleado> empleados = empleadosService.findByEmpresa(empresaNombre);
        empleados.forEach( empleado -> {
            enviarMensajeYSonidoSiOnline(empleado.getNombre(), ChatColor.GOLD + quienSolicita +
                    " te ha solicitado el servicio de la empresa: " + empresaNombre, Sound.ENTITY_PLAYER_LEVELUP);
        });
    }
}
