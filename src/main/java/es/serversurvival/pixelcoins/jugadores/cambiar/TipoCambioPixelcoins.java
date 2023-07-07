package es.serversurvival.pixelcoins.jugadores.cambiar;

import lombok.Getter;

public enum TipoCambioPixelcoins {
    DIAMOND(TipoCambioPixelcoins.DIAMANTE, "diamante"),
    DIAMOND_BLOCK(TipoCambioPixelcoins.DIAMANTE * 9, "bloque de diamante"),
    LAPIS_LAZULI(TipoCambioPixelcoins.LAPISLAZULI, "lapislazuli"),
    LAPIS_BLOCK(TipoCambioPixelcoins.LAPISLAZULI * 9, "bloque de lapislazuli"),
    QUARTZ_BLOCK(TipoCambioPixelcoins.CUARZO, "bloque de cuarzo");

    public static final int DIAMANTE = 250;
    public static final int CUARZO = 25;
    public static final int LAPISLAZULI = 10;

    @Getter public final int cambio;
    @Getter public final String nombre;

    TipoCambioPixelcoins(int cambio, String nombre) {
        this.cambio = cambio;
        this.nombre = nombre;
    }
}
