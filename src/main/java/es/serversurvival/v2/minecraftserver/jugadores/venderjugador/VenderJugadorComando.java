package es.serversurvival.v2.minecraftserver.jugadores.venderjugador;

import lombok.Getter;
import org.bukkit.entity.Player;

public class VenderJugadorComando {
    @Getter private Player comprador;
    @Getter private double pixelcoins;
}
