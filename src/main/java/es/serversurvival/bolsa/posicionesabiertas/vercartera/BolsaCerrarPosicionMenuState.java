package es.serversurvival.bolsa.posicionesabiertas.vercartera;

import es.serversurvival.bolsa.activosinfo._shared.domain.ActivoInfo;
import es.serversurvival.bolsa.posicionesabiertas._shared.domain.PosicionAbierta;

public record BolsaCerrarPosicionMenuState(PosicionAbierta posicion, ActivoInfo activoInfo){}
