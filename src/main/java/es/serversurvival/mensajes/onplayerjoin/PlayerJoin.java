package es.serversurvival.mensajes.onplayerjoin;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.mensajes._shared.application.MensajesService;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerJoin implements Listener {
    private final MensajesService mensajesService;

    public PlayerJoin(){
        this.mensajesService = DependecyContainer.get(MensajesService.class);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent evento) {
        Player player = evento.getPlayer();

        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Tienes " + this.mensajesService.findMensajesByDestinatario(player.getName()).size() +
                " pendientes " + ChatColor.AQUA + "  /mensajes");
    }
}
