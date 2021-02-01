package es.serversurvival.mySQL.enums;

import es.serversurvival.mySQL.Transacciones;
import es.serversurvival.mySQL.tablasObjetos.Jugador;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public enum CambioPixelcoins {
    DIAMOND(CambioPixelcoins.DIAMANTE, Transacciones.INSTANCE::sacarMaxItemDiamond),
    DIAMOND_BLOCK(CambioPixelcoins.DIAMANTE * 9, Transacciones.INSTANCE::sacarMaxItemDiamond),
    LAPIS_LAZULI(CambioPixelcoins.LAPISLAZULI, Transacciones.INSTANCE::sacarMaxItemLapisLazuli),
    LAPIS_BLOCK(CambioPixelcoins.LAPISLAZULI * 9, Transacciones.INSTANCE::sacarMaxItemLapisLazuli),
    QUARTZ_BLOCK(CambioPixelcoins.CUARZO, Transacciones.INSTANCE::sacarMaxItemQuartzBlock);

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

    public static double sacarItem (Jugador jugador, String item) {
        return Transacciones.INSTANCE.sacarObjeto(jugador, item, CambioPixelcoins.getCambioTotal(item, 1));
    }

    public static boolean suficientesPixelcoins (String material, int cantidad, double pixelcoinsJugador) {
        return pixelcoinsJugador >= CambioPixelcoins.valueOf(material).cambio * cantidad;
    }

    public static void sacarMaxItem (String tipo, Jugador jugador, Player player) {
        CambioPixelcoins.valueOf(tipo).functionStrategy.sacar(jugador, player);
    }
    
    @FunctionalInterface
    private interface FunctionStrategy {
        void sacar (Jugador jugador, Player player);
    }
}
