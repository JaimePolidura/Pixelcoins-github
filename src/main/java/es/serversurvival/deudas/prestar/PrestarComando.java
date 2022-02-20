package es.serversurvival.deudas.prestar;

import lombok.Getter;

public class PrestarComando {
    @Getter private String jugador;
    @Getter private int pixelcoins;
    @Getter private int dias;
    @Getter private int interes;
}
