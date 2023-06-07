package es.serversurvival.pixelcoins.empresas.ipo;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class EmpresaIPORealizada extends PixelcoinsEvento {
    @Getter private final EmpresaIPOParametros empresaIPOParametros;
}
