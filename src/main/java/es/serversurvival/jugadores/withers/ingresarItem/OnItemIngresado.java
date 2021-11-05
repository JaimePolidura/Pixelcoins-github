package es.serversurvival.jugadores.withers.ingresarItem;

import es.jaime.EventListener;
import es.serversurvival.shared.utils.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

public final class OnItemIngresado {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onItemIngresado (ItemIngresadoEvento evento) {
        Player player = Bukkit.getPlayer(evento.getJugador().getNombre());

        Funciones.enviarMensajeYSonido(player, GOLD + "Se ha a?adido: " + GREEN + formatea.format(evento.getPixelcoins())
                + GOLD + " Tienes " + formatea.format(evento.getPixelcoins() + evento.getJugador().getPixelcoins()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
