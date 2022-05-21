package es.serversurvival.empresas.ofertasaccionesserver.venderofertaaccionaserver;

import es.serversurvival._shared.eventospixelcoins.PixelcoinsEvento;
import es.serversurvival.empresas.accionistasserver._shared.domain.TipoAccionista;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public final class NuevaOfertaAccionServer extends PixelcoinsEvento {
    @Getter private final String vendedor;
    @Getter private final String empresa;
    @Getter private final TipoAccionista tipoVendedor;
    @Getter private final double precio;
    @Getter private final int cantidad;

    public static NuevaOfertaAccionServer of (String vendedor, String empresa, TipoAccionista tipoAccionista, double precio, int cantidad){
        return new NuevaOfertaAccionServer(vendedor, empresa, tipoAccionista, precio, cantidad);
    }
}
