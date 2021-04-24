package es.serversurvival.legacy.mensajes;

import es.jaime.EventListener;
import es.serversurvival.nfs.empresas.sacar.PixelcoinsSacadasEvento;
import es.serversurvival.legacy.mySQL.tablasObjetos.Empresa;
import es.serversurvival.legacy.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static es.serversurvival.legacy.util.Funciones.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;

public final class OnPixelcoinsSacadasEmpresa {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onPixelcoinsSacadas (PixelcoinsSacadasEvento evento) {
        Player player = Bukkit.getPlayer(evento.getJugador().getNombre());
        Empresa empresa = evento.getEmpresa();

        enviarMensajeYSonido(player, GOLD + "Has sacado " + GREEN + formatea.format(evento.getPixelcoins())
                + " PC" + GOLD + " de tu empresa: " + DARK_AQUA + empresa.getNombre() + GOLD + " ahora tiene: " + GREEN
                + formatea.format(empresa.getPixelcoins() - evento.getPixelcoins()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
