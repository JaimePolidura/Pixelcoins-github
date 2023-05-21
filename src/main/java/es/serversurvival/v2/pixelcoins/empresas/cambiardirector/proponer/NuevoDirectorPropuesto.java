package es.serversurvival.v2.pixelcoins.empresas.cambiardirector.proponer;

import es.serversurvival.v1._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class NuevoDirectorPropuesto extends PixelcoinsEvento {
    @Getter private final ProponerNuevoDirectorParametros parametros;
}
