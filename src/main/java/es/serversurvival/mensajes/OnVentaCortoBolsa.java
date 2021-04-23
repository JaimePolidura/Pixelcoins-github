package es.serversurvival.mensajes;

import es.jaime.EventListener;
import es.serversurvival.mySQL.eventos.bolsa.PosicionVentaCortoEvento;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static es.serversurvival.util.Funciones.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;
import static org.bukkit.ChatColor.GOLD;

public final class OnVentaCortoBolsa {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onVentaCortoBolsa (PosicionVentaCortoEvento evento) {
        Player player = Bukkit.getPlayer(evento.getComprador());

        enviarMensajeYSonido( player, GOLD + "Te has puesto corto en " + evento.getNombreValor() + " en " +
                evento.getNombreValor() + " cada una a " + GREEN + formatea.format(evento.getPrecioUnidad()) + " PC " +
                GOLD + "Para recomprar las acciones: /bolsa comprarcorto <id>. /bolsa cartera" + GOLD +
                "Ademas se te ha cobrado un 5% del valor total de la venta (" + GREEN  + formatea.format(evento.getPrecioTotal())
                + " PC" + GOLD + ") por lo cual: " + RED + "-" + formatea.format(evento.getPrecioTotal()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

        Bukkit.broadcastMessage(GOLD + player.getName() + " se ha puesto en corto en " + evento.getNombreValor());
    }
}
