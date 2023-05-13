package es.serversurvival.empresas.empresas.solicitarservicio;

import es.dependencyinjector.dependencies.annotations.UseCase;
import es.serversurvival.empresas.empleados._shared.application.EmpleadosService;
import es.serversurvival.empresas.empleados._shared.domain.Empleado;
import es.serversurvival.empresas.empresas._shared.application.EmpresasService;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import es.serversurvival.mensajes._shared.application.EnviadorMensajes;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

import java.util.List;

import static es.serversurvival._shared.utils.Funciones.*;

@UseCase
@RequiredArgsConstructor
public final class SolicitarServicioUseCase {
    private final EmpleadosService empleadosService;
    private final EnviadorMensajes enviadorMensajes;
    private final EmpresasService empresasService;

    public void solicitar (String quienSolicita, String empresaNombre) {
        Empresa empresa = this.empresasService.getByNombre(empresaNombre);

        String mensajeOnline = ChatColor.GOLD + quienSolicita + " te ha solicitado el servicio de tu empresa: " + empresaNombre;
        String mensajeOffline = quienSolicita + " te ha solicitado el servicio de tu empresa: " + empresaNombre;

        enviadorMensajes.enviarMensaje(empresa.getOwner(), mensajeOnline, mensajeOffline, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);

        empleadosService.findByEmpresa(empresaNombre).forEach( empleado -> {
            enviadorMensajes.enviarMensajeYSonidoSiOnline(empleado.getNombre(), ChatColor.GOLD + quienSolicita +
                    " te ha solicitado el servicio de la empresa: " + empresaNombre, Sound.ENTITY_PLAYER_LEVELUP);
        });
    }
}
