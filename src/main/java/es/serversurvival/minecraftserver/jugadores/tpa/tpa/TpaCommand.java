package es.serversurvival.minecraftserver.jugadores.tpa.tpa;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

@AllArgsConstructor
@NoArgsConstructor
public final class TpaCommand {
    @Getter private Player player;
}
