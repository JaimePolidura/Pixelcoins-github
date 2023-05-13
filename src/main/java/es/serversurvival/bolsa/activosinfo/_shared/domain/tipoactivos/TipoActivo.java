package es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos;

import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.acciones.AccionesApiService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.criptomonedas.CriptomonedasApiService;
import es.serversurvival.bolsa.activosinfo._shared.domain.tipoactivos.materiasprimas.MateriasPrimasApiService;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionesAbiertasRepository;
import lombok.Getter;

public enum TipoActivo {
    CRIPTOMONEDAS(CriptomonedasApiService.class, "monedas"),
    MATERIAS_PRIMAS(MateriasPrimasApiService.class, "unidades"),
    ACCIONES(AccionesApiService.class, "cantidad");

    @Getter private final Class<? extends TipoActivoService> tipoActivoService;
    @Getter private final String alias;

    TipoActivo(Class<? extends TipoActivoService> tipoActivoService, String alias) {
        this.tipoActivoService = tipoActivoService;
        this.alias = alias;
    }
}
