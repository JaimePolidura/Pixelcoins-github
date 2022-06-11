package es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos;

import es.serversurvival._shared.DependecyContainer;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.acciones.AccionesApiService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.criptomonedas.CriptomonedasApiService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.materiasprimas.MateriasPrimasApiService;
import lombok.Getter;

public enum TipoActivo {
    CRIPTOMONEDAS(DependecyContainer.get(CriptomonedasApiService.class), "monedas"),
    MATERIAS_PRIMAS(DependecyContainer.get(MateriasPrimasApiService.class), "unidades"),
    ACCIONES(DependecyContainer.get(AccionesApiService.class), "acciones");

    @Getter private final TipoActivoService tipoActivoService;
    @Getter private final String alias;

    TipoActivo(TipoActivoService tipoActivoService, String alias) {
        this.tipoActivoService = tipoActivoService;
        this.alias = alias;
    }

    public synchronized double getPrecio(String nombreActivo) throws Exception {
        return this.getTipoActivoService().getPrecio(nombreActivo);
    }
}
