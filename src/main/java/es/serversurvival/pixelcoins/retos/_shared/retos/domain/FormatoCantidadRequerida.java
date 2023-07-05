package es.serversurvival.pixelcoins.retos._shared.retos.domain;

import es.serversurvival._shared.utils.Funciones;

import java.util.function.Function;

public enum FormatoCantidadRequerida {
    NUMERO(Funciones::formatNumero),
    PIXELCOINS(Funciones::formatPixelcoins),
    PORCENTAJE_RESULTADO(Funciones::formatRentabilidad);

    private final Function<Double, String> formateador;

    FormatoCantidadRequerida(Function<Double, String> formateador) {
        this.formateador = formateador;
    }

    public String formatear(Double sinFormatear) {
        return formateador.apply(sinFormatear);
    }
}
