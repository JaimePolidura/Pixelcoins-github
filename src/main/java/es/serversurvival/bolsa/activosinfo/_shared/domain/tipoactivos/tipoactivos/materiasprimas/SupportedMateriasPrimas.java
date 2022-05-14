package es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.tipoactivos.materiasprimas;

import java.util.Optional;
import java.util.stream.Stream;

import static es.serversurvival.bolsa.activosinfo._shared.domain.TipoActivo.MATERIAS_PRIMAS;

public enum SupportedMateriasPrimas {
    DJFUELUSGULF("Queroseno", MATERIAS_PRIMAS),
    DCOILBRENTEU("Petroleo", MATERIAS_PRIMAS),
    DHHNGSP("Gas natural", MATERIAS_PRIMAS),
    GASDESW("Diesel", MATERIAS_PRIMAS);

    public final String nombreActivoLargo;
    public final TipoActivo tipoActivo;

    SupportedMateriasPrimas(String nombreActivoLargo, TipoActivo tipoActivo) {
        this.nombreActivoLargo = nombreActivoLargo;
        this.tipoActivo = tipoActivo;
    }

    public static String getNombreActivoLargo(String nombreActivo) {
        Optional<SupportedMateriasPrimas> valor = Stream.of(SupportedMateriasPrimas.values())
                .filter(mat -> mat.nombreActivoLargo.equalsIgnoreCase(nombreActivo))
                .findAny();

        return valor.isPresent() ?
                valor.get().nombreActivoLargo :
                nombreActivo;
    }
}
