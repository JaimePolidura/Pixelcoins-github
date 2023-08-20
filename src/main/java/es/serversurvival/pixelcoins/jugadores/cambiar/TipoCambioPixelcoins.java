package es.serversurvival.pixelcoins.jugadores.cambiar;

import es.serversurvival.pixelcoins.config._shared.application.Configuration;
import es.serversurvival.pixelcoins.config._shared.domain.ConfigurationKey;
import lombok.Getter;

public enum TipoCambioPixelcoins {
    DIAMOND(ConfigurationKey.JUGADORES_CAMBIO_DIAMANTE, 1,"diamante"),
    DIAMOND_BLOCK(ConfigurationKey.JUGADORES_CAMBIO_DIAMANTE, 9, "bloque de diamante"),
    LAPIS_LAZULI(ConfigurationKey.JUGADORES_CAMBIO_LAPISLAZULI, 1,"lapislazuli"),
    LAPIS_BLOCK(ConfigurationKey.JUGADORES_CAMBIO_LAPISLAZULI, 9, "bloque de lapislazuli"),
    QUARTZ_BLOCK(ConfigurationKey.JUGADORES_CAMBIO_CUARZO, 1, "bloque de cuarzo");

    @Getter public final ConfigurationKey cambioConfigKey;
    @Getter public final int cantidad;
    @Getter public final String nombre;

    public int getCambio(Configuration configuration) {
        return (int) (configuration.getDouble(cambioConfigKey) * cantidad);
    }

    TipoCambioPixelcoins(ConfigurationKey cambioConfigKey, int cantidad, String nombre) {
        this.cambioConfigKey = cambioConfigKey;
        this.cantidad = cantidad;
        this.nombre = nombre;
    }
}
