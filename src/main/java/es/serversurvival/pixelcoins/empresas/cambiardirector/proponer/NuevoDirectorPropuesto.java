package es.serversurvival.pixelcoins.empresas.cambiardirector.proponer;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class NuevoDirectorPropuesto extends PixelcoinsEvento {
    @Getter private final ProponerNuevoDirectorParametros parametros;
}
