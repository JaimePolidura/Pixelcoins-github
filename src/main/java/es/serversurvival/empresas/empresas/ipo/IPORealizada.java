package es.serversurvival.empresas.empresas.ipo;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public final class IPORealizada extends PixelcoinsEvento {
    @Getter private final String empresaNombre;
    @Getter private final double precioPorAccion;
    @Getter private final int accionesTotales;
    @Getter private final int accionesOwner;

    public static IPORealizada of(String empresaNombre, double precioPorAccion, int accionesTotales, int accionesOwner) {
        return new IPORealizada(empresaNombre, precioPorAccion, accionesTotales, accionesOwner);
    }
}
