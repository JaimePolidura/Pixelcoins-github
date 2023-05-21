package es.serversurvival.v2.pixelcoins.mensajes._shared.tipomensajes;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public enum TipoContenidoMensaje {
    INFO(ChatColor.DARK_AQUA, null),
    AVISO(ChatColor.YELLOW, Sound.ENTITY_VILLAGER_NO),
    INFLOW_PIXELCOINS(ChatColor.GREEN, Sound.ENTITY_PLAYER_LEVELUP),
    OUTFLOW_PIXELCOINS(ChatColor.RED, Sound.BLOCK_ANVIL_LAND);

    @Getter private final ChatColor color;
    @Getter private final Sound sound;

    TipoContenidoMensaje(ChatColor color, Sound sound) {
        this.color = color;
        this.sound = sound;
    }
}
