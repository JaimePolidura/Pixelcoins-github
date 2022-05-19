package es.serversurvival.empresas.empresas.ipo;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.empresas.empresas._shared.domain.Empresa;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public final class IPORealizada extends PixelcoinsEvento {
    @Getter private final Empresa empresa;
    @Getter private final double precioPorAccion;
    @Getter private final int accionesTotales;
    @Getter private final int accionesOwner;
}
