package es.serversurvival.mensajes.eventlisteners;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.empresas.empresas.pagardividendos.eventos.EmpresaServerDividendoPagadoJugador;
import es.serversurvival.mensajes._shared.application.EnviadorMensajes;
import lombok.RequiredArgsConstructor;

import static es.serversurvival._shared.utils.Funciones.*;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

@EventHandler
@RequiredArgsConstructor
public final class OnDividendosPagadosAccionServer {
    private final EnviadorMensajes enviadorMensajes;

    @EventListener
    public void onDividendosPagados (EmpresaServerDividendoPagadoJugador e) {
        String mensajeOnline = GOLD + "Has cobrado " + GREEN + "%s PC" + GOLD + " en dividendo de la empresa " + e.getEmpresa();
        String mensajeOffline = GOLD + "Has cobrado %s PC en dividendo de la empresa " + e.getEmpresa();

        enviadorMensajes.enviarMensaje(e.getJugador(), String.format(mensajeOnline, FORMATEA.format(e.getPixelcoins())), String.format(mensajeOffline, FORMATEA.format(e.getPixelcoins())));
    }
}
