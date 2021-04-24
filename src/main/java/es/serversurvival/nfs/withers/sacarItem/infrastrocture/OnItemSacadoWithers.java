package es.serversurvival.nfs.withers.sacarItem.infrastrocture;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.eventos.withers.ItemSacadoEvento;
import es.serversurvival.legacy.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static es.serversurvival.legacy.util.Funciones.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;

public final class OnItemSacadoWithers {

    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onItemSacadoWithers (ItemSacadoEvento evento) {
        Player player = Bukkit.getPlayer(evento.getJugador().getNombre());

        enviarMensajeYSonido(player, GOLD + "Has convertido las pixelcoins" + RED + "-" + evento.getPixelcoins() + " PC " + GOLD +
                "Quedan " + GREEN + formatea.format(evento.getJugador().getPixelcoins() - evento.getPixelcoins()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
