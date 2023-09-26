package es.serversurvival.minecraftserver.jugadores.fly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

@NoArgsConstructor
@AllArgsConstructor
public final class FlyCommand {
    @Getter private Player player;
}
