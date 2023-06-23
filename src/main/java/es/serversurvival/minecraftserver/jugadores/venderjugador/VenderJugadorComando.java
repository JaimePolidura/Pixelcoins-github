package es.serversurvival.minecraftserver.jugadores.venderjugador;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;

@NoArgsConstructor
@AllArgsConstructor
public class VenderJugadorComando {
    @Getter private Player jugador;
    @Getter private double pixelcoins;
}
