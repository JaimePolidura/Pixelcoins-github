package es.serversurvival.deudas.prestar;

import lombok.Getter;
import org.bukkit.entity.Player;

public class PrestarComando {
    @Getter private Player jugador;
    @Getter private int pixelcoins;
    @Getter private int dias;
    @Getter private int interes;
}
