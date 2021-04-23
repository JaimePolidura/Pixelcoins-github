package es.serversurvival.mensajes;

import es.jaime.EventListener;
import es.serversurvival.mySQL.eventos.tienda.ItemCompradoEvento;
import es.serversurvival.util.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;

import static es.serversurvival.util.Funciones.enviarMensajeYSonido;
import static es.serversurvival.util.Funciones.enviarMensajeYSonidoSiOnline;
import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GREEN;

public final class OnItemCompradoTienda {
    private final DecimalFormat formatea = Funciones.FORMATEA;

    @EventListener
    public void onItemCompradoTienda (ItemCompradoEvento evento) {
        Player player = Bukkit.getPlayer(evento.getComprador());

        enviarMensajeYSonido(player, GOLD + "Has comprado: " + evento.getObjeto() + " , por " + GREEN +
                formatea.format(evento.getPrecioUnidad()) + " PC", Sound.ENTITY_PLAYER_LEVELUP);

        enviarMensajeYSonidoSiOnline(evento.getVendedor(), GOLD + evento.getComprador() + " te ha comprado: " +
                evento.getObjeto() + " por: " + GREEN + formatea.format(evento.getPrecioUnidad()) + " PC ", Sound.ENTITY_PLAYER_LEVELUP);

    }
}
