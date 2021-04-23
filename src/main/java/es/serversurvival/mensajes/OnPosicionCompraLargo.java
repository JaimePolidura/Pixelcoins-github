package es.serversurvival.mensajes;

import es.jaime.EventListener;
import es.serversurvival.mySQL.eventos.bolsa.PosicionCompraLargoEvento;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Sound;

import java.text.DecimalFormat;

import static es.serversurvival.util.Funciones.enviarMensajeYSonido;
import static org.bukkit.ChatColor.*;

public final class OnPosicionCompraLargo {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onCompraBolsaLargo (PosicionCompraLargoEvento evento) {
        Bukkit.broadcastMessage(GOLD + evento.getComprador() + " ha comprado " + evento.getCantidadPosicion() + " " + evento.getAlias()
                +  " de " + evento.getNombreValor() + " a " + GREEN + formatea.format(evento.getPrecioUnidad()) + "PC");


        enviarMensajeYSonido(Bukkit.getPlayer(evento.getComprador()), GOLD + "Has comprado " + formatea.format(evento.getCantidadPosicion())
                + " " + evento.getAlias() + " a " + GREEN + formatea.format(evento.getPrecioUnidad()) + " PC" + GOLD + " que es un total de " + GREEN +
                formatea.format(evento.getPrecioUnidad()) + " PC " + GOLD + " comandos: " + AQUA + "/bolsa vender /bolsa cartera", Sound.ENTITY_PLAYER_LEVELUP);
    }
}
