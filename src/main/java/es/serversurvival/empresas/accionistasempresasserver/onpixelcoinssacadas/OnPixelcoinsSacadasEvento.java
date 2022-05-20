package es.serversurvival.empresas.accionistasempresasserver.onpixelcoinssacadas;

import es.jaime.EventListener;
import es.serversurvival._shared.DependecyContainer;
import es.serversurvival._shared.utils.Funciones;
import es.serversurvival.empresas.accionistasempresasserver._shared.application.AccionistasEmpresasServerService;
import es.serversurvival.empresas.accionistasempresasserver._shared.domain.AccionEmpresaServer;
import es.serversurvival.empresas.empresas.sacar.PixelcoinsSacadasEvento;

import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static org.bukkit.ChatColor.*;

public final class OnPixelcoinsSacadasEvento {
    private final AccionistasEmpresasServerService accionistasEmpresasServerService;

    public OnPixelcoinsSacadasEvento() {
        this.accionistasEmpresasServerService = DependecyContainer.get(AccionistasEmpresasServerService.class);
    }

    @EventListener
    public void on(PixelcoinsSacadasEvento evento){
        String empresaNombre = evento.getEmpresaNombre();
        String mensajeOnline = format(GOLD + "La empresa %s de la que eres accionista el owner ha sacado %s pixelcoins para el", empresaNombre);
        String mensajeOffline = format(GOLD + "La empresa %s de la que eres accionista el owner ha sacado %s pixelcoins para el", empresaNombre);
        Set<String> jugadoresMensajesYaEnviados = new HashSet<>();

        this.accionistasEmpresasServerService.findByEmpresa(empresaNombre, AccionEmpresaServer::esJugador).forEach(accionista -> {
            String accionistaNombre = accionista.getNombreAccionista();

            if(!jugadoresMensajesYaEnviados.contains(accionistaNombre)){
                Funciones.enviarMensaje(accionistaNombre, mensajeOnline, mensajeOffline);

                jugadoresMensajesYaEnviados.add(accionistaNombre);
            }
        });
    }
}
