package es.serversurvival.pixelcoins.mensajes._shared.domain;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Sound;

public enum TipoMensaje {
    INFO(ChatColor.DARK_AQUA),
    AVISO(ChatColor.YELLOW),
    INFLOW_PIXELCOINS(ChatColor.GREEN),
    OUTFLOW_PIXELCOINS(ChatColor.RED);

    @Getter private final ChatColor color;

    TipoMensaje(ChatColor color) {
        this.color = color;
    }
}
