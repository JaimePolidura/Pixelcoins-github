package es.serversurvival.v1.jugadores.pagar;

import lombok.Getter;
import org.bukkit.entity.Player;

public class PagarComando {
    @Getter private Player destino;
    @Getter private double pixelcoins;
}
