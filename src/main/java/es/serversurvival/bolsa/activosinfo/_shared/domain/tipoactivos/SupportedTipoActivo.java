package es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.acciones.AccionesApiService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.criptomonedas.CriptomonedasApiService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.materiasprimas.MateriasPrimasApiService;
import es.serversurvival.bolsa.activosinfo._shared.infrastructure.tipoactivos.AccionesApiServiceIEXCloud;
import es.serversurvival.bolsa.activosinfo._shared.infrastructure.tipoactivos.CriptomonedasApiServiceIEXCloud;
import es.serversurvival.bolsa.activosinfo._shared.infrastructure.tipoactivos.MateriasPrimasApiServiceIEXCloud;
import lombok.Getter;

public enum SupportedTipoActivo {
    CRIPTOMONEDAS(DependecyContainer.get(CriptomonedasApiService.class), "monedas"),
    MATERIAS_PRIMAS(DependecyContainer.get(MateriasPrimasApiService.class), "unidades"),
    ACCIONES(DependecyContainer.get(AccionesApiService.class), "acciones");

    @Getter private final TipoActivoService tipoActivoService;
    @Getter private final String alias;

    SupportedTipoActivo(TipoActivoService tipoActivoService, String alias) {
        this.tipoActivoService = tipoActivoService;
        this.alias = alias;
    }
}
