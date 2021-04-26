package es.serversurvival.legacy.mensajes;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.eventos.withers.ItemSacadoMaxEvento;
import es.serversurvival.nfs.utils.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static es.serversurvival.nfs.utils.Funciones.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;

public final class OnItemSacadaMaxWithers {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onItemSacadoMax (ItemSacadoMaxEvento evento) {
        Player player = Bukkit.getPlayer(evento.getJugador().getNombre());

        enviarMensajeYSonido(player, GOLD + "Has sacado: " + GREEN + formatea.format(evento.getPixelcoins())
                + GOLD + " ahora te quedan: " + GREEN + formatea.format(evento.getJugador().getPixelcoins() - evento.getPixelcoins())
                + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
