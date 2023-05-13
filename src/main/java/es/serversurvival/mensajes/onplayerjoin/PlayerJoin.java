package es.serversurvival.mensajes.onplayerjoin;

import es.serversurvival.mensajes._shared.application.MensajesService;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

@AllArgsConstructor
@es.dependencyinjector.dependencies.annotations.EventHandler
public final class PlayerJoin implements Listener {
    private final MensajesService mensajesService;
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evento) {
        Player player = evento.getPlayer();

        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Tienes " + this.mensajesService.findMensajesByDestinatario(player.getName()).size() +
                " pendientes " + ChatColor.AQUA + "  /mensajes");
    }
}
