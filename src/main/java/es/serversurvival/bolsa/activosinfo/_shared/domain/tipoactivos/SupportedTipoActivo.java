package es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos;

import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.tipoactivos.acciones.AccionesService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.tipoactivos.criptomonedas.CriptomonedasService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.tipoactivos.materiasprimas.MateriasPrimasService;
import lombok.Getter;

public enum SupportedTipoActivo {
    CRIPTOMONEDAS(new CriptomonedasService(), "monedas"),
    MATERIAS_PRIMAS(new MateriasPrimasService(), "unidades"),
    ACCIONES(new AccionesService(), "acciones");

    @Getter private final TipoActivoService tipoActivoService;
    @Getter private final String alias;

    SupportedTipoActivo(TipoActivoService tipoActivoService, String alias) {
        this.tipoActivoService = tipoActivoService;
        this.alias = alias;
    }
}
