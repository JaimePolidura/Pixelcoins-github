package es.serversurvival.legacy.mensajes;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.eventos.withers.ItemIngresadoEvento;
import es.serversurvival.nfs.utils.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static es.serversurvival.nfs.utils.Funciones.enviarMensajeYSonido;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

public final class OnItemIngresadoWithers {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onItemIngresado (ItemIngresadoEvento evento) {
        Player player = Bukkit.getPlayer(evento.getJugador().getNombre());

        enviarMensajeYSonido(player, GOLD + "Se ha a?adido: " + GREEN + formatea.format(evento.getPixelcoins())
                + GOLD + " Tienes " + formatea.format(evento.getPixelcoins() + evento.getJugador().getPixelcoins()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
