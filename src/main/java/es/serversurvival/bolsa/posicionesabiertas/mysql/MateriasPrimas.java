package es.serversurvival.bolsa.posicionesabiertas.mysql;

import es.serversurvival.bolsa.llamadasapi.mysql.TipoActivo;

import java.util.Optional;
import java.util.stream.Stream;

import static es.serversurvival.bolsa.llamadasapi.mysql.TipoActivo.*;

public enum MateriasPrimas {
    DJFUELUSGULF("Queroseno", MATERIAS_PRIMAS),
    DCOILBRENTEU("Petroleo", MATERIAS_PRIMAS),
    DHHNGSP("Gas natural", MATERIAS_PRIMAS),
    GASDESW("Diesel", MATERIAS_PRIMAS);

    public final String nombreValor;
    public final TipoActivo tipoActivo;

    MateriasPrimas(String nombreValor, TipoActivo tipoActivo) {
        this.nombreValor = nombreValor;
        this.tipoActivo = tipoActivo;
    }

    public static String getNombreValor (String simbolo) {
        Optional<MateriasPrimas> valor = Stream.of(MateriasPrimas.values())
                .filter(mat -> mat.nombreValor.equalsIgnoreCase(simbolo))
                .findAny();

        return valor.isPresent() ?
                valor.get().nombreValor :
                simbolo;
    }
}
