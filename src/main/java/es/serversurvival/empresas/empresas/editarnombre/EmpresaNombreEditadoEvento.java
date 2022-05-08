package es.serversurvival.empresas.empresas.editarnombre;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmpresaNombreEditadoEvento extends PixelcoinsEvento {
    @Getter private final String antiguoNombre;
    @Getter private final String nuevoNombre;
}
