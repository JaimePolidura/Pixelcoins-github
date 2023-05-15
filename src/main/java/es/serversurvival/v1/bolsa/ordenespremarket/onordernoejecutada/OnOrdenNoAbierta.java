package es.serversurvival.v1.bolsa.ordenespremarket.onordernoejecutada;

import es.dependencyinjector.dependencies.annotations.EventHandler;
import es.jaime.EventListener;
import es.serversurvival.v1.bolsa.ordenespremarket.abrirorden.OrdenNoAbiertaEvento;
import es.serversurvival.v1.mensajes._shared.application.EnviadorMensajes;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static org.bukkit.ChatColor.*;
import static org.bukkit.Sound.*;

@EventHandler
@AllArgsConstructor
public final class OnOrdenNoAbierta {
    private final EnviadorMensajes enviadorMensajes;

    @EventListener
    public void on(OrdenNoAbiertaEvento evento) {
        Player player = Bukkit.getPlayer(evento.getPlayerName());

        enviadorMensajes.enviarMensajeYSonido(player, DARK_RED + "No puedes abrir dos ordenes de la misma posicionabierta", ENTITY_VILLAGER_NO);
    }
}
