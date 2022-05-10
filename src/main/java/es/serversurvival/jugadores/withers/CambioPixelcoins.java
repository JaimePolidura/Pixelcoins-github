package es.serversurvival.jugadores.withers;

import es.serversurvival.jugadores._shared.domain.Jugador;
import es.serversurvival.jugadores.withers.sacarItem.SacarItemUseCase;
import es.serversurvival.jugadores.withers.sacarMaxItem.SacarMaxItemUseCase;

public enum CambioPixelcoins {
    DIAMOND(CambioPixelcoins.DIAMANTE, SacarMaxItemUseCase.INSTANCE::sacarMaxItemDiamond),
    DIAMOND_BLOCK(CambioPixelcoins.DIAMANTE * 9, SacarMaxItemUseCase.INSTANCE::sacarMaxItemDiamond),
    LAPIS_LAZULI(CambioPixelcoins.LAPISLAZULI, SacarMaxItemUseCase.INSTANCE::sacarMaxItemLapisLazuli),
    LAPIS_BLOCK(CambioPixelcoins.LAPISLAZULI * 9, SacarMaxItemUseCase.INSTANCE::sacarMaxItemLapisLazuli),
    QUARTZ_BLOCK(CambioPixelcoins.CUARZO, SacarMaxItemUseCase.INSTANCE::sacarMaxItemQuartzBlock);

    public static final int DIAMANTE = 290;
    public static final int CUARZO = 12;
    public static final int LAPISLAZULI = 5;

    public final int cambio;
    private final FunctionStrategy functionStrategy;

    CambioPixelcoins(int cambio, FunctionStrategy functionStrategy) {
        this.cambio = cambio;
        this.functionStrategy = functionStrategy;
    }

    public static int getCambioTotal (String nombreItem, int cantidad) {
        return CambioPixelcoins.valueOf(nombreItem).cambio * cantidad;
    }

    public static void sacarItem (Jugador jugador, String item) {
        SacarItemUseCase.INSTANCE.sacarItem(jugador, item, CambioPixelcoins.getCambioTotal(item, 1));
    }

    public static boolean suficientesPixelcoins (String material, int cantidad, double pixelcoinsJugador) {
        return pixelcoinsJugador >= CambioPixelcoins.valueOf(material).cambio * cantidad;
    }

    public static void sacarMaxItem (String tipo, Jugador jugador, String playerName) {
        CambioPixelcoins.valueOf(tipo).functionStrategy.sacar(jugador, playerName);
    }
    
    @FunctionalInterface
    private interface FunctionStrategy {
        void sacar (Jugador jugador, String playerName);
    }
}
