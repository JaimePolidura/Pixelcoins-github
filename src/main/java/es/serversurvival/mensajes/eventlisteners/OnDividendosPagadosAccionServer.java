package es.serversurvival.mensajes.eventlisteners;

import es.jaime.EventListener;
import es.dependencyinjector.annotations.Component;
import es.serversurvival.empresas.empresas.pagardividendos.eventos.EmpresaServerDividendoPagadoJugador;
import es.serversurvival._shared.utils.Funciones;

import java.text.DecimalFormat;

import static es.serversurvival._shared.utils.Funciones.enviarMensaje;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

@Component
public final class OnDividendosPagadosAccionServer {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onDividendosPagados (EmpresaServerDividendoPagadoJugador e) {
        String mensajeOnline = GOLD + "Has cobrado " + GREEN + "%s PC" + GOLD + " en dividendo de la empresa " + e.getEmpresa();
        String mensajeOffline = GOLD + "Has cobrado %s PC en dividendo de la empresa " + e.getEmpresa();

        enviarMensaje(e.getJugador(), String.format(mensajeOnline, formatea.format(e.getPixelcoins())), String.format(mensajeOffline, formatea.format(e.getPixelcoins())));
    }
}
