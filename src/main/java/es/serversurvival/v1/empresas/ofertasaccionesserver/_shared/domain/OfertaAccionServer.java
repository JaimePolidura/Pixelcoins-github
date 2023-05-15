package es.serversurvival.v1.empresas.ofertasaccionesserver._shared.domain;

import es.serversurvival.v1.empresas.accionistasserver._shared.domain.TipoAccionista;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
public final class OfertaAccionServer {
    @Getter private final UUID ofertaAccionServerId;
    @Getter private final String nombreOfertante;
    @Getter private final String empresa;
    @Getter private final double precio;
    @Getter private final int cantidad;
    @Getter private final String fecha;
    @Getter private final TipoAccionista tipoOfertante;
    @Getter private final double precioApertura;
    @Getter private final UUID accionistaEmpresaServerId;

    public OfertaAccionServer withNombreOfertante(String nombreOfertante){
        return new OfertaAccionServer(ofertaAccionServerId, nombreOfertante, empresa, precio,
                cantidad, fecha, tipoOfertante, precioApertura, accionistaEmpresaServerId);
    }

    public OfertaAccionServer decreaseCantidadBy(int cantidad){
        return new OfertaAccionServer(ofertaAccionServerId, nombreOfertante, empresa, precio,
                this.cantidad - cantidad, fecha, tipoOfertante, precioApertura, accionistaEmpresaServerId);
    }

    public boolean esTipoOfertanteJugador () {
        return this.tipoOfertante == TipoAccionista.JUGADOR;
    }
}
