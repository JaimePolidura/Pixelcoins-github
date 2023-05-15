package es.serversurvival.v2.pixelcoins.jugadores.cambiar;

public enum TipoCambioPixelcoins {
    DIAMOND(TipoCambioPixelcoins.DIAMANTE),
    DIAMOND_BLOCK(TipoCambioPixelcoins.DIAMANTE * 9),
    LAPIS_LAZULI(TipoCambioPixelcoins.LAPISLAZULI),
    LAPIS_BLOCK(TipoCambioPixelcoins.LAPISLAZULI * 9),
    QUARTZ_BLOCK(TipoCambioPixelcoins.CUARZO);

    public static final int DIAMANTE = 290;
    public static final int CUARZO = 12;
    public static final int LAPISLAZULI = 5;

    public final int cambio;

    TipoCambioPixelcoins(int cambio) {
        this.cambio = cambio;
    }

    public static int getCambioTotal (String nombreItem, int cantidad) {
        return TipoCambioPixelcoins.valueOf(nombreItem).cambio * cantidad;
    }
}
