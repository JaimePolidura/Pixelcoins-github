package es.serversurvival.bolsa.posicionesabiertas.vercartera;

import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;

public record BolsaCerrarPosicionMenuState(PosicionAbierta posicion, ActivoInfo activoInfo){
    public static BolsaCerrarPosicionMenuState of(PosicionAbierta posicion, ActivoInfo activoInfo) {
        return new BolsaCerrarPosicionMenuState(posicion, activoInfo);
    }
}
