package es.serversurvival.bolsa._shared.ordenespremarket.eventlisteners;

import es.jaime.EventListener;
import es.serversurvival.bolsa._shared.OrdenNoAbiertaEvento;
import es.serversurvival._shared.utils.Funciones;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.*;

public final class OnOrdenNoAbierta {
    @EventListener
    public void on(OrdenNoAbiertaEvento evento) {
        Player player = Bukkit.getPlayer(evento.getPlayerName());

        Funciones.enviarMensajeYSonido(player, DARK_RED + "No puedes abrir dos ordenes de la misma posicionabierta", ENTITY_VILLAGER_NO);
    }
}
