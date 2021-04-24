package es.serversurvival.legacy.mensajes;

import es.jaime.EventListener;
import es.serversurvival.legacy.mySQL.eventos.jugadores.JugadorPagoManualEvento;
import es.serversurvival.legacy.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static es.serversurvival.legacy.util.Funciones.enviarMensaje;
import static org.bukkit.ChatColor.*;

public final class OnPagoRealizadoManual  {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onPagoRealizado (JugadorPagoManualEvento evento) {
        Player pagador = Bukkit.getPlayer(evento.getPagador());

        pagador.sendMessage(GOLD + "Has pagado: " + GREEN + formatea.format(evento.getCantidad()) + " PC " + GOLD + "a " + evento.getPagado());

        String mensajeSiEstaOnline = GOLD + evento.getPagador() + " te ha pagado: " + GREEN + "+" + formatea.format(evento.getCantidad()) + " PC " + AQUA + "(/estadisticas)";

        enviarMensaje(evento.getPagado(), mensajeSiEstaOnline, mensajeSiEstaOnline, Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
    }
}
